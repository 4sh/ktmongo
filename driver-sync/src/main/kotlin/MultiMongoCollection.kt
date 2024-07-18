package fr.qsh.ktmongo.sync

import com.mongodb.kotlin.client.FindIterable
import com.mongodb.kotlin.client.MongoDatabase
import fr.qsh.ktmongo.dsl.expr.FilterExpression

private class MultiMongoCollection<Document : Any>(
	private val generator: () -> MongoCollection<Document>
) : MongoCollection<Document> {
	override fun find(): FindIterable<Document> =
		generator().find()

	override fun count(): Long =
		generator().count()

	override fun countEstimated(): Long =
		generator().countEstimated()

	override fun count(predicate: FilterExpression<Document>.() -> Unit): Long =
		generator().count(predicate)

	override fun find(predicate: FilterExpression<Document>.() -> Unit): FindIterable<Document> =
		generator().find(predicate)

}

/**
 * Wraps multiple collections based on the context, as computed by [generator].
 *
 * The [generator] function is executed in the context of each request, and is responsible for deciding which collection
 * the request will be routed to.
 *
 * ### Example
 *
 * A common use-case is for SaaS companies, to segregate between each client to its own collection to ensure
 * data from different clients isn't mixed.
 *
 * For example, if you store the client ID in a [ScopedValue]:
 * ```kotlin
 * val clientId = ScopedValue.newInstance<String>();
 *
 * val client = MongoClient.create()
 * val database = client.getDatabase("test")
 * val users = database.getMultiCollection<User> {
 *     it.getCollection<User>("users-${clientId.get()}").asKtMongo()
 * }
 *
 * ScopedValue.runWhere(clientId, "foo") {
 *     users.find() // finds in the collection "users-foo"
 * }
 *
 * ScopedValue.runWhere(clientId, "bar") {
 *     users.find() // finds in the collection "users-bar"
 * }
 * ```
 */
fun <Document : Any> MongoDatabase.getMultiCollection(generator: (MongoDatabase) -> MongoCollection<Document>): MongoCollection<Document> =
	MultiMongoCollection { generator(this) }
