package fr.qsh.ktmongo.sync

import com.mongodb.kotlin.client.FindIterable
import fr.qsh.ktmongo.dsl.expr.FilterExpression

/**
 * Parent interface to all collection types provided by KtMongo.
 *
 * To obtain an instance of this interface, see [asKtMongo].
 */
sealed interface MongoCollection<Document : Any> {

	// region Find

	/**
	 * Finds all documents in this collection.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/method/db.collection.find/)
	 */
	fun find(): FindIterable<Document>

	/**
	 * Finds all documents in this collection that satisfy [predicate].
	 *
	 * If multiple predicates are specified, and [and][FilterExpression.and] operator is implied.
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
	 *    User::name eq "foo"
	 *    User::age eq 10
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/method/db.collection.find/)
	 *
	 * @see findOne When only one result is expected.
	 */
	fun find(predicate: FilterExpression<Document>.() -> Unit): FindIterable<Document>

	/**
	 * Finds a document in this collection that satisfies [predicate].
	 *
	 * If multiple predicates are specified, and [and][FilterExpression.and] operator is implied.
	 *
	 * This function doesn't check that there is exactly one value in the collection.
	 * It simply returns the first matching document it finds.
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val age: Int,
	 * )
	 *
	 * collection.findOne {
	 *     User::name eq "foo"
	 *     User::age eq 10
	 * }
	 * ```
	 *
	 * @see find When multiple results are expected.
	 */
	fun findOne(predicate: FilterExpression<Document>.() -> Unit): Document? =
		find(predicate).firstOrNull()

	// endregion
	// region Count

	/**
	 * Counts all documents in the collection.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/method/db.collection.countDocuments/)
	 *
	 * @see countEstimated Faster alternative when the result doesn't need to be exact.
	 */
	fun count(): Long

	/**
	 * Counts how many documents match [predicate] in the collection.
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String,
	 *     val age: Int,
	 * )
	 *
	 * collection.countDocuments {
	 *     User::name eq "foo"
	 *     User::age eq 10
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/method/db.collection.countDocuments/)
	 */
	fun count(predicate: FilterExpression<Document>.() -> Unit): Long

	/**
	 * Counts all documents in the collection.
	 *
	 * This function reads collection metadata instead of actually counting through all documents.
	 * This makes it much more performant (almost no CPU nor RAM usage), but the count may be slightly out of date.
	 *
	 * In particular, it may become inaccurate when:
	 * - there are orphaned documents in a shared cluster,
	 * - an unclean shutdown happened.
	 *
	 * Views do not possess the required metadata.
	 * When this function is called on a view (either a MongoDB view or a [filter] logical view), a regular [count] is executed instead.
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/method/db.collection.estimatedDocumentCount/)
	 *
	 * @see count Perform the count for real.
	 */
	fun countEstimated(): Long

	// endregion

}
