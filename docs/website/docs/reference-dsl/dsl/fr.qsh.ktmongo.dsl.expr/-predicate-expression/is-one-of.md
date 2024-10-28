//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[PredicateExpression](index.md)/[isOneOf](is-one-of.md)

# isOneOf

[jvm]\
fun [isOneOf](is-one-of.md)(values: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](index.md)&gt;)

Selects documents for which this field is equal to one of the given [values](is-one-of.md).

### Example

```kotlin
class User(
    val name: String,
    val age: Int?,
)

collection.find {
    User::name {
        isOneOf(listOf("Alfred", "Arthur"))
    }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/in/)

#### See also

|                                                                |
|----------------------------------------------------------------|
| [FilterExpression.isOneOf](../-filter-expression/is-one-of.md) |

[jvm]\
fun [isOneOf](is-one-of.md)(vararg values: [T](index.md))

Selects documents for which this field is equal to one of the given [values](is-one-of.md).

### Example

```kotlin
class User(
    val name: String,
    val age: Int?,
)

collection.find {
    User::name {
        isOneOf("Alfred", "Arthur")
    }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/in/)

#### See also

|                                                                |
|----------------------------------------------------------------|
| [FilterExpression.isOneOf](../-filter-expression/is-one-of.md) |
