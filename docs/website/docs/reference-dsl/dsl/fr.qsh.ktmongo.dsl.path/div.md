//[dsl](../../index.md)/[fr.qsh.ktmongo.dsl.path](index.md)/[div](div.md)

# div

[jvm]\
operator fun &lt;[T0](div.md), [T1](div.md), [T2](div.md)&gt; [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T0](div.md), [T1](div.md)&gt;.[div](div.md)(child: [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T1](div.md), [T2](div.md)&gt;): [KProperty1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property1/index.html)&lt;[T0](div.md), [T2](div.md)&gt;

Combines Kotlin properties into a path usable to point to a specific field in a document.

### Examples

```kotlin
class User(
    val id: Int,
    val profile: Profile,
)

class Profile(
    val name: String,
    val age: Int,
)

// Refer to the id
println(User::id)
// → 'id'

// Refer to the name
println(User::profile / Profile::name)
// → 'profile.name'

// Refer to the name
println(User::profile / Profile::age)
// → 'profile.age'
```

#### See also

|               |                         |
|---------------|-------------------------|
| [get](get.md) | Indexed access (`.$0.`) |
