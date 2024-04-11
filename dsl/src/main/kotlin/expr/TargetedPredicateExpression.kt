package fr.qsh.ktmongo.dsl.expr

import fr.qsh.ktmongo.dsl.LowLevelApi
import fr.qsh.ktmongo.dsl.buildDocument
import org.bson.BsonDocumentWriter
import org.bson.BsonType
import org.bson.codecs.Encoder
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry

/**
 * DSL for MongoDB operators that are used as predicates in conditions in a context where the targeted field is already
 * specified.
 */
@OptIn(LowLevelApi::class)
class TargetedPredicateExpression<T>(
	@property:LowLevelApi
	@PublishedApi
	internal val writer: BsonDocumentWriter,

	@PublishedApi
	internal val codec: CodecRegistry,
) {

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
	 *     User::name {
	 *         eq("foo")
	 *     }
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/eq/)
	 *
	 * @see PredicateExpression.eq Shorthand.
	 */
	fun eq(value: T) {
		writer.buildDocument("\$eq") {
			if (value == null) {
				writer.writeNull()
			} else {
				@Suppress("UNNECESSARY_NOT_NULL_ASSERTION", "UNCHECKED_CAST") // Kotlin doesn't smart-cast here, but should, this is safe
				(codec.get(value!!::class.java) as Encoder<T>)
					.encode(writer, value, EncoderContext.builder().build())
			}
		}
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
	 *     User::name {
	 *         if (criteria.name != null)
	 *             eq(criteria.name)
	 *     }
	 * }
	 * ```
	 * this operator can be used instead:
	 * ```kotlin
	 * collection.find {
	 *     User::name {
	 *         eqNotNull(criteria.name)
	 *     }
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/eq/)
	 *
	 * @see PredicateExpression.eqNotNull Shorthand.
	 * @see eq Equality filter.
	 */
	fun eqNotNull(value: T?) {
		if (value != null) eq(value)
	}

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
	 *     User::name {
	 *         exists()
	 *     }
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/exists/)
	 *
	 * @see PredicateExpression.exists Shorthand.
	 * @see doesNotExist Opposite.
	 * @see isNotNull Identical, but does not match elements where the field is `null`.
	 */
	fun exists() {
		writer.buildDocument("\$exists") {
			writer.writeBoolean(true)
		}
	}

	/**
	 * Matches documents that do not contain the specified field.
	 * Documents where the field if `null` are counted as existing.
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
	 *         doesNotExist()
	 *     }
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/exists/)
	 *
	 * @see PredicateExpression.doesNotExist Shorthand.
	 * @see exists Opposite.
	 * @see isNull Only matches elements that are specifically `null`.
	 */
	fun doesNotExist() {
		writer.buildDocument("\$exists") {
			writer.writeBoolean(false)
		}
	}

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
	 *     User::age {
	 *         type(BsonType.STRING)
	 *     }
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/type/)
	 *
	 * @see PredicateExpression.hasType Shorthand.
	 * @see isNull Checks if a value has the type [BsonType.NULL].
	 * @see isUndefined Checks if a value has the type [BsonType.UNDEFINED].
	 */
	fun hasType(type: BsonType) {
		writer.buildDocument("\$type") {
			writer.writeInt32(type.value)
		}
	}

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
	 *     User::age {
	 *         not {
	 *             hasType(BsonType.STRING)
	 *         }
	 *     }
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/not/)
	 *
	 * @see PredicateExpression.not Shorthand.
	 */
	inline fun not(expression: TargetedPredicateExpression<T>.() -> Unit) {
		writer.buildDocument("\$not") {
			TargetedPredicateExpression<T>(writer, codec).apply(expression)
		}
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
	 *     User::age { isNull() }
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/tutorial/query-for-null-fields/#type-check)
	 *
	 * @see PredicateExpression.isNull Shorthand.
	 * @see doesNotExist Checks if the value is not set.
	 * @see isNotNull Opposite.
	 */
	fun isNull() =
		hasType(BsonType.NULL)

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
	 *     User::age { isNotNull() }
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/tutorial/query-for-null-fields/#type-check)
	 *
	 * @see PredicateExpression.isNotNull Shorthand.
	 * @see isNull Opposite.
	 */
	fun isNotNull() =
		not { isNull() }

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
	 *     User::age { isUndefined() }
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/tutorial/query-for-null-fields/#type-check)
	 *
	 * @see PredicateExpression.isUndefined Shorthand.
	 * @see isNotUndefined Opposite.
	 */
	fun isUndefined() =
		hasType(BsonType.UNDEFINED)

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
	 *     User::age { isNotUndefined() }
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/tutorial/query-for-null-fields/#type-check)
	 *
	 * @see PredicateExpression.isNotUndefined Shorthand.
	 * @see isUndefined Opposite.
	 */
	fun isNotUndefined() =
		not { isUndefined() }
}
