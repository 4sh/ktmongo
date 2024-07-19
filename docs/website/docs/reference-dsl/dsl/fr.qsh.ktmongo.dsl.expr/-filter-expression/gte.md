//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[gte](gte.md)

# gte

[jvm]\
infix fun &lt;[V](gte.md)&gt; [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), [V](gte.md)&gt;.[gte](gte.md)(value: [V](gte.md))

Selects documents for which this field has a value greater or equal to [value](gte.md).

### Example

```kotlin
class User(
    val name: String,
    val age: Int?,
)

collection.find {
    User::age gte 18
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/gte/)

#### See also

|                                                |
|------------------------------------------------|
| [FilterExpression.gteNotNull](gte-not-null.md) |
