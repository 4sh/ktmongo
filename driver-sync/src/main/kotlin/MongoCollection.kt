package fr.qsh.ktmongo.sync

import fr.qsh.ktmongo.dsl.LowLevelApi
import com.mongodb.kotlin.client.MongoCollection as OfficialMongoCollection

class MongoCollection<T : Any>(
	@property:LowLevelApi
	val unsafe: OfficialMongoCollection<T>,
)

fun <T : Any> OfficialMongoCollection<T>.asKtMongo() =
	MongoCollection(this)
