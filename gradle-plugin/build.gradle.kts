plugins {
	kotlin("jvm")
	`java-gradle-plugin`
	`maven-publish`
	id("com.gradle.plugin-publish") version "0.10.1"
	id("ru.capjack.bintray")
}

repositories {
	gradlePluginPortal()
}

dependencies {
	compileOnly("ru.capjack.gradle:gradle-depver:0.2.0")
	implementation("ru.capjack.tool:tool-reflect-gradle:0.12.0")
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

