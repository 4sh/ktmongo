//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[PredicateExpression](index.md)/[hasType](has-type.md)

# hasType

[jvm]\
fun [hasType](has-type.md)(type: BsonType)

Selects documents where the value of the field is an instance of the specified BSON [type](has-type.md).

Querying by data type is useful when dealing with highly unstructured data where data types are not predictable.

### Example

```kotlin
class User(
    val name: String,
    val age: Any,
)

collection.find {
    User::age {
        type(BsonType.STRING)
    }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/type/)

#### See also

|                                                               |                                                    |
|---------------------------------------------------------------|----------------------------------------------------|
| [FilterExpression.hasType](../-filter-expression/has-type.md) | Shorthand.                                         |
| [PredicateExpression.isNull](is-null.md)                      | Checks if a value has the type BsonType.NULL.      |
| [PredicateExpression.isUndefined](is-undefined.md)            | Checks if a value has the type BsonType.UNDEFINED. |
