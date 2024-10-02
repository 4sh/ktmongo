//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[or](or.md)

# or

[jvm]\
fun [or](or.md)(block: [FilterExpression](index.md)&lt;[T](index.md)&gt;.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))

Performs a logical `OR` operation on one or more expressions, and selects the documents that satisfy *at least one* of the expressions.

### Example

```kotlin
class User(
    val name: String?,
    val age: Int,
)

collection.find {
    or {
        User::name eq "foo"
        User::name eq "bar"
        User::age eq 18
    }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/or/)

#### See also

|                                |                          |
|--------------------------------|--------------------------|
| [FilterExpression.and](and.md) | Logical `AND` operation. |
