//[dsl](../../index.md)/[fr.qsh.ktmongo.dsl.path](index.md)/[get](get.md)

# get

[jvm]\
operator fun &lt;[T0](get.md), [T1](get.md)&gt; [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T0](get.md), [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[T1](get.md)&gt;&gt;.[get](get.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T0](get.md), [T1](get.md)&gt;

Denotes a specific item in an array, by index.

### Examples

```kotlin
class User(
    val name: String,
    val friends: List<Friend>,
)

class Friend(
    val name: String,
)

// Refer to the first friend
println(User::friends[0])
// → 'friends.$0'

// Refer to the third friend's name
println(User::friends[2] / Friend::name)
// → 'friends.$0.name'
```

#### See also

|               |                                          |
|---------------|------------------------------------------|
| [div](div.md) | Access based on the field name (`.foo.`) |
