package fr.qsh.ktmongo.sync

import com.mongodb.kotlin.client.FindIterable
import fr.qsh.ktmongo.dsl.LowLevelApi
import fr.qsh.ktmongo.dsl.expr.FilterExpression
import org.bson.BsonDocument
import org.bson.BsonDocumentWriter
import com.mongodb.kotlin.client.MongoCollection as OfficialMongoCollection

class NativeMongoCollection<Document : Any>(
	private val unsafe: OfficialMongoCollection<Document>,
) : MongoCollection<Document> {

	@LowLevelApi
	fun asOfficialMongoCollection() = unsafe

	// region Find

	override fun find(): FindIterable<Document> =
		unsafe.find()

	override fun find(predicate: FilterExpression<Document>.() -> Unit): FindIterable<Document> {
		val bson = BsonDocument()

		@OptIn(LowLevelApi::class)
		BsonDocumentWriter(bson).use { writer ->
			FilterExpression<Document>(unsafe.codecRegistry)
				.apply(predicate)
				.writeTo(writer)
		}

		return unsafe.find(bson.asDocument())
	}

	// endregion
	// region Count

	override fun count(): Long =
		unsafe.countDocuments()

	override fun count(predicate: FilterExpression<Document>.() -> Unit): Long {
		val bson = BsonDocument()

		@OptIn(LowLevelApi::class)
		BsonDocumentWriter(bson).use { writer ->
			FilterExpression<Document>(unsafe.codecRegistry)
				.apply(predicate)
				.writeTo(writer)
		}

		return unsafe.countDocuments(filter = bson)
	}

	override fun countEstimated(): Long =
		unsafe.estimatedDocumentCount()

	// endregion
}

/**
 * Converts a MongoDB collection from the Kotlin driver into a KtMongo collection.
 *
 * ### Example
 *
 * ```kotlin
 * val client = MongoClient.create()
 * val database = client.getDatabase("test")
 * val collection = database.getCollection<User>("users").asKtMongo()
 * ```
 */
fun <T : Any> OfficialMongoCollection<T>.asKtMongo() =
	NativeMongoCollection(this)
