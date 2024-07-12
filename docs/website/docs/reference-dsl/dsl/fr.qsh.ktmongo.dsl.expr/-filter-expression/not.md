//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[not](not.md)

# not

[jvm]\
infix fun &lt;[V](not.md)&gt; [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), [V](not.md)&gt;.[not](not.md)(expression: [PredicateExpression](../-predicate-expression/index.md)&lt;[V](not.md)&gt;.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))

Performs a logical `NOT` operation on the specified [expression](not.md) and selects the documents that *do not* match the expression. This includes the elements that do not contain the field.

### Example

```kotlin
class User(
    val name: String,
    val age: Int,
)

collection.find {
    User::age not {
        hasType(BsonType.STRING)
    }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/not/)
