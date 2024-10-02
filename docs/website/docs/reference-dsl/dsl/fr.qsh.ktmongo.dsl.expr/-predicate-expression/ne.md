//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[PredicateExpression](index.md)/[ne](ne.md)

# ne

[jvm]\
fun [ne](ne.md)(value: [T](index.md))

Matches documents where the value of a field does not equal the [value](ne.md).

The result includes documents which do not contain the specified field.

### Example

```kotlin
class User(
    val name: String?,
    val age: Int,
)

collection.find {
    User::name {
        ne("foo")
    }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/ne/)

#### See also

|                                                    |            |
|----------------------------------------------------|------------|
| [FilterExpression.ne](../-filter-expression/ne.md) | Shorthand. |
