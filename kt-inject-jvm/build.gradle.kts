import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("platform.jvm")
	id("io.freefair.sources-jar")
	id("ru.capjack.capjack-bintray")
}

dependencies {
	expectedBy(project(":kt-inject-common"))
	
	implementation(kotlin("stdlib-jdk8"))
	implementation(kotlin("reflect"))
	implementation("ru.capjack.lib.kt.logging:kt-logging-jvm")
	implementation("ru.capjack.lib.kt.reflect:kt-reflect-jvm")
	
	testImplementation(kotlin("test-junit"))
	testImplementation("ch.qos.logback:logback-classic:1.2.+")
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = "1.8"
}