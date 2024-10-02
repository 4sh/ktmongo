//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.path](../index.md)/[Path](index.md)

# Path

[jvm]\
@[LowLevelApi](../../fr.qsh.ktmongo.dsl/-low-level-api/index.md)

data class [Path](index.md)(val segment: [PathSegment](../-path-segment/index.md), val parent: [Path](index.md)?)

A path is a string pointer that identifies which field(s) are impacted by an operator.

For example, the following are valid paths:

-
`"foo"`: targets the field &quot;foo&quot;,
-
`"foo.bar"`: targets the field &quot;bar&quot; which is part of the object &quot;foo&quot;,
-
`"arr.$5.bar"`: targets the field &quot;bar&quot; which is part of the item with index 5 in the array &quot;arr&quot;.

This structure is a singly-linked list representing the entire path. Each segment is represented by [PathSegment](../-path-segment/index.md).

## Constructors

|                  |                                                                                                    |
|------------------|----------------------------------------------------------------------------------------------------|
| [Path](-path.md) | [jvm]<br>constructor(segment: [PathSegment](../-path-segment/index.md), parent: [Path](index.md)?) |

## Types

| Name                             | Summary                                          |
|----------------------------------|--------------------------------------------------|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Properties

| Name                  | Summary                                                                      |
|-----------------------|------------------------------------------------------------------------------|
| [parent](parent.md)   | [jvm]<br>val [parent](parent.md): [Path](index.md)?                          |
| [segment](segment.md) | [jvm]<br>val [segment](segment.md): [PathSegment](../-path-segment/index.md) |

## Functions

| Name                         | Summary                                                                                                                                                                                            |
|------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [asSequence](as-sequence.md) | [jvm]<br>fun [asSequence](as-sequence.md)(): [Sequence](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/-sequence/index.html)&lt;[PathSegment](../-path-segment/index.md)&gt;        |
| [plus](../plus.md)           | [jvm]<br>@[LowLevelApi](../../fr.qsh.ktmongo.dsl/-low-level-api/index.md)<br>operator fun [Path](index.md).[plus](../plus.md)(segment: [PathSegment](../-path-segment/index.md)): [Path](index.md) |
| [toString](to-string.md)     | [jvm]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)                                                            |
