package fr.qsh.ktmongo.dsl.expr

import fr.qsh.ktmongo.dsl.KtMongoDsl
import fr.qsh.ktmongo.dsl.LowLevelApi
import fr.qsh.ktmongo.dsl.expr.common.AbstractCompoundExpression
import fr.qsh.ktmongo.dsl.expr.common.AbstractExpression
import fr.qsh.ktmongo.dsl.expr.common.Expression
import fr.qsh.ktmongo.dsl.path.PropertySyntaxScope
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
 * ### Example
 *
 * This expression type is available in multiple operators, most commonly `find`:
 * ```kotlin
 * class User(
 *     val name: String,
 *     val age: Int,
 * )
 *
 * collection.find {
 *     User::age gte 18
 * }
 * ```
 *
 * ### Beware of arrays!
 *
 * MongoDB operators do not discriminate between scalars and arrays.
 * When an array is encountered, all operators attempt to match on the array itself.
 * If the match fails, the operators attempt to match array elements.
 *
 * It is not possible to mimic this behavior in KtMongo while still keeping type-safety,
 * so operators may behave strangely when arrays are encountered.
 *
 * Note that if the collection corresponds to the declared Kotlin type,
 * these situations can never happen, as the Kotlin type system doesn't allow them to.
 *
 * When developers attempt to perform an operator on the entire array,
 * they should use operators as normal:
 * ```kotlin
 * class User(
 *     val name: String,
 *     val favoriteNumbers: List<Int>
 * )
 *
 * collection.find {
 *     User::favoriteNumbers eq listOf(1, 2)
 * }
 * ```
 * Developers should use the request above when they want to match a document similar to:
 * ```json
 * {
 *     favoriteNumbers: [1, 2]
 * }
 * ```
 * The following document will NOT match:
 * ```json
 * {
 *     favoriteNumbers: [3]
 * }
 * ```
 *
 * However, due to MongoDB's behavior when encountering arrays, it should be noted
 * that the following document WILL match:
 * ```json
 * {
 *     favoriteNumbers: [
 *         [3],
 *         [1, 2],
 *         [7, 2]
 *     ]
 * }
 * ```
 *
 * To execute an operator on one of the elements of an array, see [any].
 *
 */
@KtMongoDsl
class FilterExpression<T>(
	codec: CodecRegistry,
) : AbstractCompoundExpression(codec), PropertySyntaxScope {

	// region Low-level operations

	@LowLevelApi
	override fun simplify(children: List<Expression>): AbstractExpression? =
		when (children.size) {
			0 -> null
			1 -> this
			else -> AndFilterExpressionNode<T>(children, codec)
		}

	@LowLevelApi
	private sealed class FilterExpressionNode(codec: CodecRegistry) : AbstractExpression(codec)

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

		override fun simplify(): AbstractExpression? {
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
			writer.writeName("\$and")
			writer.writeArray {
				for (child in declaredChildren) {
					writer.writeDocument {
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

		override fun simplify(): AbstractExpression? {
			if (declaredChildren.isEmpty())
				return null

			if (declaredChildren.size == 1)
				return FilterExpression<T>(codec).apply { accept(declaredChildren.single()) }

			return super.simplify()
		}

		override fun write(writer: BsonWriter) {
			writer.writeName("\$or")
			writer.writeArray {
				for (child in declaredChildren) {
					writer.writeDocument {
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
		val expression: Expression,
		codec: CodecRegistry,
	) : FilterExpressionNode(codec) {

		override fun simplify(): AbstractExpression? =
			expression.simplify()
				?.let { PredicateInFilterExpression(target, it, codec) }

		override fun write(writer: BsonWriter) {
			writer.writeDocument(target) {
				expression.writeTo(writer)
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
	// region $elemMatch

	/**
	 * Specify operators on array elements.
	 *
	 * ### Example
	 *
	 * Find any user who has 12 as one of their favorite numbers.
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val favoriteNumbers: List<Int>
	 * )
	 *
	 * collection.find {
	 *     User::favoriteNumbers.any eq 12
	 * }
	 * ```
	 *
	 * ### Repeated usages will match different items
	 *
	 * Note that if `any` is used multiple times, it may test different items.
	 * For example, the following request will match the following document:
	 * ```kotlin
	 * collection.find {
	 *     User::favoriteNumbers.any gt 2
	 *     User::favoriteNumbers.any lte 7
	 * }
	 * ```
	 * ```json
	 * {
	 *     "name": "Nicolas",
	 *     "favoriteNumbers": [ 1, 9 ]
	 * }
	 * ```
	 * Because 1 is less than 7, and 9 is greater than 2, the document is returned.
	 *
	 * If you want to apply multiple filters to the same item, use the [any] function.
	 *
	 * ### Arrays don't exist in finds!
	 *
	 * MongoDB operators do not discriminate between scalars and arrays.
	 * When an array is encountered, all operators attempt to match on the array itself.
	 * If the match fails, the operators attempt to match array elements.
	 *
	 * It is not possible to mimic this behavior in KtMongo while still keeping type-safety,
	 * so KtMongo has different operators to filter a collection itself or its elements.
	 *
	 * As a consequence, the request:
	 * ```kotlin
	 * collection.find {
	 *     User::favoriteNumbers.any eq 5
	 * }
	 * ```
	 * will, as expected, match the following document:
	 * ```json
	 * {
	 *     favoriteNumbers: [1, 4, 5, 10]
	 * }
	 * ```
	 *
	 * It is important to note that it WILL also match this document:
	 * ```json
	 * {
	 *     favoriteNumbers: 5
	 * }
	 * ```
	 *
	 * Since this document doesn't conform to the Kotlin declared type `List<Int>`,
	 * it is unlikely that such an element exists, but developers should keep it in mind.
	 *
	 * ### External resources
	 *
	 * - [Official document](https://www.mongodb.com/docs/manual/tutorial/query-arrays/)
	 */
	@KtMongoDsl
	val <V> KProperty1<T, Collection<V>>.any: KProperty1<T, V>
		@Suppress("UNCHECKED_CAST") // The type parameters are fake anyway
		get() = this as KProperty1<T, V>

	/**
	 * Combines Kotlin properties into a path usable to point to any item in an array.
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val grades: List<Grade>
	 * )
	 *
	 * class Grade(
	 *     val name: Int
	 * )
	 *
	 * collection.find {
	 *     User::grades / Grade::name eq 19
	 * }
	 * ```
	 *
	 * This function is a shorthand for `any`:
	 * ```kotlin
	 * collection.find {
	 *     User::grades.any / Gradle::name eq 19
	 * }
	 * ```
	 */
	@KtMongoDsl
	@JvmName("anyChild")
	operator fun <V, V2> KProperty1<T, Collection<V>>.div(other: KProperty1<V, V2>): KProperty1<T, V2> =
		this.any.div(other)

	/**
	 * Specify multiple operators on a single array element.
	 *
	 * ### Example
	 *
	 * Find students with a grade between 8 and 10, that may be eligible to perform
	 * an exam a second time.
	 *
	 * ```kotlin
	 * class Student(
	 *     val name: String,
	 *     val grades: List<Int>
	 * )
	 *
	 * collection.find {
	 *     Student::grades.any {
	 *         gte(8)
	 *         lte(10)
	 *     }
	 * }
	 * ```
	 *
	 * The following document will match because the grade 9 is in the interval.
	 * ```json
	 * {
	 *     "name": "John",
	 *     "grades": [9, 3]
	 * }
	 * ```
	 *
	 * The following document will NOT match, because none of the grades are in the interval.
	 * ```json
	 * {
	 *     "name": "Lea",
	 *     "grades": [18, 19]
	 * }
	 * ```
	 *
	 * If you want to perform multiple checks on different elements of an array,
	 * see the [any] property.
	 *
	 * This function only allows specifying operators on array elements directly.
	 * To specify operators on sub-fields of array elements, see [anyObject].
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/elemMatch/)
	 */
	@OptIn(LowLevelApi::class)
	@KtMongoDsl
	fun <V> KProperty1<T, Collection<V>>.any(block: PredicateExpression<V>.() -> Unit) {
		accept(ElementMatchExpressionNode<V>(this.path().toString(), PredicateExpression<V>(codec).apply(block), codec))
	}

	/**
	 * Specify multiple operators on fields of a single array element.
	 *
	 * ### Example
	 *
	 * Find customers who have a pet that is born this month, as they may be eligible for a discount.
	 *
	 * ```kotlin
	 * class Customer(
	 *     val name: String,
	 *     val pets: List<Pet>,
	 * )
	 *
	 * class Pet(
	 *     val name: String,
	 *     val birthMonth: Int
	 * )
	 *
	 * val currentMonth = 3
	 *
	 * collection.find {
	 *     Customer::pets.anyObject {
	 *         Pet::birthMonth gte currentMonth
	 *         Pet::birthMonth lte (currentMonth + 1)
	 *     }
	 * }
	 * ```
	 *
	 * The following document will match:
	 * ```json
	 * {
	 *     "name": "Fred",
	 *     "pets": [
	 *         {
	 *             "name": "Arthur",
	 *             "birthMonth": 5
	 *         },
	 *         {
	 *             "name": "Gwen",
	 *             "birthMonth": 3
	 *         }
	 *     ]
	 * }
	 * ```
	 * because the pet "Gwen" has a matching birth month.
	 *
	 * If you want to perform operators on the elements directly (not on their fields), use
	 * [any] instead.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/elemMatch/)
	 */
	@OptIn(LowLevelApi::class)
	@KtMongoDsl
	fun <V> KProperty1<T, Collection<V>>.anyObject(block: FilterExpression<V>.() -> Unit) {
		accept(ElementMatchExpressionNode<V>(this.path().toString(), FilterExpression<V>(codec).apply(block), codec))
	}

	@LowLevelApi
	private class ElementMatchExpressionNode<T>(
		val target: String,
		val expression: Expression,
		codec: CodecRegistry,
	) : FilterExpressionNode(codec) {

		override fun simplify(): AbstractExpression =
			ElementMatchExpressionNode<T>(target, expression.simplify()
				?: OrFilterExpressionNode<T>(emptyList(), codec), codec)

		override fun write(writer: BsonWriter) {
			writer.writeDocument(target) {
				writer.writeName("\$elemMatch")
				writer.writeStartDocument()
				expression.writeTo(writer)
				writer.writeEndDocument()
			}
		}
	}

	// endregion
}
