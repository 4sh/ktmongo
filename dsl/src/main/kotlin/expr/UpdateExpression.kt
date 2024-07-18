package fr.qsh.ktmongo.dsl.expr

import fr.qsh.ktmongo.dsl.KtMongoDsl
import fr.qsh.ktmongo.dsl.LowLevelApi
import fr.qsh.ktmongo.dsl.expr.common.CompoundExpression
import fr.qsh.ktmongo.dsl.expr.common.Expression
import fr.qsh.ktmongo.dsl.expr.common.acceptAll
import fr.qsh.ktmongo.dsl.path.Path
import fr.qsh.ktmongo.dsl.path.path
import fr.qsh.ktmongo.dsl.writeDocument
import fr.qsh.ktmongo.dsl.writeObject
import org.bson.BsonWriter
import org.bson.codecs.configuration.CodecRegistry
import kotlin.internal.OnlyInputTypes
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

/**
 * DSL for MongoDB operators that are used to update fields.
 *
 * For example, these operators are available with `insertOne` or as the update in `updateOne`.
 */
@KtMongoDsl
class UpdateExpression<T>(
	codec: CodecRegistry,
) : CompoundExpression(codec) {

	// region Low-level operations

	private class OperatorCombinator<T : Expression>(
		val type: KClass<T>,
		val combinator: (List<T>, CodecRegistry) -> T
	) {
		@Suppress("UNCHECKED_CAST") // This is a private class, it should not be used incorrectly
		operator fun invoke(sources: List<Expression>, codec: CodecRegistry) =
			combinator(sources as List<T>, codec)
	}

	@LowLevelApi
	override fun simplify(children: List<Expression>): Expression? {
		if (children.isEmpty())
			return null

		val simplifiedChildren = combinators.fold(children) { newChildren, combinator ->
			val matching = newChildren.filterIsInstance(combinator.type.java)

			if (matching.size <= 1)
				// At least two elements are required to combine them into a single one!
				return@fold newChildren

			val childrenWithoutMatching = newChildren - matching.toSet()
			childrenWithoutMatching + combinator(matching, codec)
		}

		if (simplifiedChildren != children)
			return UpdateExpression<T>(codec).apply {
				acceptAll(simplifiedChildren)
			}
		return this
	}

	@LowLevelApi
	private sealed class UpdateExpressionNode(codec: CodecRegistry) : Expression(codec)

	// endregion
	// region $set

	/**
	 * Replaces the value of a field with the specified [value].
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String?,
	 *     val age: Int,
	 * )
	 *
	 * collection.update {
	 *     User::age set 18
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/update/set/)
	 *
	 * @see setOnInsert Only set if a new document is created.
	 */
	@OptIn(LowLevelApi::class)
	@KtMongoDsl
	infix fun <@OnlyInputTypes V> KProperty1<T, V>.set(value: V) {
		accept(SetExpressionNode(listOf(this.path() to value), codec))
	}

	@LowLevelApi
	private class SetExpressionNode(
		val mappings: List<Pair<Path, *>>,
		codec: CodecRegistry,
	) : UpdateExpressionNode(codec) {

		override fun simplify() =
			this.takeUnless { mappings.isEmpty() }

		override fun write(writer: BsonWriter) {
			writer.writeDocument("\$set") {
				for ((field, value) in mappings) {
					writer.writeName(field.toString())
					writer.writeObject(value, codec)
				}
			}
		}
	}

	// endregion
	// region $setOnInsert

	/**
	 * If an upsert operation results in an insert of a document,
	 * then this operator assigns the specified [value] to the field.
	 * If the update operation does not result in an insert, this operator does nothing.
	 *
	 * If used in an update operation that isn't an upset, no document can be inserted,
	 * and thus this operator never does anything.
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String?,
	 *     val age: Int,
	 * )
	 *
	 * collection.update {
	 *     User::age setOnInsert 18
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/update/setOnInsert/)
	 *
	 * @see set Always set the value.
	 */
	// TODO: make the above example an upsert
	@OptIn(LowLevelApi::class)
	@KtMongoDsl
	infix fun <@OnlyInputTypes V> KProperty1<T, V>.setOnInsert(value: V) {
		accept(SetOnInsertExpressionNode(listOf(this.path() to value), codec))
	}

	@LowLevelApi
	private class SetOnInsertExpressionNode(
		val mappings: List<Pair<Path, *>>,
		codec: CodecRegistry,
	) : UpdateExpressionNode(codec) {
		override fun simplify(): Expression? =
			this.takeUnless { mappings.isEmpty() }

		override fun write(writer: BsonWriter) {
			writer.writeDocument("\$setOnInsert") {
				for ((field, value) in mappings) {
					writer.writeName(field.toString())
					writer.writeObject(value, codec)
				}
			}
		}
	}

	// endregion

	companion object {
		@OptIn(LowLevelApi::class)
		private val combinators = listOf(
			OperatorCombinator(SetExpressionNode::class) { sources, codec ->
				SetExpressionNode(sources.flatMap { it.mappings }, codec)
			},
			OperatorCombinator(SetOnInsertExpressionNode::class) { sources, codec ->
				SetOnInsertExpressionNode(sources.flatMap { it.mappings }, codec)
			}
		)
	}
}
