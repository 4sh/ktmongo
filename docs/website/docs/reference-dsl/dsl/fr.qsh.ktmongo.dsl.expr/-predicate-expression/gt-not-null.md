//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[PredicateExpression](index.md)/[gtNotNull](gt-not-null.md)

# gtNotNull

[jvm]\
fun [gtNotNull](gt-not-null.md)(value: [T](index.md)?)

Selects documents for which this field has a value strictly greater than [value](gt-not-null.md).

If [value](gt-not-null.md) is `null`, the operator is not added (all elements are matched).

### Example

```kotlin
class User(
    val name: String,
    val age: Int?
)

collection.find {
    User::age { gtNotNull(18) }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/gt/)

#### See also

|                                                                    |
|--------------------------------------------------------------------|
| [FilterExpression.gtNotNull](../-filter-expression/gt-not-null.md) |
| [PredicateExpression.eqNotNull](eq-not-null.md)                    | Learn more about the 'notNull' variants |
