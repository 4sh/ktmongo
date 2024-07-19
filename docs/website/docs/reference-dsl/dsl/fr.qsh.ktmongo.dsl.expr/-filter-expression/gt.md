//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[gt](gt.md)

# gt

[jvm]\
infix fun &lt;[V](gt.md)&gt; [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), [V](gt.md)&gt;.[gt](gt.md)(value: [V](gt.md))

Selects documents for which this field has a value strictly greater than [value](gt.md).

### Example

```kotlin
class User(
    val name: String,
    val age: Int?,
)

collection.find {
    User::age gt 18
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/gt/)

#### See also

|                                              |
|----------------------------------------------|
| [FilterExpression.gtNotNull](gt-not-null.md) |
