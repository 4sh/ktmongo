package fr.qsh.ktmongo.dsl.expr

import io.kotest.core.spec.style.FunSpec

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
