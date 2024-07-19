//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[PredicateExpression](index.md)/[exists](exists.md)

# exists

[jvm]\
fun [exists](exists.md)()

Matches documents that contain the specified field, including values where the field value is `null`.

### Example

```kotlin
class User(
    val name: String?,
    val age: Int,
)

collection.find {
    User::name {
        exists()
    }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/exists/)

#### See also

|                                                            |                                                                   |
|------------------------------------------------------------|-------------------------------------------------------------------|
| [FilterExpression.exists](../-filter-expression/exists.md) | Shorthand.                                                        |
| [PredicateExpression.doesNotExist](does-not-exist.md)      | Opposite.                                                         |
| [PredicateExpression.isNotNull](is-not-null.md)            | Identical, but does not match elements where the field is `null`. |
