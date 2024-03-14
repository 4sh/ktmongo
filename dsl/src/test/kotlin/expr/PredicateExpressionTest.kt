package fr.qsh.ktmongo.dsl.expr

import io.kotest.core.spec.style.FunSpec
import org.bson.BsonType

@Suppress("unused")
class PredicateExpressionTest : FunSpec({

	class User(
		val id: String,
		val name: String,
		val age: Int?,
	)

	fun <T> predicate(block: PredicateExpression<T>.() -> Unit): String =
		buildExpression(::PredicateExpression, block)

	val eq = "\$eq"
	val and = "\$and"
	val or = "\$or"
	val exists = "\$exists"
	val type = "\$type"
	val not = "\$not"

	context("Operator $eq") {
		test("Integer") {
			predicate {
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
			predicate {
				User::name eq null
			} shouldBeBson """
				{
					"name": {
						"$eq": null
					}
				}
			""".trimIndent()
		}
	}

	context("Operator $exists") {
		test("Exists") {
			predicate {
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
			predicate {
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
			predicate {
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
			predicate {
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
			predicate {
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
			predicate {
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
			predicate {
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
			predicate {
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
			predicate {
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

		test("Or") {
			predicate {
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
	}
})
