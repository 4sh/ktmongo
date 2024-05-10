package fr.qsh.ktmongo.sync

import fr.qsh.ktmongo.dsl.LowLevelApi
import fr.qsh.ktmongo.dsl.expr.FilterExpression
import org.bson.BsonDocument
import org.bson.BsonDocumentWriter

/**
 * Counts all documents in a collection.
 *
 * ### External resources
 *
 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/method/db.collection.countDocuments/)
 *
 * @see countDocumentsEstimated Faster alternative when the result doesn't need to be exact.
 */
@OptIn(LowLevelApi::class)
fun <T : Any> MongoCollection<T>.countDocuments(): Long {
	return unsafe.countDocuments()
}

/**
 * Counts documents that match [predicate] in a collection.
 *
 * ### External resources
 *
 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/method/db.collection.countDocuments/)
 */
@OptIn(LowLevelApi::class)
fun <T : Any> MongoCollection<T>.countDocuments(predicate: FilterExpression<T>.() -> Unit): Long {
	val bson = BsonDocument()

	BsonDocumentWriter(bson).use { writer ->
		FilterExpression<T>(unsafe.codecRegistry)
			.apply(predicate)
			.writeTo(writer)
	}

	return unsafe.countDocuments(filter = bson)
}

/**
 * Counts documents in a collection.
 *
 * This function reads collection metadata instead of actually counting through all documents.
 * This makes it much more performant (almost no CPU nor RAM usage), but the count may be slightly out of date.
 *
 * Note that this count may become inaccurate when:
 * - there are orphaned documents in a sharded cluster,
 * - an unclean shutdown happened.
 *
 * Views do not possess the required metadata. Thus, when this function is called on a view, a regular [countDocuments]
 * is executed instead.
 *
 * ### External resources
 *
 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/method/db.collection.estimatedDocumentCount/)
 */
@OptIn(LowLevelApi::class)
fun <T : Any> MongoCollection<T>.countDocumentsEstimated(): Long {
	return unsafe.estimatedDocumentCount()
}
