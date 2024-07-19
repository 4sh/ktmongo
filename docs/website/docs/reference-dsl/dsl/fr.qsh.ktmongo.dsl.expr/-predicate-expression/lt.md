//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[PredicateExpression](index.md)/[lt](lt.md)

# lt

[jvm]\
fun [lt](lt.md)(value: [T](index.md))

Selects documents for which this field has a value strictly lesser than [value](lt.md).

### Example

```kotlin
class User(
    val name: String,
    val age: Int?,
)

collection.find {
    User::age { lt(18) }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/lt/)

#### See also

|                                                    |
|----------------------------------------------------|
| [FilterExpression.lt](../-filter-expression/lt.md) |
| [PredicateExpression.ltNotNull](lt-not-null.md)    |
