//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[lt](lt.md)

# lt

[jvm]\
infix fun &lt;[V](lt.md)&gt; [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), [V](lt.md)&gt;.[lt](lt.md)(value: [V](lt.md))

Selects documents for which this field has a value strictly lesser than [value](lt.md).

### Example

```kotlin
class User(
    val name: String,
    val age: Int?,
)

collection.find {
    User::age lt 18
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/lt/)

#### See also

|                                              |
|----------------------------------------------|
| [FilterExpression.ltNotNull](lt-not-null.md) |
