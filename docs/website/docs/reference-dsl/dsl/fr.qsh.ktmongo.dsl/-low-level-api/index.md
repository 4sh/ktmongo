//[dsl](../../../index.md)/[fr.qsh.ktmongo.dsl](../index.md)/[LowLevelApi](index.md)

# LowLevelApi

[jvm]\
annotation class [LowLevelApi](index.md)

Annotation to mark parts of the library that are not meant to be used by end-users.

They are still public because they may be needed by driver implementations, or to in case some functionality is missing in the high-level API (which is everything *not* annotated by this annotation).

Users should be cautious about using functionality from the low-level API, as it may not protect against incorrect usage. This could leak to injection attacks, memory leaks, or other unwanted consequences.
