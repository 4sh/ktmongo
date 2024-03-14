plugins {
	id("conventions.base")

	alias(libs.plugins.kotlin)
	alias(libs.plugins.dokkatoo)
}

kotlin {
	jvm()

	sourceSets.commonMain.dependencies {
		api(projects.dsl)
	}
}
