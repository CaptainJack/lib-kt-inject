import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	`kotlin-dsl`
	`java-gradle-plugin`
	`maven-publish`
	id("com.gradle.plugin-publish") version "0.10.0"
}

dependencies {
	implementation("ru.capjack.kt.reflect:kt-reflect-js-gradle:0.8.1")
}

gradlePlugin {
	plugins.create("KtInjectJs") {
		id = "ru.capjack.kt-inject-js"
		implementationClass = "ru.capjack.kt.inject.js.gradle.InjectPlugin"
		displayName = "Lib KtInjectJs"
	}
}

pluginBundle {
	vcsUrl = "https://github.com/CaptainJack/kt-inject"
	website = vcsUrl
	description = "Kotlin compiler plugin for support kt-inject-js library"
	tags = listOf("kotlin", "javascript", "inject")
}

rootProject.tasks["postRelease"].dependsOn(tasks["publishPlugins"])

tasks.withType<ProcessResources> {
	inputs.property("version", version.toString())
	expand(project.properties)
}

