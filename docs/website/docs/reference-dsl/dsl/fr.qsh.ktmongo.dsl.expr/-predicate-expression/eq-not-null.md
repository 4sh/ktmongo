//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr](../index.md)/[PredicateExpression](index.md)/[eqNotNull](eq-not-null.md)

# eqNotNull

[jvm]\
fun [eqNotNull](eq-not-null.md)(value: [T](index.md)?)

Matches documents where the value of a field equals [value](eq-not-null.md).

If [value](eq-not-null.md) is `null`, the operator is not added (all documents are matched).

### Example

This operator is useful to simplify searches when the criteria is optional. For example, instead of writing:

```kotlin
collection.find {
    User::name {
        if (criteria.name != null)
            eq(criteria.name)
    }
}
```

this operator can be used instead:

```kotlin
collection.find {
    User::name {
        eqNotNull(criteria.name)
    }
}
```

### External resources

-
[Official documentation](https://www.mongodb.com/docs/manual/reference/operator/query/eq/)

#### See also

|                                                                    |                  |
|--------------------------------------------------------------------|------------------|
| [FilterExpression.eqNotNull](../-filter-expression/eq-not-null.md) | Shorthand.       |
| [PredicateExpression.eq](eq.md)                                    | Equality filter. |
