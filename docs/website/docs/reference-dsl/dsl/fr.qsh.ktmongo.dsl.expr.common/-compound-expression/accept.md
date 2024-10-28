//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl.expr.common](../index.md)/[CompoundExpression](index.md)/[accept](accept.md)

# accept

[jvm]\

@[LowLevelApi](../../fr.qsh.ktmongo.dsl/-low-level-api/index.md)

fun [accept](accept.md)(expression: [Expression](../-expression/index.md))

Binds an arbitrary [expression](accept.md) as a sub-expression of the receiver.

### Security and correctness

This function makes no verification on the validity of the passed expression. It is added to this expression as-is.

This function is only publicly available to allow users to add missing operators themselves by implementing [Expression](../-expression/index.md) for their operator.

**An incorrectly written expression may allow arbitrary code execution on the database, data corruption, or data leaks. Only call this function on expressions you are sure are implemented correctly!**
