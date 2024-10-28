//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[eq](eq.md)

# eq

[jvm]\
infix fun &lt;[V](eq.md)&gt; [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), [V](eq.md)&gt;.[eq](eq.md)(value: [V](eq.md))

Matches documents where the value of a field equals the [value](eq.md).

### Example

```kotlin
class User(
    val name: String?,
    val age: Int,
)

collection.find {
    User::name eq "foo"
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/eq/)
