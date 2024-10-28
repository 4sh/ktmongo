//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[gtNotNull](gt-not-null.md)

# gtNotNull

[jvm]\
infix fun &lt;[V](gt-not-null.md)&gt; [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), [V](gt-not-null.md)&gt;.[gtNotNull](gt-not-null.md)(value: [V](gt-not-null.md)?)

Selects documents for which this field has a value strictly greater than [value](gt-not-null.md).

If [value](gt-not-null.md) is `null`, the operator is not added (all elements are matched).

### Example

```kotlin
class User(
    val name: String,
    val age: Int?
)

collection.find {
    User::age gtNotNull 10
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/gt/)

#### See also

|                                              |
|----------------------------------------------|
| [FilterExpression.gt](gt.md)                 |
| [FilterExpression.eqNotNull](eq-not-null.md) | Learn more about the 'notNull' variants |
