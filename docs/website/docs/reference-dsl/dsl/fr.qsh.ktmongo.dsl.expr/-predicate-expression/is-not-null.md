//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[PredicateExpression](index.md)/[isNotNull](is-not-null.md)

# isNotNull

[jvm]\
fun [isNotNull](is-not-null.md)()

Selects documents for which the field is not `null`.

### Example

```kotlin
class User(
    val name: String,
    val age: Int?,
)

collection.find {
    User::age { isNotNull() }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/tutorial/query-for-null-fields/#type-check)

#### See also

|                                                                    |            |
|--------------------------------------------------------------------|------------|
| [FilterExpression.isNotNull](../-filter-expression/is-not-null.md) | Shorthand. |
| [PredicateExpression.isNull](is-null.md)                           | Opposite.  |
