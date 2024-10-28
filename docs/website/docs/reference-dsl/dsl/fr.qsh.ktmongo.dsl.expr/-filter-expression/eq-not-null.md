//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[FilterExpression](index.md)/[eqNotNull](eq-not-null.md)

# eqNotNull

[jvm]\
infix fun &lt;[V](eq-not-null.md)&gt; [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T](index.md), [V](eq-not-null.md)&gt;.[eqNotNull](eq-not-null.md)(value: [V](eq-not-null.md)?)

Matches documents where the value of a field equals [value](eq-not-null.md).

If [value](eq-not-null.md) is `null`, the operator is not added (all documents are matched).

### Example

This operator is useful to simplify searches when the criteria is optional. For example, instead of writing:

```kotlin
collection.find {
    if (criteria.name != null)
        User::name eq criteria.name
}
```

this operator can be used instead:

```kotlin
collection.find {
    User::name eqNotNull criteria.name
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/eq/)

#### See also

|                              |                  |
|------------------------------|------------------|
| [FilterExpression.eq](eq.md) | Equality filter. |
