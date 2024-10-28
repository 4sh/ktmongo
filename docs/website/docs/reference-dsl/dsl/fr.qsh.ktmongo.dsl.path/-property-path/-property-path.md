//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.path](../index.md)/[PropertyPath](index.md)/[PropertyPath](-property-path.md)

# PropertyPath

[jvm]\
constructor(path: [Path](../-path/index.md), backingProperty: [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;*, *&gt;)

#### Parameters

jvm

|            |                                                                                                                                                                                                                                                               |
|------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| RootParent | The type at the root of this path. Normally, [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html) instances refer to the direct parent. Instead, this class refers to the very first type in the property chain. |
| Value      | The type of the property pointed to by this instance.                                                                                                                                                                                                         |
