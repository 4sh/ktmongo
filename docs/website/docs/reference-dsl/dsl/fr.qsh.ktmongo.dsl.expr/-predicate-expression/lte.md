//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[PredicateExpression](index.md)/[lte](lte.md)

# lte

[jvm]\
fun [lte](lte.md)(value: [T](index.md))

Selects documents for which this field has a value lesser or equal to [value](lte.md).

### Example

```kotlin
class User(
    val name: String,
    val age: Int?,
)

collection.find {
    User::age { lte(18) }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/lte/)

#### See also

|                                                      |
|------------------------------------------------------|
| [FilterExpression.lte](../-filter-expression/lte.md) |
| [PredicateExpression.lteNotNull](lte-not-null.md)    |
