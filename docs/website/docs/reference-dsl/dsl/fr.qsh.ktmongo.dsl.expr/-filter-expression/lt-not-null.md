//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[ltNotNull](lt-not-null.md)

# ltNotNull

[jvm]\
infix fun &lt;[V](lt-not-null.md)&gt; [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), [V](lt-not-null.md)&gt;.[ltNotNull](lt-not-null.md)(value: [V](lt-not-null.md)?)

Selects documents for which this field has a value strictly lesser than [value](lt-not-null.md).

If [value](lt-not-null.md) is `null`, the operator is not added (all elements are matched).

### Example

```kotlin
class User(
    val name: String,
    val age: Int?
)

collection.find {
    User::age ltNotNull 10
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/lt/)

#### See also

|                                              |
|----------------------------------------------|
| [FilterExpression.lt](lt.md)                 |
| [FilterExpression.eqNotNull](eq-not-null.md) | Learn more about the 'notNull' variants |
