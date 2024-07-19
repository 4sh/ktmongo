//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.path](../index.md)/[PathSegment](index.md)

# PathSegment

@[LowLevelApi](../../fr.qsh.ktmongo.dsl/-low-level-api/index.md)

sealed class [PathSegment](index.md)

Single segment in a [Path](../-path/index.md).

Each subclass represents a different type of segment that can appear in a path, and links to the high-level factory to obtain an instance of this path.

The high-level operators are only available in correct contexts to disambiguate multiple usages of the same operator. Subclasses of this type do not protect against these usages.

#### Inheritors

|                                           |
|-------------------------------------------|
| [Field](-field/index.md)                  |
| [Indexed](-indexed/index.md)              |
| [Positional](-positional/index.md)        |
| [AllPositional](-all-positional/index.md) |

## Types

| Name                                      | Summary                                                                                                                                                                                                                                                                                            |
|-------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [AllPositional](-all-positional/index.md) | [jvm]<br>@[LowLevelApi](../../fr.qsh.ktmongo.dsl/-low-level-api/index.md)<br>data object [AllPositional](-all-positional/index.md) : [PathSegment](index.md)<br>Path segment for the &quot;all positional&quot; operator (`.$[].`).                                                                |
| [Field](-field/index.md)                  | [jvm]<br>@[LowLevelApi](../../fr.qsh.ktmongo.dsl/-low-level-api/index.md)<br>data class [Field](-field/index.md)(val name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [PathSegment](index.md)<br>Path segment representing the name of a field.           |
| [Indexed](-indexed/index.md)              | [jvm]<br>@[LowLevelApi](../../fr.qsh.ktmongo.dsl/-low-level-api/index.md)<br>data class [Indexed](-indexed/index.md)(val index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [PathSegment](index.md)<br>Path segment representing an indexed element in an array. |
| [Positional](-positional/index.md)        | [jvm]<br>@[LowLevelApi](../../fr.qsh.ktmongo.dsl/-low-level-api/index.md)<br>data object [Positional](-positional/index.md) : [PathSegment](index.md)<br>Path segment for the &quot;positional&quot; operator (`.$.`).                                                                             |
