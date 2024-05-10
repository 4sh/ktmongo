package fr.qsh.ktmongo.dsl.expr

import fr.qsh.ktmongo.dsl.KtMongoDsl
import fr.qsh.ktmongo.dsl.LowLevelApi
import org.bson.BsonDocument
import org.bson.BsonDocumentWriter
import org.bson.codecs.configuration.CodecRegistry

/**
 * A compound node in the BSON AST.
 *
 * Compared to regular [ExpressionNode], `Expression` adds [accept], which allows to inject sub-expressions
 * into the current expression.
 *
 * Subclasses of this interface provide DSLs to create BSON expressions.
 *
 * ### Implementation notes
 *
 * Much like [ExpressionNode], implementations of this interface **must** implement [toString] to print the generated
 * BSON used by the request. We recommend implementing [AbstractExpression], which does this automatically.
 */
@OptIn(LowLevelApi::class)
@KtMongoDsl
interface Expression : ExpressionNode {

	/**
	 * Adds an arbitrary [node] to this expression.
	 *
	 * ### Security and correctness
	 *
	 * This function makes no verification on the validity of the passed node.
	 * It is added to this expression as-is.
	 *
	 * This function is only publicly available to allow users to add missing operators themselves, by implementing
	 * [ExpressionNode]. Only implement operators yourself if you are sure of what you are doing!
	 */
	@LowLevelApi
	@KtMongoDsl
	fun accept(node: ExpressionNode)
}

@LowLevelApi
@KtMongoDsl
fun Expression.acceptAll(nodes: Iterable<ExpressionNode>) {
	for (node in nodes)
		accept(node)
}

/**
 * A node in the BSON AST.
 *
 * Each node knows how to [write] itself into the expression.
 *
 * ### Security
 *
 * Implementing this interface allows to inject arbitrary BSON into a request.
 * Be very careful not to allow request injections.
 *
 * ### Implementation notes
 *
 * To facilitate debugging, **all** implementations of [ExpressionNode] must implement [toString] to print the
 * predicted BSON used by the request.
 * We recommend implementing [AbstractExpressionNode], which does this automatically.
 */
@LowLevelApi
interface ExpressionNode {

	/**
	 * Writes the current node into the BSON AST, represented by the [writer].
	 */
	@LowLevelApi
	fun write(writer: BsonDocumentWriter, codec: CodecRegistry)

	/**
	 * Executes simplification against the current node.
	 *
	 * By default, no simplifications are executed and this object is returned as-is.
	 */
	@LowLevelApi
	fun simplify(codec: CodecRegistry): ExpressionNode = this

	@LowLevelApi
	fun simplifyAndWrite(writer: BsonDocumentWriter, codec: CodecRegistry) =
		simplify(codec).write(writer, codec)

	object EmptyExpressionNode : ExpressionNode {
		override fun write(writer: BsonDocumentWriter, codec: CodecRegistry) {}
	}
}

/**
 * Helper to implement [Expression] that handles [toString] generation and the implementation of [accept].
 */
@OptIn(LowLevelApi::class)
abstract class AbstractExpression(
	codec: CodecRegistry,
) : AbstractExpressionNode(codec), Expression {

	@OptIn(LowLevelApi::class)
	private val children = ArrayList<ExpressionNode>()

	@LowLevelApi
	override fun accept(node: ExpressionNode) {
		children += node
	}

	@LowLevelApi
	protected open fun simplify(codec: CodecRegistry, children: List<ExpressionNode>): ExpressionNode = this

	final override fun simplify(codec: CodecRegistry): ExpressionNode =
		simplify(codec, children)

	@LowLevelApi
	override fun write(writer: BsonDocumentWriter, codec: CodecRegistry) {
		for (child in children) {
			child.write(writer, codec)
		}
	}
}

/**
 * Helper to implement [ExpressionNode] that handles [toString] generation.
 */
@LowLevelApi
abstract class AbstractExpressionNode(
	private val codec: CodecRegistry,
) : ExpressionNode {

	override fun toString(): String {
		val document = BsonDocument()

		@OptIn(LowLevelApi::class)
		BsonDocumentWriter(document).use {
			simplifyAndWrite(it, codec)
		}

		return document.toJson()
	}
}
