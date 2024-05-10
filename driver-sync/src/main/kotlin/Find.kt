package fr.qsh.ktmongo.sync

import com.mongodb.kotlin.client.FindIterable
import fr.qsh.ktmongo.dsl.LowLevelApi
import fr.qsh.ktmongo.dsl.expr.FilterExpression
import org.bson.BsonDocument
import org.bson.BsonDocumentWriter

/**
 * Finds all documents in the collection.
 *
 * ### External resources
 *
 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/method/db.collection.find/)
 */
@OptIn(LowLevelApi::class)
fun <T : Any> MongoCollection<T>.find(): FindIterable<T> {
	return unsafe.find()
}

/**
 * Finds all documents in the collection that satisfy [predicate].
 *
 * If multiple expressions are specified, an [and][FilterExpression.and] is used by default.
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
 *     User::name eq "foo"
 *     User::age eq 18
 * }
 * ```
 *
 * ### External resources
 *
 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/method/db.collection.find/)
 */
@OptIn(LowLevelApi::class)
fun <T : Any> MongoCollection<T>.find(predicate: FilterExpression<T>.() -> Unit): FindIterable<T> {
	val bson = BsonDocument()

	BsonDocumentWriter(bson).use { writer ->
		FilterExpression<T>(unsafe.codecRegistry)
			.apply(predicate)
			.writeTo(writer)
	}

	return unsafe.find(bson.asDocument())
}

/**
 * Finds one or zero documents in the collection that satisfy [predicate].
 *
 * If multiple expressions are specified, an [and][FilterExpression.and] is used by default.
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
 *     User::age eq 18
 * }
 * ```
 *
 * ### External resources
 *
 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/method/db.collection.find/)
 */
@OptIn(LowLevelApi::class)
fun <T : Any> MongoCollection<T>.findOne(predicate: FilterExpression<T>.() -> Unit): T? =
	find(predicate).firstOrNull()
