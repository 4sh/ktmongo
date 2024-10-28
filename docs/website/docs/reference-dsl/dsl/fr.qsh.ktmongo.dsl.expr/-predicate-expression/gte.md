//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[PredicateExpression](index.md)/[gte](gte.md)

# gte

[jvm]\
fun [gte](gte.md)(value: [T](index.md))

Selects documents for which this field has a value greater or equal to [value](gte.md).

### Example

```kotlin
class User(
    val name: String,
    val age: Int?,
)

collection.find {
    User::age { gte(18) }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/gte/)

#### See also

|                                                      |
|------------------------------------------------------|
| [FilterExpression.gte](../-filter-expression/gte.md) |
| [PredicateExpression.gteNotNull](gte-not-null.md)    |
