package fr.qsh.ktmongo.dsl.expr.common

import fr.qsh.ktmongo.dsl.KtMongoDsl
import fr.qsh.ktmongo.dsl.LowLevelApi
import org.bson.BsonWriter
import org.bson.codecs.configuration.CodecRegistry
import java.util.*

/**
 * A compound node in the BSON AST.
 * This class is an implementation detail of all operator DSLs.
 *
 * This class adds the method [accept] which allows binding a child expression
 * into the current one.
 * It manages the bound expressions internally, only giving the implementations
 * access to them when [simplify] or [write] are called.
 *
 * @see Expression
 */
abstract class CompoundExpression(
	codec: CodecRegistry,
) : Expression(codec) {

	// region Sub-expression binding

	private val _children = ArrayList<Expression>()

	@LowLevelApi
	val children: List<Expression>
		get() = Collections.unmodifiableList(_children)

	/**
	 * Binds an arbitrary [expression] as a sub-expression of the receiver.
	 *
	 * ### Security and correctness
	 *
	 * This function makes no verification on the validity of the passed expression.
	 * It is added to this expression as-is.
	 *
	 * This function is only publicly available to allow users to add missing operators themselves
	 * by implementing [Expression] for their operator.
	 *
	 * **An incorrectly written expression may allow arbitrary code execution on the database,
	 * data corruption, or data leaks. Only call this function on expressions you are sure
	 * are implemented correctly!**
	 */
	@LowLevelApi
	@KtMongoDsl
	fun accept(expression: Expression) {
		require(!frozen) { "This expression has already been frozen, it cannot accept the child expression $expression" }

		val simplifiedExpression = expression.simplify()

		if (simplifiedExpression != null) {
			_children += simplifiedExpression
				.also { it.freeze() }
		}
	}

	// endregion
	// region Simplifications

	/**
	 * See [Expression.simplify].
	 *
	 * @param children The list of expressions that have been [bound][accept] into this
	 * expression.
	 * **These children have already been simplified.**
	 */
	@LowLevelApi
	protected open fun simplify(children: List<Expression>): Expression =
		this

	@LowLevelApi
	final override fun simplify(): Expression =
		simplify(children)

	// endregion
	// region Writing

	/**
	 * See [Expression.write].
	 *
	 * @param children The list of expressions that have been [bound][accept] into this
	 * expression.
	 */
	@LowLevelApi
	protected open fun write(writer: BsonWriter, children: List<Expression>) {
		for (child in children) {
			require(this !== child) { "Trying to write myself as my own child!" }
			child.writeTo(writer)
		}
	}

	@LowLevelApi
	final override fun write(writer: BsonWriter) {
		write(writer, children)
	}

	// endregion

	companion object
}

/**
 * Binds any arbitrary [expressions] as sub-expressions of the receiver.
 *
 * To learn more about the security implications, see [CompoundExpression.accept].
 */
@LowLevelApi
@KtMongoDsl
fun CompoundExpression.acceptAll(expressions: Iterable<Expression>) {
	for (child in expressions)
		accept(child)
}
