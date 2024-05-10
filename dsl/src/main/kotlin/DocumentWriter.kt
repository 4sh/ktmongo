package fr.qsh.ktmongo.dsl

import org.bson.BsonWriter

/**
 * Helper to start a document, ensuring it is closed.
 */
@LowLevelApi
@PublishedApi
internal inline fun BsonWriter.writeDocument(name: String, block: () -> Unit) {
	writeStartDocument(name)
	block()
	writeEndDocument()
}

/**
 * Helper to start a document, ensuring it is closed.
 */
@LowLevelApi
@PublishedApi
internal inline fun BsonWriter.writeDocument(block: () -> Unit) {
	writeStartDocument()
	block()
	writeEndDocument()
}

/**
 * Helper to start an array, ensuring it is closed.
 */
@LowLevelApi
@PublishedApi
internal inline fun BsonWriter.writeArray(block: () -> Unit) {
	writeStartArray()
	block()
	writeEndArray()
}
