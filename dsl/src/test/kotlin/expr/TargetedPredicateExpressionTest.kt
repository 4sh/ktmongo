package fr.qsh.ktmongo.dsl.expr

import io.kotest.core.spec.style.FunSpec

@Suppress("unused")
class TargetedPredicateExpressionTest : FunSpec({

	fun <T> targetedPredicate(block: TargetedPredicateExpression<T>.() -> Unit): String =
		buildExpression(::TargetedPredicateExpression, block)

	val eq = "\$eq"

	context("Operator \$eq") {
		test("Integer") {
			targetedPredicate {
				eq(4)
			} shouldBeBson """
				{
					"$eq": 4
				}
			""".trimIndent()
		}

		test("String") {
			targetedPredicate {
				eq("foo")
			} shouldBeBson """
				{
					"$eq": "foo"
				}
			""".trimIndent()
		}

		test("Null") {
			targetedPredicate {
				eq(null)
			} shouldBeBson """
				{
					"$eq": null
				}
			""".trimIndent()
		}
	}

})
