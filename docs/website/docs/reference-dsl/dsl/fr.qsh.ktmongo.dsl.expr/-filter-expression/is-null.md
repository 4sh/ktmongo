//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[isNull](is-null.md)

# isNull

[jvm]\
fun [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), *&gt;.[isNull](is-null.md)()

Selects documents for which the field is `null`.

### Example

```kotlin
class User(
    val name: String,
    val age: Int?,
)

collection.find {
    User::age.isNull()
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/tutorial/query-for-null-fields/#type-check)

#### See also

|                                                    |                                 |
|----------------------------------------------------|---------------------------------|
| [FilterExpression.doesNotExist](does-not-exist.md) | Checks if the value is not set. |
| [FilterExpression.isNotNull](is-not-null.md)       | Opposite.                       |
