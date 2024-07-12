//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[and](and.md)

# and

[jvm]\
fun [and](and.md)(block: [FilterExpression](index.md)&lt;[T](index.md)&gt;.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))

Performs a logical `AND` operation on one or more expressions, and selects the documents that satisfy *all* the expressions.

### Example

```kotlin
class User(
    val name: String?,
    val age: Int,
)

collection.findOne {
    and {
        User::name eq "foo"
        User::age eq 18
    }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/and/)

#### See also

|                              |                         |
|------------------------------|-------------------------|
| [FilterExpression.or](or.md) | Logical `OR` operation. |
