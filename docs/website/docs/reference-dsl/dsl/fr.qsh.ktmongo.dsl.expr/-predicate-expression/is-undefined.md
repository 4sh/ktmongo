//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[PredicateExpression](index.md)/[isUndefined](is-undefined.md)

# isUndefined

[jvm]\
fun [isUndefined](is-undefined.md)()

Selects documents for which the field is `undefined`.

### Example

```kotlin
class User(
    val name: String,
    val age: Int?,
)

collection.find {
    User::age { isUndefined() }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/tutorial/query-for-null-fields/#type-check)

#### See also

|                                                                       |            |
|-----------------------------------------------------------------------|------------|
| [FilterExpression.isUndefined](../-filter-expression/is-undefined.md) | Shorthand. |
| [PredicateExpression.isNotUndefined](is-not-undefined.md)             | Opposite.  |
