//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[isNotUndefined](is-not-undefined.md)

# isNotUndefined

[jvm]\
fun [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), *&gt;.[isNotUndefined](is-not-undefined.md)()

Selects documents for which the field is not `undefined`.

### Example

```kotlin
class User(
    val name: String,
    val age: Int?,
)

collection.find {
    User::age.isNotUndefined()
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/tutorial/query-for-null-fields/#type-check)

#### See also

|                                                 |           |
|-------------------------------------------------|-----------|
| [FilterExpression.isUndefined](is-undefined.md) | Opposite. |
