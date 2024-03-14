rootProject.name = "KtMongo"

dependencyResolutionManagement {
	repositories {
		mavenCentral()
	}
}

pluginManagement {
	includeBuild("gradle/conventions")
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include(
	"dsl",
	"driver-blocking",
	"driver-coroutines",
	"demo",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
