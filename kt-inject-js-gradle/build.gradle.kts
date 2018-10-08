import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	`kotlin-dsl`
	`java-gradle-plugin`
	`maven-publish`
	id("com.gradle.plugin-publish") version "0.10.0"
	id("io.freefair.sources-jar")
	id("ru.capjack.capjack-bintray")
}

dependencies {
	implementation("ru.capjack.lib.kt.reflect:kt-reflect-js-gradle")
}

gradlePlugin {
	plugins.create("KtInjectJs") {
		id = "ru.capjack.lib.kt-inject-js"
		implementationClass = "ru.capjack.lib.kt.inject.js.gradle.InjectPlugin"
		displayName = "Lib KtInjectJs"
	}
}

pluginBundle {
	vcsUrl = "https://github.com/CaptainJack/lib-kt-inject"
	website = vcsUrl
	description = "Kotlin compiler plugin for support kt-inject-js library"
	tags = listOf("kotlin", "javascript", "inject")
}

rootProject.tasks["postRelease"].dependsOn(tasks["publishPlugins"])

tasks.withType<ProcessResources> {
	inputs.property("version", version.toString())
	expand(project.properties)
}

