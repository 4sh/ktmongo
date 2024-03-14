package fr.qsh.ktmongo.demo

import com.mongodb.kotlin.client.MongoClient
import fr.qsh.ktmongo.sync.asKtMongo
import fr.qsh.ktmongo.sync.find

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
}
