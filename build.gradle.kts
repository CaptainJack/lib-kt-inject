plugins {
	id("nebula.release") version "8.0.3"
	
	kotlin("jvm") version "1.2.61" apply false
	id("io.freefair.sources-jar") version "2.7.3" apply false
	id("ru.capjack.capjack-bintray") version "0.9.0" apply false
	id("ru.capjack.ktjs-test") version "0.4.0" apply false
	id("ru.capjack.kt-logging-js") version "0.6.0" apply false
	id("ru.capjack.kt-reflect-js") version "0.7.0" apply false
}

subprojects {
	group = "ru.capjack.kt.inject"
	
	configurations.all {
		resolutionStrategy.eachDependency {
			when (requested.group) {
				"ru.capjack.kt.logging" -> useVersion("0.6.0")
				"ru.capjack.kt.reflect" -> useVersion("0.7.0")
			}
		}
	}
	
	repositories {
		jcenter()
	}
}