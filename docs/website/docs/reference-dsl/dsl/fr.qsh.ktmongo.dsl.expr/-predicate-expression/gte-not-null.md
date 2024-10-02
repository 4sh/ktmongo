//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[PredicateExpression](index.md)/[gteNotNull](gte-not-null.md)

# gteNotNull

[jvm]\
fun [gteNotNull](gte-not-null.md)(value: [T](index.md)?)

Selects documents for which this field has a value greater or equal to [value](gte-not-null.md).

If [value](gte-not-null.md) is `null`, the operator is not added (all elements are matched).

### Example

```kotlin
class User(
    val name: String,
    val age: Int?
)

collection.find {
    User::age { gteNotNull(18) }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/gte/)

#### See also

|                                                                      |
|----------------------------------------------------------------------|
| [FilterExpression.gteNotNull](../-filter-expression/gte-not-null.md) |
| [PredicateExpression.eqNotNull](eq-not-null.md)                      | Learn more about the 'notNull' variants |
