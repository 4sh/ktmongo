//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[gteNotNull](gte-not-null.md)

# gteNotNull

[jvm]\
infix fun &lt;[V](gte-not-null.md)&gt; [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), [V](gte-not-null.md)&gt;.[gteNotNull](gte-not-null.md)(value: [V](gte-not-null.md)?)

Selects documents for which this field has a value greater or equal to [value](gte-not-null.md).

If [value](gte-not-null.md) is `null`, the operator is not added (all elements are matched).

### Example

```kotlin
class User(
	val name: String,
	val age: Int?
)

collection.find {
	User::age gteNotNull 10
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/gte/)

#### See also

|                                              |                                         |
|----------------------------------------------|-----------------------------------------|
| [FilterExpression.gte](gte.md)               |                                         |
| [FilterExpression.eqNotNull](eq-not-null.md) | Learn more about the 'notNull' variants |
