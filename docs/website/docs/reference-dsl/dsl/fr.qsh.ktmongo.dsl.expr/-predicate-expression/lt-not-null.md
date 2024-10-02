//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[PredicateExpression](index.md)/[ltNotNull](lt-not-null.md)

# ltNotNull

[jvm]\
fun [ltNotNull](lt-not-null.md)(value: [T](index.md)?)

Selects documents for which this field has a value strictly lesser than [value](lt-not-null.md).

If [value](lt-not-null.md) is `null`, the operator is not added (all elements are matched).

### Example

```kotlin
class User(
    val name: String,
    val age: Int?
)

collection.find {
    User::age { ltNotNull(18) }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/lt/)

#### See also

|                                                                    |
|--------------------------------------------------------------------|
| [FilterExpression.ltNotNull](../-filter-expression/lt-not-null.md) |
| lqNotNull                                                          | Learn more about the 'notNull' variants |
