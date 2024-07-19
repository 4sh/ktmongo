//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[PredicateExpression](index.md)/[doesNotExist](does-not-exist.md)

# doesNotExist

[jvm]\
fun [doesNotExist](does-not-exist.md)()

Matches documents that do not contain the specified field. Documents where the field if `null` are counted as existing.

### Example

```kotlin
class User(
    val name: String?,
    val age: Int,
)

collection.find {
    User::name {
        doesNotExist()
    }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/exists/)

#### See also

|                                                                          |                                                     |
|--------------------------------------------------------------------------|-----------------------------------------------------|
| [FilterExpression.doesNotExist](../-filter-expression/does-not-exist.md) | Shorthand.                                          |
| [PredicateExpression.exists](exists.md)                                  | Opposite.                                           |
| [PredicateExpression.isNull](is-null.md)                                 | Only matches elements that are specifically `null`. |
