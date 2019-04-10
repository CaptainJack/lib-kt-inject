plugins {
	`kotlin-dsl`
	`java-gradle-plugin`
	`maven-publish`
	id("com.gradle.plugin-publish") version "0.10.1"
	id("ru.capjack.reflect")
}

dependencies {
	implementation("ru.capjack.tool:tool-reflect-gradle")
}

gradlePlugin {
	plugins.create("Depin") {
		id = "ru.capjack.depin"
		implementationClass = "ru.capjack.tool.depin.gradle.DepinPlugin"
		displayName = "ru.capjack.depin"
	}
}

pluginBundle {
	vcsUrl = "https://github.com/CaptainJack/tool-depin"
	website = vcsUrl
	description = "Plugin for support tool-depin library"
	tags = listOf("capjack", "kotlin", "dependency injection")
}

rootProject.tasks["postRelease"].dependsOn(tasks["publishPlugins"])

tasks.withType<ProcessResources> {
	inputs.property("version", version.toString())
	expand(project.properties)
}

