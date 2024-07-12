//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[PredicateExpression](index.md)/[isNull](is-null.md)

# isNull

[jvm]\
fun [isNull](is-null.md)()

Selects documents for which the field is `null`.

### Example

```kotlin
class User(
    val name: String,
    val age: Int?,
)

collection.find {
    User::age { isNull() }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/tutorial/query-for-null-fields/#type-check)

#### See also

|                                                             |                                 |
|-------------------------------------------------------------|---------------------------------|
| [FilterExpression.isNull](../-filter-expression/is-null.md) | Shorthand.                      |
| [PredicateExpression.doesNotExist](does-not-exist.md)       | Checks if the value is not set. |
| [PredicateExpression.isNotNull](is-not-null.md)             | Opposite.                       |
