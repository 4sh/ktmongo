package fr.qsh.ktmongo.dsl

import org.bson.BsonDocumentWriter

/**
 * Helper to start a document, ensuring it is closed.
 *
 * If [name] is not null, it is set as the document name.
 */
@LowLevelApi
@PublishedApi
internal inline fun BsonDocumentWriter.buildDocument(name: String? = null, block: () -> Unit) {
	try {
		writeStartDocument()
		name?.let(::writeName)
		block()
	} finally {
		writeEndDocument()
	}
}

/**
 * Helper to start an array, ensuring it is closed.
 */
@LowLevelApi
@PublishedApi
internal inline fun BsonDocumentWriter.buildArray(block: () -> Unit) {
	try {
		writeStartArray()
		block()
	} finally {
		writeEndArray()
	}
}
