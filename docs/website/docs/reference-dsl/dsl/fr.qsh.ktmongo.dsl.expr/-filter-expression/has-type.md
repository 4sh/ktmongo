//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[hasType](has-type.md)

# hasType

[jvm]\
infix fun [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), *&gt;.[hasType](has-type.md)(type: BsonType)

Selects documents where the value of the field is an instance of the specified BSON [type](has-type.md).

Querying by data type is useful when dealing with highly unstructured data where data types are not predictable.

### Example

```kotlin
class User(
    val name: String,
    val age: Any,
)

collection.find {
    User::age hasType BsonType.STRING
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/type/)

#### See also

|                                                 |                                                    |
|-------------------------------------------------|----------------------------------------------------|
| [FilterExpression.isNull](is-null.md)           | Checks if a value has the type BsonType.NULL.      |
| [FilterExpression.isUndefined](is-undefined.md) | Checks if a value has the type BsonType.UNDEFINED. |
