plugins {
	id("conventions.base")

	alias(libs.plugins.kotlin)
	alias(libs.plugins.dokkatoo)
}

dependencies {
	api(projects.dsl)
	api(libs.kotlinx.coroutines)

	api(libs.mongodb.coroutines)
}
