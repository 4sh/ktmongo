package fr.qsh.ktmongo.dsl.expr

import fr.qsh.ktmongo.dsl.LowLevelApi
import fr.qsh.ktmongo.dsl.buildArray
import fr.qsh.ktmongo.dsl.buildDocument
import fr.qsh.ktmongo.dsl.path.path
import org.bson.BsonDocumentWriter
import org.bson.BsonType
import org.bson.codecs.configuration.CodecRegistry
import kotlin.reflect.KProperty1

/**
 * DSL for MongoDB operators that are used as predicates in conditions.
 *
 * For example, these operators are available when querying with `find`, or as the filter in `updateOne`.
 */
class PredicateExpression<T>(
	@property:LowLevelApi
	@PublishedApi
	internal val writer: BsonDocumentWriter,

	@PublishedApi
	internal val codec: CodecRegistry,
) {

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
	 */
	@OptIn(LowLevelApi::class)
	inline fun and(block: PredicateExpression<T>.() -> Unit) {
		writer.buildDocument("\$and") {
			writer.buildArray {
				block()
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
	 */
	@OptIn(LowLevelApi::class)
	inline fun or(block: PredicateExpression<T>.() -> Unit) {
		writer.buildDocument("\$or") {
			writer.buildArray {
				block()
			}
		}
	}

	/**
	 * Targets a single field to execute a [targeted predicate][TargetedPredicateExpression].
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
	 * shorten this. For this example, see [eq].
	 */
	@OptIn(LowLevelApi::class)
	inline operator fun <V> KProperty1<T, V>.invoke(block: TargetedPredicateExpression<V>.() -> Unit) {
		writer.buildDocument(this.path().toString()) {
			TargetedPredicateExpression<V>(writer, codec).apply(block)
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
	inline infix fun <V> KProperty1<T, V>.not(expression: TargetedPredicateExpression<V>.() -> Unit) {
		this { this.not(expression) }
	}

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
	infix fun <V> KProperty1<T, V>.eq(value: V) {
		this { eq(value) }
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
	 *     User::age.exists()
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/exists/)
	 */
	fun KProperty1<T, *>.exists() {
		this { exists() }
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
	 *     User::age.doesNotExist()
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/exists/)
	 */
	fun KProperty1<T, *>.doesNotExist() {
		this { doesNotExist() }
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
	 *     User::age hasType BsonType.STRING
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/type/)
	 */
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
	 */
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
	 */
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
	 */
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
	 */
	fun KProperty1<T, *>.isNotUndefined() {
		this { isNotUndefined() }
	}

}
