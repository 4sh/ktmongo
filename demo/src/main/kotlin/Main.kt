package fr.qsh.ktmongo.demo

import com.mongodb.kotlin.client.MongoClient
import fr.qsh.ktmongo.dsl.path.div
import fr.qsh.ktmongo.sync.asKtMongo
import fr.qsh.ktmongo.sync.filter

data class Jedi(
	val name: String,
	val age: Int,
	val level: Int,
	val friends: List<Friend>,
)

data class Friend(
	val name: String,
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
	}.upsertOne {
		Jedi::age set 19
		Jedi::level inc 1
	}

	collection.find {
		Jedi::friends.items() / Friend::name eq "Foo"
	}
}
