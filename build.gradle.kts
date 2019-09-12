import ru.capjack.tool.reflect.gradle.JsReflectTarget.Unit.ANNOTATIONS
import ru.capjack.tool.reflect.gradle.JsReflectTarget.Unit.MEMBERS

plugins {
	kotlin("multiplatform") version "1.3.50"
	id("nebula.release") version "11.1.0"
	id("ru.capjack.depver") version "1.0.0"
	id("ru.capjack.bintray") version "1.0.0"
	id("ru.capjack.logging") version "1.0.0"
	id("ru.capjack.reflect") version "1.1.0"
}

allprojects {
	group = "ru.capjack.tool"
	repositories {
		jcenter()
		mavenLocal()
		maven("https://dl.bintray.com/capjack/public")
	}
}

jsReflect {
	withAnnotation("ru.capjack.tool.depin.Inject")
	withAnnotation("ru.capjack.tool.depin.Bind", ANNOTATIONS)
	withAnnotation("ru.capjack.tool.depin.Proxy", ANNOTATIONS, MEMBERS)
	withAnnotation("ru.capjack.tool.depin.Implementation", ANNOTATIONS)
	
	withClass("ru.capjack.tool.depin.StubUser")
	withClass("ru.capjack.tool.depin.StubUserFactory")
	withClass("ru.capjack.tool.depin.StubStorage")
	withClass("ru.capjack.tool.depin.StubNotInjectable")
}

kotlin {
	jvm {
		compilations.all { kotlinOptions.jvmTarget = "1.8" }
	}
	js {
		browser()
		compilations["main"].kotlinOptions {
			sourceMap = true
			sourceMapEmbedSources = "always"
		}
	}
	
	sourceSets {
		get("commonMain").dependencies {
			implementation(kotlin("stdlib-common"))
			implementation("ru.capjack.tool:tool-reflect")
			implementation("ru.capjack.tool:tool-logging")
		}
		get("commonTest").dependencies {
			implementation(kotlin("test-common"))
			implementation(kotlin("test-annotations-common"))
		}
		
		get("jvmMain").dependencies {
			implementation(kotlin("stdlib-jdk8"))
			implementation(kotlin("reflect"))
		}
		get("jvmTest").dependencies {
			implementation(kotlin("test-junit"))
			implementation("ch.qos.logback:logback-classic:1.2.+")
		}
		
		get("jsMain").dependencies {
			implementation(kotlin("stdlib-js"))
		}
		get("jsTest").dependencies {
			implementation(kotlin("test-js"))
		}
	}
}