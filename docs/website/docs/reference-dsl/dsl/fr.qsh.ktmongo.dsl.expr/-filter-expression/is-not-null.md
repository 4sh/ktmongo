//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[isNotNull](is-not-null.md)

# isNotNull

[jvm]\
fun [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), *&gt;.[isNotNull](is-not-null.md)()

Selects documents for which the field is not `null`.

### Example

```kotlin
class User(
    val name: String,
    val age: Int?,
)

collection.find {
    User::age.isNotNull()
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/tutorial/query-for-null-fields/#type-check)

#### See also

|                                       |           |
|---------------------------------------|-----------|
| [FilterExpression.isNull](is-null.md) | Opposite. |
