//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr.common](../index.md)/[Expression](index.md)

# Expression

abstract class [Expression](index.md)(codec: CodecRegistry)

A node in the BSON AST.

Each node knows how to [writeTo](write-to.md) itself into the expression.

### Security

Implementing this interface allows to inject arbitrary BSON into a request. Be very careful not to allow request injections.

### Debugging notes

Use [toString](to-string.md) to generate the JSON of this expression.

#### Inheritors

|                                                        |
|--------------------------------------------------------|
| [CompoundExpression](../-compound-expression/index.md) |

## Constructors

|                              |                                            |
|------------------------------|--------------------------------------------|
| [Expression](-expression.md) | [jvm]<br>constructor(codec: CodecRegistry) |

## Types

| Name                             | Summary                                          |
|----------------------------------|--------------------------------------------------|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name                     | Summary                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
|--------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [freeze](freeze.md)      | [jvm]<br>@[LowLevelApi](../../fr.qsh.ktmongo.dsl/-low-level-api/index.md)<br>fun [freeze](freeze.md)()<br>Forbid further mutations to this expression.                                                                                                                                                                                                                                                                                                                                              |
| [simplify](simplify.md)  | [jvm]<br>@[LowLevelApi](../../fr.qsh.ktmongo.dsl/-low-level-api/index.md)<br>open fun [simplify](simplify.md)(): [Expression](index.md)?<br>Allows the implementation to replace itself by another more appropriate representation.                                                                                                                                                                                                                                                                 |
| [toString](to-string.md) | [jvm]<br>override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Returns a JSON representation of this node, generated using [writeTo](write-to.md).<br>[jvm]<br>fun [toString](to-string.md)(simplified: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Returns a JSON representation of this node. |
| [writeTo](write-to.md)   | [jvm]<br>@[LowLevelApi](../../fr.qsh.ktmongo.dsl/-low-level-api/index.md)<br>fun [writeTo](write-to.md)(writer: BsonWriter)<br>Writes this expression into a [writer](write-to.md).                                                                                                                                                                                                                                                                                                                 |
