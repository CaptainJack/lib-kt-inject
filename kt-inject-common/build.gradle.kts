plugins {
	kotlin("platform.common")
	id("io.freefair.sources-jar")
	id("ru.capjack.capjack-bintray")
}

dependencies {
	implementation(kotlin("stdlib-common"))
	implementation("ru.capjack.kt.logging:kt-logging-common:${ext["version.kt-logging"]}")
	implementation("ru.capjack.kt.reflect:kt-reflect-common:${ext["version.kt-reflect"]}")
	
	testImplementation(kotlin("test-common"))
	testImplementation(kotlin("test-annotations-common"))
}