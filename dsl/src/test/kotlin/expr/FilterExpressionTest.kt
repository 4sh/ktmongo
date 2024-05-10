package fr.qsh.ktmongo.dsl.expr

import io.kotest.core.spec.style.FunSpec
import org.bson.BsonType

@Suppress("unused")
class FilterExpressionTest : FunSpec({

	class User(
		val id: String,
		val name: String,
		val age: Int?,
	)

	fun <T> filter(block: FilterExpression<T>.() -> Unit): String =
		FilterExpression<T>(testCodec()).apply(block).toString(simplified = true)

	val eq = "\$eq"
	val and = "\$and"
	val or = "\$or"
	val exists = "\$exists"
	val type = "\$type"
	val not = "\$not"
	val isOneOf = "\$in"

	context("Operator $eq") {
		test("Integer") {
			filter {
				User::age eq 5
			} shouldBeBson """
				{
					"age": {
						"$eq": 5
					}
				}
			""".trimIndent()
		}

		test("Null") {
			filter {
				User::age eq null
			} shouldBeBson """
				{
					"age": {
						"$eq": null
					}
				}
			""".trimIndent()
		}
	}

	context("Operator $isOneOf") {
		test("With 0 elements") {
			filter {
				User::name.isOneOf()
			} shouldBeBson """
				{
					"name": {
						"$isOneOf": [
						]
					}
				}
			""".trimIndent()
		}

		test("With 1 element") {
			filter {
				User::name.isOneOf("Alfred")
			} shouldBeBson """
				{
					"name": {
						"$isOneOf": [
							"Alfred"
						]
					}
				}
			""".trimIndent()
		}

		test("With multiple elements") {
			filter {
				User::name.isOneOf("Alfred", "Arthur", "Annabelle")
			} shouldBeBson """
				{
					"name": {
						"$isOneOf": [
							"Alfred",
							"Arthur",
							"Annabelle"
						]
					}
				}
			""".trimIndent()
		}
	}

	context("Operator $exists") {
		test("Exists") {
			filter {
				User::age.exists()
			} shouldBeBson """
				{
					"age": {
						"$exists": true
					}
				}
			""".trimIndent()
		}

		test("Does not exist") {
			filter {
				User::age.doesNotExist()
			} shouldBeBson """
				{
					"age": {
						"$exists": false
					}
				}
			""".trimIndent()
		}
	}

	context("Operator $type") {
		test("String") {
			filter {
				User::age hasType BsonType.STRING
			} shouldBeBson """
				{
					"age": {
						"$type": 2
					}
				}
			""".trimIndent()
		}

		test("Null") {
			filter {
				User::age hasType BsonType.NULL
			} shouldBeBson """
				{
					"age": {
						"$type": 10
					}
				}
			""".trimIndent()
		}

		test("Is null") {
			filter {
				User::name.isNull()
			} shouldBeBson """
				{
					"name": {
						"$type": 10
					}
				}
			""".trimIndent()
		}

		test("Is undefined") {
			filter {
				User::name.isUndefined()
			} shouldBeBson """
				{
					"name": {
						"$type": 6
					}
				}
			""".trimIndent()
		}

		test("Is not null") {
			filter {
				User::name.isNotNull()
			} shouldBeBson """
				{
					"name": {
						"$not": {
							"$type": 10
						}
					}
				}
			""".trimIndent()
		}

		test("Is not undefined") {
			filter {
				User::name.isNotUndefined()
			} shouldBeBson """
				{
					"name": {
						"$not": {
							"$type": 6
						}
					}
				}
			""".trimIndent()
		}
	}

	context("Operators $and and $or") {
		test("And") {
			filter {
				and {
					User::name eq "foo"
					User::age eq null
				}
			} shouldBeBson """
				{
					"$and": [
						{
							"name": {
								"$eq": "foo"
							}
						},
						{
							"age": {
								"$eq": null
							}
						}
					]
				}
			""".trimIndent()
		}

		test("Empty $and") {
			filter<User> {
				and {}
			} shouldBeBson """
				{
				}
			""".trimIndent()
		}

		test("An $and with a single term is removed") {
			filter {
				and {
					User::name eq "foo"
				}
			} shouldBeBson """
				{
					"name": {
						"$eq": "foo"
					}
				}
			""".trimIndent()
		}

		test("Combine nested $and") {
			filter {
				and {
					User::name eq "foo"
					and {
						User::age eq 12
						User::id eq "abc"
					}
				}
			} shouldBeBson """
				{
					"$and": [
						{
							"name": {
								"$eq": "foo"
							}
						},
						{
							"age": {
								"$eq": 12
							}
						},
						{
							"id": {
								"$eq": "abc"
							}
						}
					]
				}
			""".trimIndent()
		}

		test("An automatic $and is generated when multiple filters are given") {
			filter { // same example as the previous, but we didn't write the '$and'
				User::name eq "foo"
				User::age eq null
			} shouldBeBson """
				{
					"$and": [
						{
							"name": {
								"$eq": "foo"
							}
						},
						{
							"age": {
								"$eq": null
							}
						}
					]
				}
			""".trimIndent()
		}

		test("Or") {
			filter {
				or {
					User::name eq "foo"
					User::age eq null
				}
			} shouldBeBson """
				{
					"$or": [
						{
							"name": {
								"$eq": "foo"
							}
						},
						{
							"age": {
								"$eq": null
							}
						}
					]
				}
			""".trimIndent()
		}

		test("Empty $or") {
			filter<User> {
				or {}
			} shouldBeBson """
				{
				}
			""".trimIndent()
		}

		test("An $or with a single term is removed") {
			filter {
				or {
					User::name eq "foo"
				}
			} shouldBeBson """
				{
					"name": {
						"$eq": "foo"
					}
				}
			""".trimIndent()
		}
	}

	context("Comparison operators") {
		val gt = "\$gt"
		val gte = "\$gte"
		val lt = "\$lt"
		val lte = "\$lte"

		test("int $gt") {
			filter {
				User::age gt 12
			} shouldBeBson """
				{
					"age": {
						"$gt": 12
					}
				}
			""".trimIndent()
		}

		test("int $gte") {
			filter {
				User::age gte 12
			} shouldBeBson """
				{
					"age": {
						"$gte": 12
					}
				}
			""".trimIndent()
		}

		test("int $lt") {
			filter {
				User::age lt 12
			} shouldBeBson """
				{
					"age": {
						"$lt": 12
					}
				}
			""".trimIndent()
		}

		test("int $lte") {
			filter {
				User::age lte 12
			} shouldBeBson """
				{
					"age": {
						"$lte": 12
					}
				}
			""".trimIndent()
		}
	}
})
