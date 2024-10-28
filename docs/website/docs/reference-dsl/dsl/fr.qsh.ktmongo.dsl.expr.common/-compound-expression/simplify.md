//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr.common](../index.md)/[CompoundExpression](index.md)/[simplify](simplify.md)

# simplify

[jvm]\

@[LowLevelApi](../../fr.qsh.ktmongo.dsl/-low-level-api/index.md)

override fun [simplify](simplify.md)(): [Expression](../-expression/index.md)?

Allows the implementation to replace itself by another more appropriate representation.

For example, if the current node is an `$and` operator with a single child, it may use this function to replace itself by that child.

**Implementations must be pure.**
