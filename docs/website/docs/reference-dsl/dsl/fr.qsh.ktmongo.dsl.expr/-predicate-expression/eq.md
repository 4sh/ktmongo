//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[PredicateExpression](index.md)/[eq](eq.md)

# eq

[jvm]\
fun [eq](eq.md)(value: [T](index.md))

Matches documents where the value of a field equals the [value](eq.md).

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

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/eq/)

#### See also

|                                                    |            |
|----------------------------------------------------|------------|
| [FilterExpression.eq](../-filter-expression/eq.md) | Shorthand. |
