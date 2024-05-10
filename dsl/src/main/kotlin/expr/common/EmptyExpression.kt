package fr.qsh.ktmongo.dsl.expr.common

import fr.qsh.ktmongo.dsl.LowLevelApi
import org.bson.AbstractBsonWriter
import org.bson.codecs.configuration.CodecRegistry

private class EmptyExpression(codec: CodecRegistry) : Expression(codec) {
	@LowLevelApi
	override fun write(writer: AbstractBsonWriter) {}
}

fun Expression.Companion.empty(codec: CodecRegistry): Expression =
	EmptyExpression(codec)
