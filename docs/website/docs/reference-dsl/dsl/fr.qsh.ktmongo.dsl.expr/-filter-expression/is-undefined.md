//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[isUndefined](is-undefined.md)

# isUndefined

[jvm]\
fun [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), *&gt;.[isUndefined](is-undefined.md)()

Selects documents for which the field is `undefined`.

### Example

```kotlin
class User(
    val name: String,
    val age: Int?,
)

collection.find {
    User::age.isUndefined()
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/tutorial/query-for-null-fields/#type-check)

#### See also

|                                                        |           |
|--------------------------------------------------------|-----------|
| [FilterExpression.isNotUndefined](is-not-undefined.md) | Opposite. |
