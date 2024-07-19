//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[lte](lte.md)

# lte

[jvm]\
infix fun &lt;[V](lte.md)&gt; [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), [V](lte.md)&gt;.[lte](lte.md)(value: [V](lte.md))

Selects documents for which this field has a value lesser or equal to [value](lte.md).

### Example

```kotlin
class User(
    val name: String,
    val age: Int?,
)

collection.find {
    User::age lte 18
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/lte/)

#### See also

|                                                |
|------------------------------------------------|
| [FilterExpression.lteNotNull](lte-not-null.md) |
