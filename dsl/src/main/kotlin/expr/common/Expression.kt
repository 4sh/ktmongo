package fr.qsh.ktmongo.dsl.expr.common

import fr.qsh.ktmongo.dsl.LowLevelApi
import org.bson.BsonDocument
import org.bson.BsonDocumentWriter
import org.bson.BsonWriter
import org.bson.codecs.configuration.CodecRegistry

/**
 * A node in the BSON AST.
 *
 * Each node knows how to [writeTo] itself into the expression.
 *
 * ### Security
 *
 * Implementing this interface allows to inject arbitrary BSON into a request.
 * Be very careful not to allow request injections.
 *
 * ### Debugging notes
 *
 * Use [toString] to generate the JSON of this expression.
 */
abstract class Expression(
	protected val codec: CodecRegistry,
) {

	/**
	 * Writes this expression into [writer] **exactly as it is described**.
	 *
	 * This function is not allowed to edit the expression in any way,
	 * in particular, simplifying it is not allowed.
	 * To modify the expression before writing it, implement [simplify].
	 *
	 * **Implementations must be pure.**
	 */
	@LowLevelApi
	protected abstract fun write(writer: BsonWriter)

	/**
	 * Allows the implementation to replace itself by another more appropriate representation.
	 *
	 * For example, if the current node is an `$and` operator with a single child,
	 * it may use this function to replace itself by that child.
	 *
	 * **Implementations must be pure.**
	 */
	@LowLevelApi
	protected open fun simplify(): Expression = this

	/**
	 * Writes this expression into a [writer].
	 *
	 * This function is guaranteed to be pure.
	 */
	@LowLevelApi
	fun writeTo(writer: BsonWriter) {
		this.simplify().write(writer)
	}

	/**
	 * Returns a JSON representation of this node.
	 *
	 * If [simplified] is `true`, [simplifications][simplify] are executed before printing.
	 */
	fun toString(simplified: Boolean): String {
		val document = BsonDocument()

		val writer = BsonDocumentWriter(document)
			.withLoggedContext()

		@OptIn(LowLevelApi::class)
		if (simplified)
			writeTo(writer)
		else
			write(writer)

		return document.toString()
	}

	/**
	 * Returns a JSON representation of this node, generated using [writeTo].
	 */
	final override fun toString(): String =
		toString(simplified = true)

	companion object
}
