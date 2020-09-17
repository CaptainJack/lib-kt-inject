plugins {
	kotlin("multiplatform") version "1.4.10"
	id("nebula.release") version "15.2.0"
	id("ru.capjack.bintray") version "1.0.0"
}

allprojects {
	group = "ru.capjack.tool"
	repositories {
		jcenter()
		mavenLocal()
		maven("https://dl.bintray.com/capjack/public")
	}
}

kotlin {
	jvm {
		compilations.all { kotlinOptions.jvmTarget = "1.8" }
	}
	
	sourceSets {
		get("commonMain").dependencies {
			implementation("ru.capjack.tool:tool-reflect:1.2.0")
			implementation("ru.capjack.tool:tool-logging:1.2.0")
		}
		get("commonTest").dependencies {
			implementation(kotlin("test-common"))
			implementation(kotlin("test-annotations-common"))
		}
		
		get("jvmMain").dependencies {
			implementation(kotlin("reflect"))
		}
		get("jvmTest").dependencies {
			implementation(kotlin("test-junit"))
			implementation("ch.qos.logback:logback-classic:1.2.+")
		}
	}
}