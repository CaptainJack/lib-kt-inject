plugins {
	`kotlin-dsl`
	`java-gradle-plugin`
	`maven-publish`
	id("com.gradle.plugin-publish") version "0.10.0"
	id("ru.capjack.kt-reflect")
}

dependencies {
	implementation("ru.capjack.tool:kt-reflect-gradle")
}

gradlePlugin {
	plugins.create("KtInject") {
		id = "ru.capjack.kt-inject"
		implementationClass = "ru.capjack.tool.kt.inject.gradle.InjectPlugin"
		displayName = "kt-inject"
	}
}

pluginBundle {
	vcsUrl = "https://github.com/CaptainJack/kt-inject"
	website = vcsUrl
	description = "Plugin for support kt-inject library"
	tags = listOf("capjack", "kotlin", "inject")
}

rootProject.tasks["postRelease"].dependsOn(tasks["publishPlugins"])

tasks.withType<ProcessResources> {
	inputs.property("version", version.toString())
	expand(project.properties)
}

