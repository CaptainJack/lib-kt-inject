plugins {
	kotlin("multiplatform") version "1.7.10"
	id("ru.capjack.publisher") version "1.0.0"
}

group = "ru.capjack.tool"

repositories {
	mavenCentral()
	mavenCapjack()
}

kotlin {
	jvm {
		compilations.all { kotlinOptions.jvmTarget = "17" }
	}
	
	sourceSets {
		get("commonMain").dependencies {
			api("ru.capjack.tool:tool-reflect:1.5.0")
			implementation("ru.capjack.tool:tool-logging:1.7.0")
		}
		get("commonTest").dependencies {
			implementation(kotlin("test"))
		}
		
		get("jvmTest").dependencies {
			implementation("ch.qos.logback:logback-classic:1.2.+")
		}
	}
}