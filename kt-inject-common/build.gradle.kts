plugins {
	kotlin("platform.common")
	id("io.freefair.sources-jar")
	id("ru.capjack.capjack-bintray")
}

dependencies {
	implementation(kotlin("stdlib-common"))
	implementation("ru.capjack.lib.kt.logging:kt-logging-common")
	implementation("ru.capjack.lib.kt.reflect:kt-reflect-common")
	
	testImplementation(kotlin("test-common"))
	testImplementation(kotlin("test-annotations-common"))
}