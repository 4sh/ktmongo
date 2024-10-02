//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[ne](ne.md)

# ne

[jvm]\
infix fun &lt;[V](ne.md)&gt; [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), [V](ne.md)&gt;.[ne](ne.md)(value: [V](ne.md))

Matches documents where the value of a field does not equal the [value](ne.md).

The result includes documents which do not contain the specified field.

### Example

```kotlin
class User(
    val name: String?,
    val age: Int,
)

collection.find {
    User::name ne "foo"
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/ne/)

#### See also

|                              |
|------------------------------|
| [FilterExpression.eq](eq.md) |
