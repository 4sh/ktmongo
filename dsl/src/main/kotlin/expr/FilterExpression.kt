package fr.qsh.ktmongo.dsl.expr

import fr.qsh.ktmongo.dsl.KtMongoDsl
import fr.qsh.ktmongo.dsl.LowLevelApi
import fr.qsh.ktmongo.dsl.expr.common.CompoundExpression
import fr.qsh.ktmongo.dsl.expr.common.Expression
import fr.qsh.ktmongo.dsl.path.PropertyPath
import fr.qsh.ktmongo.dsl.path.path
import fr.qsh.ktmongo.dsl.writeArray
import fr.qsh.ktmongo.dsl.writeDocument
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.configuration.CodecRegistry
import javax.management.Query.and
import kotlin.internal.OnlyInputTypes
import kotlin.reflect.KProperty1

/**
 * DSL for MongoDB operators that are used as predicates in conditions.
 *
 * For example, these operators are available when querying with `find`, or as the filter in `updateOne`.
 */
@KtMongoDsl
class FilterExpression<T>(
	codec: CodecRegistry,
) : CompoundExpression(codec) {

	// region Low-level operations

	@LowLevelApi
	override fun simplify(children: List<Expression>): Expression? =
		when (children.size) {
			0 -> null
			1 -> this
			else -> AndFilterExpressionNode<T>(children, codec)
		}

	@LowLevelApi
	private sealed class FilterExpressionNode(codec: CodecRegistry) : Expression(codec)

	// endregion
	// region $and, $or

	/**
	 * Performs a logical `AND` operation on one or more expressions,
	 * and selects the documents that satisfy *all* the expressions.
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String?,
	 *     val age: Int,
	 * )
	 *
	 * collection.findOne {
	 *     and {
	 *         User::name eq "foo"
	 *         User::age eq 18
	 *     }
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/and/)
	 *
	 * @see or Logical `OR` operation.
	 */
	@OptIn(LowLevelApi::class)
	@KtMongoDsl
	fun and(block: FilterExpression<T>.() -> Unit) {
		accept(AndFilterExpressionNode<T>(FilterExpression<T>(codec).apply(block).children, codec))
	}

	@LowLevelApi
	private class AndFilterExpressionNode<T>(
		val declaredChildren: List<Expression>,
		codec: CodecRegistry,
	) : FilterExpressionNode(codec) {

		override fun simplify(): Expression? {
			if (declaredChildren.isEmpty())
				return null

			if (declaredChildren.size == 1)
				return FilterExpression<T>(codec).apply { accept(declaredChildren.single()) }

			// If there are nested $and operators, we combine them into the current one
			val nestedChildren = ArrayList<Expression>()

			for (child in declaredChildren) {
				if (child is AndFilterExpressionNode<*>) {
					for (nestedChild in child.declaredChildren) {
						nestedChildren += nestedChild
					}
				} else {
					nestedChildren += child
				}
			}

			return AndFilterExpressionNode<T>(nestedChildren, codec)
		}

		override fun write(writer: BsonWriter) {
			writer.writeDocument {
				writer.writeName("\$and")
				writer.writeArray {
					for (child in declaredChildren) {
						child.writeTo(writer)
					}
				}
			}
		}
	}

	/**
	 * Performs a logical `OR` operation on one or more expressions,
	 * and selects the documents that satisfy *at least one* of the expressions.
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String?,
	 *     val age: Int,
	 * )
	 *
	 * collection.find {
	 *     or {
	 *         User::name eq "foo"
	 *         User::name eq "bar"
	 *         User::age eq 18
	 *     }
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/or/)
	 *
	 * @see and Logical `AND` operation.
	 */
	@OptIn(LowLevelApi::class)
	@KtMongoDsl
	fun or(block: FilterExpression<T>.() -> Unit) {
		accept(OrFilterExpressionNode<T>(FilterExpression<T>(codec).apply(block).children, codec))
	}

	@LowLevelApi
	private class OrFilterExpressionNode<T>(
		val declaredChildren: List<Expression>,
		codec: CodecRegistry,
	) : FilterExpressionNode(codec) {

		override fun simplify(): Expression? {
			if (declaredChildren.isEmpty())
				return null

			if (declaredChildren.size == 1)
				return FilterExpression<T>(codec).apply { accept(declaredChildren.single()) }

			return super.simplify()
		}

		override fun write(writer: BsonWriter) {
			writer.writeDocument {
				writer.writeName("\$or")
				writer.writeArray {
					for (child in declaredChildren) {
						child.writeTo(writer)
					}
				}
			}
		}
	}

	// endregion
	// region Predicate access

	/**
	 * Targets a single field to execute a [targeted predicate][PredicateExpression].
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String?,
	 *     val age: Int,
	 * )
	 *
	 * collection.find {
	 *     User::name {
	 *         eq("foo")
	 *     }
	 * }
	 * ```
	 *
	 * Note that many operators available this way have a convenience function directly in this class to
	 * shorten this. For this example, see [eq]:
	 *
	 * ```kotlin
	 * collection.find {
	 *     User::name eq "foo"
	 * }
	 * ```
	 */
	@OptIn(LowLevelApi::class)
	@KtMongoDsl
	operator fun <@OnlyInputTypes V> KProperty1<T, V>.invoke(block: PredicateExpression<V>.() -> Unit) {
		accept(PredicateInFilterExpression(this.path().toString(), PredicateExpression<V>(codec).apply(block), codec))
	}

	@LowLevelApi
	private class PredicateInFilterExpression(
		val target: String,
		val expression: PredicateExpression<*>,
		codec: CodecRegistry,
	) : FilterExpressionNode(codec) {

		override fun write(writer: BsonWriter) {
			writer.writeDocument {
				writer.writeDocument(target) {
					expression.writeTo(writer)
				}
			}
		}
	}

	// endregion
	// region $not

	/**
	 * Performs a logical `NOT` operation on the specified [expression] and selects the
	 * documents that *do not* match the expression. This includes the elements
	 * that do not contain the field.
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val age: Int,
	 * )
	 *
	 * collection.find {
	 *     User::age not {
	 *         hasType(BsonType.STRING)
	 *     }
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/not/)
	 */
	@KtMongoDsl
	infix fun <@OnlyInputTypes V> KProperty1<T, V>.not(expression: PredicateExpression<V>.() -> Unit) {
		this { this.not(expression) }
	}

	// endregion
	// region $eq

	/**
	 * Matches documents where the value of a field equals the [value].
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String?,
	 *     val age: Int,
	 * )
	 *
	 * collection.find {
	 *     User::name eq "foo"
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/eq/)
	 *
	 * @see eqNotNull To only filter when the value is non-null.
	 * @see contains To make an equality check on one of the elements of a collection.
	 */
	@KtMongoDsl
	infix fun <@OnlyInputTypes V> KProperty1<T, V>.eq(value: V) {
		this { eq(value) }
	}

	/**
	 * Matches documents where the value of a field equals [value].
	 *
	 * If [value] is `null`, the operator is not added (all documents are matched).
	 *
	 * ### Example
	 *
	 * This operator is useful to simplify searches when the criteria is optional.
	 * For example, instead of writing:
	 * ```kotlin
	 * collection.find {
	 *     if (criteria.name != null)
	 *         User::name eq criteria.name
	 * }
	 * ```
	 * this operator can be used instead:
	 * ```kotlin
	 * collection.find {
	 *     User::name eqNotNull criteria.name
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/eq/)
	 *
	 * @see eq Equality filter.
	 */
	@KtMongoDsl
	infix fun <@OnlyInputTypes V> KProperty1<T, V>.eqNotNull(value: V?) {
		this { eqNotNull(value) }
	}

	// endregion
	// region $ne

	/**
	 * Matches documents where the value of a field does not equal the [value].
	 *
	 * The result includes documents which do not contain the specified field.
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String?,
	 *     val age: Int,
	 * )
	 *
	 * collection.find {
	 *     User::name ne "foo"
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/ne/)
	 *
	 * @see eq
	 */
	@KtMongoDsl
	infix fun <@OnlyInputTypes V> KProperty1<T, V>.ne(value: V) {
		this { ne(value) }
	}

	// endregion
	// region Predicates on array elements

	/**
	 * Allows to declare filters on the items of the specified field.
	 *
	 * ### Example
	 *
	 * This example will return all users who have at least one grade above 10:
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val grades: List<Int>,
	 * )
	 *
	 * collection.find {
	 *     User::grades.items() gte 10
	 * }
	 * ```
	 *
	 * ### Behavior with non-array fields
	 *
	 * TODO
	 *
	 * ### Using multiple criteria
	 *
	 * @see fr.qsh.ktmongo.dsl.path.get Refer to a specific item by its index.
	 */
	@OptIn(LowLevelApi::class)
	fun <@OnlyInputTypes V> KProperty1<T, Collection<V>>.items(): KProperty1<T, V> =
		PropertyPath(
			path = this.path(),
			backingProperty = this,
		)

	/**
	 * Matches documents where one of the items in the specified field is [value].
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val addresses: List<String>,
	 * )
	 *
	 * collection.find {
	 *     User::addresses contains "Some address"
	 * }
	 * ```
	 *
	 * All documents for which one of the `addresses` is equal to "Some address" are returned.
	 *
	 * ### Behavior with non-array fields
	 *
	 * MongoDB doesn't make a difference between "checking if one of the elements of an array matches the predicate" and "checking if the field matches the predicate".
	 *
	 * In the previous example, if a document had a field named `addresses` that was a `String` (**not** a `List<String>`), and its value was "Some address", it would be returned as well.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/eq/#array-element-equals-a-value)
	 *
	 * @see eq To make an equality check on the array itself, instead of one its elements.
	 * @see items Perform other kinds of filters on one of the items of an array.
	 * @see UpdateExpression.matched Update the element item matched by this function.
	 */
	@KtMongoDsl
	infix fun <@OnlyInputTypes V> KProperty1<T, Collection<V>>.contains(value: V) {
		this.items() eq value
	}

	// endregion
	// region $exists

	/**
	 * Matches documents that contain the specified field, including
	 * values where the field value is `null`.
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String?,
	 *     val age: Int,
	 * )
	 *
	 * collection.find {
	 *     User::age.exists()
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/exists/)
	 *
	 * @see doesNotExist Opposite.
	 * @see isNotNull Identical, but does not match elements where the field is `null`.
	 */
	@KtMongoDsl
	fun KProperty1<T, *>.exists() {
		this { exists() }
	}

	/**
	 * Matches documents that do not contain the specified field.
	 * Documents where the field if `null` are not matched.
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String?,
	 *     val age: Int,
	 * )
	 *
	 * collection.find {
	 *     User::age.doesNotExist()
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/exists/)
	 *
	 * @see exists Opposite.
	 * @see isNull Only matches documents that are specifically `null`.
	 */
	@KtMongoDsl
	fun KProperty1<T, *>.doesNotExist() {
		this { doesNotExist() }
	}

	// endregion
	// region $type

	/**
	 * Selects documents where the value of the field is an instance of the specified BSON [type].
	 *
	 * Querying by data type is useful when dealing with highly unstructured data where data types
	 * are not predictable.
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val age: Any,
	 * )
	 *
	 * collection.find {
	 *     User::age hasType BsonType.STRING
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/type/)
	 *
	 * @see isNull Checks if a value has the type [BsonType.NULL].
	 * @see isUndefined Checks if a value has the type [BsonType.UNDEFINED].
	 */
	@KtMongoDsl
	infix fun KProperty1<T, *>.hasType(type: BsonType) {
		this { hasType(type) }
	}

	/**
	 * Selects documents for which the field is `null`.
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val age: Int?,
	 * )
	 *
	 * collection.find {
	 *     User::age.isNull()
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/tutorial/query-for-null-fields/#type-check)
	 *
	 * @see doesNotExist Checks if the value is not set.
	 * @see isNotNull Opposite.
	 */
	@KtMongoDsl
	fun KProperty1<T, *>.isNull() {
		this { isNull() }
	}

	/**
	 * Selects documents for which the field is not `null`.
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val age: Int?,
	 * )
	 *
	 * collection.find {
	 *     User::age.isNotNull()
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/tutorial/query-for-null-fields/#type-check)
	 *
	 * @see isNull Opposite.
	 */
	@KtMongoDsl
	fun KProperty1<T, *>.isNotNull() {
		this { isNotNull() }
	}

	/**
	 * Selects documents for which the field is `undefined`.
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val age: Int?,
	 * )
	 *
	 * collection.find {
	 *     User::age.isUndefined()
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/tutorial/query-for-null-fields/#type-check)
	 *
	 * @see isNotUndefined Opposite.
	 */
	@KtMongoDsl
	fun KProperty1<T, *>.isUndefined() {
		this { isUndefined() }
	}

	/**
	 * Selects documents for which the field is not `undefined`.
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val age: Int?,
	 * )
	 *
	 * collection.find {
	 *     User::age.isNotUndefined()
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/tutorial/query-for-null-fields/#type-check)
	 *
	 * @see isUndefined Opposite.
	 */
	@KtMongoDsl
	fun KProperty1<T, *>.isNotUndefined() {
		this { isNotUndefined() }
	}

	// endregion
	// region $gt, $gte, $lt, $lte

	/**
	 * Selects documents for which this field has a value strictly greater than [value].
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val age: Int?,
	 * )
	 *
	 * collection.find {
	 *     User::age gt 18
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/gt/)
	 *
	 * @see gtNotNull
	 */
	@KtMongoDsl
	infix fun <@OnlyInputTypes V> KProperty1<T, V>.gt(value: V) {
		this { gt(value) }
	}

	/**
	 * Selects documents for which this field has a value strictly greater than [value].
	 *
	 * If [value] is `null`, the operator is not added (all elements are matched).
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val age: Int?
	 * )
	 *
	 * collection.find {
	 *     User::age gtNotNull 10
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/gt/)
	 *
	 * @see gt
	 * @see eqNotNull Learn more about the 'notNull' variants
	 */
	@KtMongoDsl
	infix fun <@OnlyInputTypes V> KProperty1<T, V>.gtNotNull(value: V?) {
		this { gtNotNull(value) }
	}

	/**
	 * Selects documents for which this field has a value greater or equal to [value].
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val age: Int?,
	 * )
	 *
	 * collection.find {
	 *     User::age gte 18
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/gte/)
	 *
	 * @see gteNotNull
	 */
	@KtMongoDsl
	infix fun <@OnlyInputTypes V> KProperty1<T, V>.gte(value: V) {
		this { gte(value) }
	}

	/**
	 * Selects documents for which this field has a value greater or equal to [value].
	 *
	 * If [value] is `null`, the operator is not added (all elements are matched).
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val age: Int?
	 * )
	 *
	 * collection.find {
	 *     User::age gteNotNull 10
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/gte/)
	 *
	 * @see gte
	 * @see eqNotNull Learn more about the 'notNull' variants
	 */
	@KtMongoDsl
	infix fun <@OnlyInputTypes V> KProperty1<T, V>.gteNotNull(value: V?) {
		this { gteNotNull(value) }
	}


	/**
	 * Selects documents for which this field has a value strictly lesser than [value].
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val age: Int?,
	 * )
	 *
	 * collection.find {
	 *     User::age lt 18
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/lt/)
	 *
	 * @see ltNotNull
	 */
	@KtMongoDsl
	infix fun <@OnlyInputTypes V> KProperty1<T, V>.lt(value: V) {
		this { lt(value) }
	}

	/**
	 * Selects documents for which this field has a value strictly lesser than [value].
	 *
	 * If [value] is `null`, the operator is not added (all elements are matched).
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val age: Int?
	 * )
	 *
	 * collection.find {
	 *     User::age ltNotNull 10
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/lt/)
	 *
	 * @see lt
	 * @see eqNotNull Learn more about the 'notNull' variants
	 */
	@KtMongoDsl
	infix fun <@OnlyInputTypes V> KProperty1<T, V>.ltNotNull(value: V?) {
		this { ltNotNull(value) }
	}

	/**
	 * Selects documents for which this field has a value lesser or equal to [value].
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val age: Int?,
	 * )
	 *
	 * collection.find {
	 *     User::age lte 18
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/lte/)
	 *
	 * @see lteNotNull
	 */
	@KtMongoDsl
	infix fun <@OnlyInputTypes V> KProperty1<T, V>.lte(value: V) {
		this { lte(value) }
	}

	/**
	 * Selects documents for which this field has a value lesser or equal to [value].
	 *
	 * If [value] is `null`, the operator is not added (all elements are matched).
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val age: Int?
	 * )
	 *
	 * collection.find {
	 *     User::age lteNotNull 10
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/lte/)
	 *
	 * @see lte
	 * @see eqNotNull Learn more about the 'notNull' variants
	 */
	@KtMongoDsl
	infix fun <@OnlyInputTypes V> KProperty1<T, V>.lteNotNull(value: V?) {
		this { lteNotNull(value) }
	}

	// endregion
	// region $in

	/**
	 * Selects documents for which this field is equal to one of the given [values].
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val age: Int?,
	 * )
	 *
	 * collection.find {
	 *     User::name.isOneOf(listOf("Alfred", "Arthur"))
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/in/)
	 *
	 * @see or
	 * @see eq
	 */
	@KtMongoDsl
	fun <@OnlyInputTypes V> KProperty1<T, V>.isOneOf(values: List<V>) {
		this { isOneOf(values) }
	}

	/**
	 * Selects documents for which this field is equal to one of the given [values].
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val age: Int?,
	 * )
	 *
	 * collection.find {
	 *     User::name.isOneOf("Alfred", "Arthur")
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/in/)
	 *
	 * @see or
	 * @see eq
	 */
	@KtMongoDsl
	fun <@OnlyInputTypes V> KProperty1<T, V>.isOneOf(vararg values: V) {
		isOneOf(values.asList())
	}

	// endregion
}
