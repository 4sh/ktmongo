//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[PredicateExpression](index.md)/[not](not.md)

# not

[jvm]\
fun [not](not.md)(expression: [PredicateExpression](index.md)&lt;[T](index.md)&gt;.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))

Performs a logical `NOT` operation on the specified [expression](not.md) and selects the documents that *do not* match the expression. This includes the elements that do not contain the field.

### Example

```kotlin
class User(
    val name: String,
    val age: Int,
)

collection.find {
    User::age {
        not {
            hasType(BsonType.STRING)
        }
    }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/not/)

#### See also

|                                                      |            |
|------------------------------------------------------|------------|
| [FilterExpression.not](../-filter-expression/not.md) | Shorthand. |
