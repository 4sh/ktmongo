//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[PredicateExpression](index.md)/[isNotUndefined](is-not-undefined.md)

# isNotUndefined

[jvm]\
fun [isNotUndefined](is-not-undefined.md)()

Selects documents for which the field is not `undefined`.

### Example

```kotlin
class User(
    val name: String,
    val age: Int?,
)

collection.find {
    User::age { isNotUndefined() }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/tutorial/query-for-null-fields/#type-check)

#### See also

|                                                                              |            |
|------------------------------------------------------------------------------|------------|
| [FilterExpression.isNotUndefined](../-filter-expression/is-not-undefined.md) | Shorthand. |
| [PredicateExpression.isUndefined](is-undefined.md)                           | Opposite.  |
