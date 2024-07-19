//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[isOneOf](is-one-of.md)

# isOneOf

[jvm]\
fun &lt;[V](is-one-of.md)&gt; [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), [V](is-one-of.md)&gt;.[isOneOf](is-one-of.md)(values: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[V](is-one-of.md)&gt;)

Selects documents for which this field is equal to one of the given [values](is-one-of.md).

### Example

```kotlin
class User(
    val name: String,
    val age: Int?,
)

collection.find {
    User::name.isOneOf(listOf("Alfred", "Arthur"))
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/in/)

#### See also

|                              |
|------------------------------|
| [FilterExpression.or](or.md) |
| [FilterExpression.eq](eq.md) |

[jvm]\
fun &lt;[V](is-one-of.md)&gt; [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), [V](is-one-of.md)&gt;.[isOneOf](is-one-of.md)(vararg values: [V](is-one-of.md))

Selects documents for which this field is equal to one of the given [values](is-one-of.md).

### Example

```kotlin
class User(
    val name: String,
    val age: Int?,
)

collection.find {
    User::name.isOneOf("Alfred", "Arthur")
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/in/)

#### See also

|                              |
|------------------------------|
| [FilterExpression.or](or.md) |
| [FilterExpression.eq](eq.md) |
