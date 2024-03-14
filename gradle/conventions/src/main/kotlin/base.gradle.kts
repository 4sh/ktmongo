package conventions

val appVersion: String? by project

version = appVersion ?: "DEV"
group = "fr.4sh"

tasks.withType<Test> {
	useJUnitPlatform()
}
