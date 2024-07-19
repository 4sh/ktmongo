//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[doesNotExist](does-not-exist.md)

# doesNotExist

[jvm]\
fun [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), *&gt;.[doesNotExist](does-not-exist.md)()

Matches documents that do not contain the specified field. Documents where the field if `null` are not matched.

### Example

```kotlin
class User(
    val name: String?,
    val age: Int,
)

collection.find {
    User::age.doesNotExist()
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/exists/)

#### See also

|                                       |                                                      |
|---------------------------------------|------------------------------------------------------|
| [FilterExpression.exists](exists.md)  | Opposite.                                            |
| [FilterExpression.isNull](is-null.md) | Only matches documents that are specifically `null`. |
