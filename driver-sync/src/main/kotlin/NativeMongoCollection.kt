package fr.qsh.ktmongo.sync

import com.mongodb.client.result.UpdateResult
import com.mongodb.kotlin.client.FindIterable
import fr.qsh.ktmongo.dsl.LowLevelApi
import fr.qsh.ktmongo.dsl.expr.FilterExpression
import fr.qsh.ktmongo.dsl.expr.UpdateExpression
import fr.qsh.ktmongo.dsl.expr.common.CompoundExpression
import org.bson.BsonDocument
import org.bson.BsonDocumentWriter
import com.mongodb.kotlin.client.MongoCollection as OfficialMongoCollection

class NativeMongoCollection<Document : Any>(
	private val unsafe: OfficialMongoCollection<Document>,
) : MongoCollection<Document> {

	@LowLevelApi
	fun asOfficialMongoCollection() = unsafe

	@OptIn(LowLevelApi::class)
	private fun CompoundExpression.toBsonDocument(): BsonDocument {
		val bson = BsonDocument()

		BsonDocumentWriter(bson).use { writer ->
			this.writeTo(writer)
		}

		return bson
	}

	// region Find

	override fun find(): FindIterable<Document> =
		unsafe.find()

	override fun find(predicate: FilterExpression<Document>.() -> Unit): FindIterable<Document> {
		val bson = FilterExpression<Document>(unsafe.codecRegistry)
			.apply(predicate)
			.toBsonDocument()

		return unsafe.find(bson)
	}

	// endregion
	// region Count

	override fun count(): Long =
		unsafe.countDocuments()

	override fun count(predicate: FilterExpression<Document>.() -> Unit): Long {
		val bson = FilterExpression<Document>(unsafe.codecRegistry)
			.apply(predicate)
			.toBsonDocument()

		return unsafe.countDocuments(filter = bson)
	}

	override fun countEstimated(): Long =
		unsafe.estimatedDocumentCount()

	// endregion
	// region Update

	override fun updateOne(filter: FilterExpression<Document>.() -> Unit, update: UpdateExpression<Document>.() -> Unit): UpdateResult {
		val filterBson = FilterExpression<Document>(unsafe.codecRegistry)
			.apply(filter)
			.toBsonDocument()

		val updateBson = UpdateExpression<Document>(unsafe.codecRegistry)
			.apply(update)
			.toBsonDocument()

		return unsafe.updateOne(
			filter = filterBson,
			update = updateBson,
		)
	}

	override fun updateMany(filter: FilterExpression<Document>.() -> Unit, update: UpdateExpression<Document>.() -> Unit): UpdateResult {
		val filterBson = FilterExpression<Document>(unsafe.codecRegistry)
			.apply(filter)
			.toBsonDocument()

		val updateBson = UpdateExpression<Document>(unsafe.codecRegistry)
			.apply(update)
			.toBsonDocument()

		return unsafe.updateMany(
			filter = filterBson,
			update = updateBson,
		)
	}

	override fun findOneAndUpdate(filter: FilterExpression<Document>.() -> Unit, update: UpdateExpression<Document>.() -> Unit): Document? {
		val filterBson = FilterExpression<Document>(unsafe.codecRegistry)
			.apply(filter)
			.toBsonDocument()

		val updateBson = UpdateExpression<Document>(unsafe.codecRegistry)
			.apply(update)
			.toBsonDocument()

		return unsafe.findOneAndUpdate(
			filter = filterBson,
			update = updateBson,
		)
	}

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
