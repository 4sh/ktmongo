package fr.qsh.ktmongo.dsl.expr

import io.kotest.core.spec.style.FunSpec

@Suppress("unused")
class PredicateExpressionTest : FunSpec({

	fun <T> predicate(block: PredicateExpression<T>.() -> Unit): String =
		PredicateExpression<T>(testCodec()).apply(block).toString(simplified = true)

	val eq = "\$eq"

	context("Operator \$eq") {
		test("Integer") {
			predicate {
				eq(4)
			} shouldBeBson """
				{
					"$eq": 4
				}
			""".trimIndent()
		}

		test("String") {
			predicate {
				eq("foo")
			} shouldBeBson """
				{
					"$eq": "foo"
				}
			""".trimIndent()
		}

		test("Null") {
			predicate {
				eq(null)
			} shouldBeBson """
				{
					"$eq": null
				}
			""".trimIndent()
		}
	}

})
