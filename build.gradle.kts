import org.gradle.api.internal.artifacts.ResolvableDependency
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency
import org.jetbrains.kotlin.utils.addToStdlib.safeAs

plugins {
	id("nebula.release") version "8.0.3"
	
	kotlin("jvm") version "1.2.61" apply false
	id("io.freefair.sources-jar") version "2.7.3" apply false
	id("ru.capjack.capjack-bintray") version "0.10.0-dev.1+32a2534" apply false
	id("ru.capjack.ktjs-test") version "0.4.0" apply false
	id("ru.capjack.kt-logging-js") version "0.6.0" apply false
	id("ru.capjack.kt-reflect-js") version "0.7.0" apply false
}

subprojects {
	group = "ru.capjack.kt.inject"
	
	ext["version.kt-logging"] = "0.6.0"
	ext["version.kt-reflect"] = "0.7.0"
	
	repositories {
		jcenter()
	}
}