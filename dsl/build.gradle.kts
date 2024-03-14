plugins {
	id("conventions.base")

	alias(libs.plugins.kotlin)
	alias(libs.plugins.dokkatoo)
}

kotlin {
	jvm()
	linuxX64() // unused for now, just there to block common code from using JVM-specific functionality
}
