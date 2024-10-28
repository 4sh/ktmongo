//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[PredicateExpression](index.md)/[lteNotNull](lte-not-null.md)

# lteNotNull

[jvm]\
fun [lteNotNull](lte-not-null.md)(value: [T](index.md)?)

Selects documents for which this field has a value lesser or equal to [value](lte-not-null.md).

If [value](lte-not-null.md) is `null`, the operator is not added (all elements are matched).

### Example

```kotlin
class User(
    val name: String,
    val age: Int?
)

collection.find {
    User::age { lteNotNull(18) }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/lte/)

#### See also

|                                                                      |
|----------------------------------------------------------------------|
| [FilterExpression.lteNotNull](../-filter-expression/lte-not-null.md) |
| [PredicateExpression.eqNotNull](eq-not-null.md)                      | Learn more about the 'notNull' variants |
