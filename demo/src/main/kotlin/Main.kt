package fr.qsh.ktmongo.demo

import com.mongodb.client.model.UpdateOptions
import com.mongodb.kotlin.client.MongoClient
import fr.qsh.ktmongo.sync.asKtMongo
import fr.qsh.ktmongo.sync.filter

data class Jedi(
	val name: String,
	val age: Int,
)

fun main() {
	val client = MongoClient.create()
	val database = client.getDatabase("test")
	val collection = database.getCollection<Jedi>("jedi").asKtMongo()

	collection.find {
		Jedi::name eq "foo"

		or {
			Jedi::age.doesNotExist()
			Jedi::age eq 18
		}
	}

	collection.filter {
		Jedi::name eq "foo"
	}.updateOne(UpdateOptions().upsert(true)) {
		Jedi::age set 19
	}
}
