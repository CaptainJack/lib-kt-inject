plugins {
	kotlin("jvm") version "1.2.61" apply false
	id("io.freefair.sources-jar") version "2.7.3" apply false
	id("ru.capjack.capjack-bintray") version "0.8.0" apply false
	id("nebula.release") version "8.0.3"
}

subprojects {
	group = "ru.capjack.lib.kt.inject"
	
	configurations.all {
		resolutionStrategy.eachDependency {
			when (requested.group) {
				"ru.capjack.lib.kt.logging" -> useVersion("0.5.0")
				"ru.capjack.lib.kt.reflect" -> useVersion("0.6.2")
			}
		}
	}
	
	repositories {
		jcenter()
	}
}