plugins {
	id("conventions.base")

	alias(libs.plugins.kotlin)
	alias(libs.plugins.dokkatoo)
}

dependencies {
	implementation(libs.mongodb.bson)

	testImplementation(libs.bundles.kotest)
}

kotlin {
	compilerOptions.freeCompilerArgs.add("-Xallow-kotlin-package")
}
