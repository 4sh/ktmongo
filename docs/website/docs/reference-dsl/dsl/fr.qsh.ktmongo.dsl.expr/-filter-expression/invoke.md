//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[invoke](invoke.md)

# invoke

[jvm]\
operator fun &lt;[V](invoke.md)&gt; [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), [V](invoke.md)&gt;.[invoke](invoke.md)(block: [PredicateExpression](../-predicate-expression/index.md)&lt;[V](invoke.md)&gt;.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))

Targets a single field to execute a [targeted predicate](../-predicate-expression/index.md).

### Example

```kotlin
class User(
    val name: String?,
    val age: Int,
)

collection.find {
    User::name {
        eq("foo")
    }
}
```

Note that many operators available this way have a convenience function directly in this class to shorten this. For this example, see [eq](eq.md):

```kotlin
collection.find {
    User::name eq "foo"
}
```
