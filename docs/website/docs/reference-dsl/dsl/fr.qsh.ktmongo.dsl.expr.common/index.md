//[dsl](../../index.md)/[fr.qsh.ktmongo.dsl.expr.common](index.md)

# Package-level declarations

## Types

| Name                                                | Summary                                                                                                                                                                                                                                 |
|-----------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [CompoundExpression](-compound-expression/index.md) | [jvm]<br>abstract class [CompoundExpression](-compound-expression/index.md)(codec: CodecRegistry) : [Expression](-expression/index.md)<br>A compound node in the BSON AST. This class is an implementation detail of all operator DSLs. |
| [Expression](-expression/index.md)                  | [jvm]<br>abstract class [Expression](-expression/index.md)(codec: CodecRegistry)<br>A node in the BSON AST.                                                                                                                             |

## Functions

| Name                                        | Summary                                                                                                                                                                                                                                                                                                                                                                                                       |
|---------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [acceptAll](accept-all.md)                  | [jvm]<br>@[LowLevelApi](../fr.qsh.ktmongo.dsl/-low-level-api/index.md)<br>fun [CompoundExpression](-compound-expression/index.md).[acceptAll](accept-all.md)(expressions: [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)&lt;[Expression](-expression/index.md)&gt;)<br>Binds any arbitrary [expressions](accept-all.md) as sub-expressions of the receiver. |
| [withLoggedContext](with-logged-context.md) | [jvm]<br>fun BsonWriter.[withLoggedContext](with-logged-context.md)(storeLogs: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true): BsonWriter                                                                                                                                                                                                                         |
