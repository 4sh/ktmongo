package fr.qsh.ktmongo.dsl.expr

import fr.qsh.ktmongo.dsl.LowLevelApi
import fr.qsh.ktmongo.dsl.buildDocument
import org.bson.BsonDocumentWriter
import org.bson.codecs.Encoder
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry

/**
 * DSL for MongoDB operators that are used as predicates in conditions in a context where the targeted field is already
 * specified.
 */
@OptIn(LowLevelApi::class)
class TargetedPredicateExpression<T>(
	@property:LowLevelApi
	@PublishedApi
	internal val writer: BsonDocumentWriter,

	@PublishedApi
	internal val codec: CodecRegistry,
) {

	/**
	 * Matches documents where the value of a field equals the [value].
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * class User(
	 *     val name: String?,
	 *     val age: Int,
	 * )
	 *
	 * collection.find {
	 *     User::name {
	 *         eq("foo")
	 *     }
	 * }
	 * ```
	 *
	 * ### External resources
	 *
	 * - [Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/eq/)
	 */
	fun eq(value: T) {
		writer.buildDocument("\$eq") {
			if (value == null) {
				writer.writeNull()
			} else {
				@Suppress("UNNECESSARY_NOT_NULL_ASSERTION", "UNCHECKED_CAST") // Kotlin doesn't smart-cast here, but should, this is safe
				(codec.get(value!!::class.java) as Encoder<T>)
					.encode(writer, value, EncoderContext.builder().build())
			}
		}
	}
}
