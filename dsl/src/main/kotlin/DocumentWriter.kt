package fr.qsh.ktmongo.dsl

import org.bson.AbstractBsonWriter

/**
 * Helper to start a document, ensuring it is closed.
 *
 * If [name] is not null, it is set as the document name.
 */
@LowLevelApi
@PublishedApi
internal inline fun AbstractBsonWriter.buildDocument(name: String? = null, block: () -> Unit) {
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
internal inline fun AbstractBsonWriter.buildArray(block: () -> Unit) {
	try {
		writeStartArray()
		block()
	} finally {
		writeEndArray()
	}
}
