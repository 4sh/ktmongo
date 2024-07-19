//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr.common](../index.md)/[CompoundExpression](index.md)

# CompoundExpression

abstract class [CompoundExpression](index.md)(codec: CodecRegistry) : [Expression](../-expression/index.md)

A compound node in the BSON AST. This class is an implementation detail of all operator DSLs.

This class adds the method [accept](accept.md) which allows binding a child expression into the current one. It manages the bound expressions internally, only giving the implementations access to them when simplify or write are called.

#### See also

|                                       |
|---------------------------------------|
| [Expression](../-expression/index.md) |

#### Inheritors

|                                                                                     |
|-------------------------------------------------------------------------------------|
| [FilterExpression](../../fr.qsh.ktmongo.dsl.expr/-filter-expression/index.md)       |
| [PredicateExpression](../../fr.qsh.ktmongo.dsl.expr/-predicate-expression/index.md) |

## Constructors

|                                               |                                            |
|-----------------------------------------------|--------------------------------------------|
| [CompoundExpression](-compound-expression.md) | [jvm]<br>constructor(codec: CodecRegistry) |

## Types

| Name                             | Summary                                          |
|----------------------------------|--------------------------------------------------|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name                                    | Summary                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
|-----------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [accept](accept.md)                     | [jvm]<br>@[LowLevelApi](../../fr.qsh.ktmongo.dsl/-low-level-api/index.md)<br>fun [accept](accept.md)(expression: [Expression](../-expression/index.md))<br>Binds an arbitrary [expression](accept.md) as a sub-expression of the receiver.                                                                                                                                                                                                                                                                                                       |
| [acceptAll](../accept-all.md)           | [jvm]<br>@[LowLevelApi](../../fr.qsh.ktmongo.dsl/-low-level-api/index.md)<br>fun [CompoundExpression](index.md).[acceptAll](../accept-all.md)(expressions: [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)&lt;[Expression](../-expression/index.md)&gt;)<br>Binds any arbitrary [expressions](../accept-all.md) as sub-expressions of the receiver.                                                                                                                                             |
| [freeze](../-expression/freeze.md)      | [jvm]<br>@[LowLevelApi](../../fr.qsh.ktmongo.dsl/-low-level-api/index.md)<br>fun [freeze](../-expression/freeze.md)()<br>Forbid further mutations to this expression.                                                                                                                                                                                                                                                                                                                                                                            |
| [simplify](simplify.md)                 | [jvm]<br>@[LowLevelApi](../../fr.qsh.ktmongo.dsl/-low-level-api/index.md)<br>override fun [simplify](simplify.md)(): [Expression](../-expression/index.md)?<br>Allows the implementation to replace itself by another more appropriate representation.                                                                                                                                                                                                                                                                                           |
| [toString](../-expression/to-string.md) | [jvm]<br>override fun [toString](../-expression/to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Returns a JSON representation of this node, generated using [writeTo](../-expression/write-to.md).<br>[jvm]<br>fun [toString](../-expression/to-string.md)(simplified: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Returns a JSON representation of this node. |
| [writeTo](../-expression/write-to.md)   | [jvm]<br>@[LowLevelApi](../../fr.qsh.ktmongo.dsl/-low-level-api/index.md)<br>fun [writeTo](../-expression/write-to.md)(writer: BsonWriter)<br>Writes this expression into a [writer](../-expression/write-to.md).                                                                                                                                                                                                                                                                                                                                |
