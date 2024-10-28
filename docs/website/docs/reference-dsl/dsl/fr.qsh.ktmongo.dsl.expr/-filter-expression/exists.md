//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[exists](exists.md)

# exists

[jvm]\
fun [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), *&gt;.[exists](exists.md)()

Matches documents that contain the specified field, including values where the field value is `null`.

### Example

```kotlin
class User(
    val name: String?,
    val age: Int,
)

collection.find {
    User::age.exists()
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/exists/)

#### See also

|                                                    |                                                                   |
|----------------------------------------------------|-------------------------------------------------------------------|
| [FilterExpression.doesNotExist](does-not-exist.md) | Opposite.                                                         |
| [FilterExpression.isNotNull](is-not-null.md)       | Identical, but does not match elements where the field is `null`. |
