//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[PredicateExpression](index.md)/[gt](gt.md)

# gt

[jvm]\
fun [gt](gt.md)(value: [T](index.md))

Selects documents for which this field has a value strictly greater than [value](gt.md).

### Example

```kotlin
class User(
    val name: String,
    val age: Int?,
)

collection.find {
    User::age { gt(18) }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/gt/)

#### See also

|                                                    |
|----------------------------------------------------|
| [FilterExpression.gt](../-filter-expression/gt.md) |
| [PredicateExpression.gtNotNull](gt-not-null.md)    |
