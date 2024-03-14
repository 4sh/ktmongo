package fr.qsh.ktmongo.dsl.expr

import fr.qsh.ktmongo.dsl.LowLevelApi
import fr.qsh.ktmongo.dsl.buildArray
import fr.qsh.ktmongo.dsl.buildDocument
import fr.qsh.ktmongo.dsl.path.path
import org.bson.BsonDocumentWriter
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

}
