package fr.qsh.ktmongo.dsl.expr

import io.kotest.matchers.shouldBe
import org.bson.BsonDocument
import org.bson.BsonDocumentWriter
import org.bson.codecs.*
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.jsr310.InstantCodec
import org.bson.codecs.jsr310.LocalDateCodec
import org.bson.codecs.jsr310.LocalDateTimeCodec
import org.bson.codecs.jsr310.LocalTimeCodec

fun <T> buildExpression(dsl: (BsonDocumentWriter, CodecRegistry) -> T, block: T.() -> Unit): String {
	val document = BsonDocument()

	val registry = CodecRegistries.fromCodecs(
		AtomicBooleanCodec(),
		AtomicIntegerCodec(),
		AtomicLongCodec(),
		BigDecimalCodec(),
		BinaryCodec(),
		BooleanCodec(),
		BsonArrayCodec(),
		BsonBinaryCodec(),
		BsonBooleanCodec(),
		BsonDateTimeCodec(),
		BsonDBPointerCodec(),
		BsonDecimal128Codec(),
		BsonDocumentCodec(),
		BsonDoubleCodec(),
		BsonInt32Codec(),
		BsonInt64Codec(),
		BsonJavaScriptCodec(),
		BsonMaxKeyCodec(),
		BsonMinKeyCodec(),
		BsonNullCodec(),
		BsonObjectIdCodec(),
		BsonRegularExpressionCodec(),
		BsonStringCodec(),
		BsonSymbolCodec(),
		BsonTimestampCodec(),
		BsonUndefinedCodec(),
		BsonValueCodec(),
		ByteArrayCodec(),
		ByteCodec(),
		CharacterCodec(),
		CodeCodec(),
		DateCodec(),
		Decimal128Codec(),
		DocumentCodec(),
		DoubleCodec(),
		FloatCodec(),
		InstantCodec(),
		IntegerCodec(),
		JsonObjectCodec(),
		LocalDateCodec(),
		LocalDateTimeCodec(),
		LocalTimeCodec(),
		LongCodec(),
		MaxKeyCodec(),
		MinKeyCodec(),
		ObjectIdCodec(),
		OverridableUuidRepresentationUuidCodec(),
		PatternCodec(),
		RawBsonDocumentCodec(),
		ShortCodec(),
		StringCodec(),
		SymbolCodec(),
		UuidCodec(),
	)

	dsl(BsonDocumentWriter(document), registry).apply(block)

	return document.toJson()
}

infix fun String.shouldBeBson(expected: String) {
	this shouldBe expected
		.replace("\n", "")
		.replace("\t", "")
		.replace(",", ", ")
}
