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

	@LowLevelApi
	override fun simplify(children: List<Expression>): Expression? {
		if (children.isEmpty())
			return null

		var simplifiedChildren = children

		run {
			// Combine all $set operators together
			val sets = simplifiedChildren.filterIsInstance<SetExpressionNode>()
			val combinedSet =
				if (sets.size > 1)
					SetExpressionNode(sets.flatMap { it.mappings }, codec)
				else null
			if (combinedSet != null) {
				val childrenWithoutSets = simplifiedChildren - sets.toSet()
				simplifiedChildren = childrenWithoutSets + combinedSet
			}
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

}
