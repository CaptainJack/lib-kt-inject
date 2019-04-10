import org.jetbrains.kotlin.cli.common.arguments.K2JsArgumentConstants
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import ru.capjack.tool.reflect.gradle.JsReflectTarget.Unit.ANNOTATIONS
import ru.capjack.tool.reflect.gradle.JsReflectTarget.Unit.MEMBERS

plugins {
	kotlin("multiplatform") version "1.3.21"
	id("nebula.release") version "10.0.1"
	id("ru.capjack.bintray") version "0.17.0"
	id("ru.capjack.kojste") version "0.11.0"
	id("ru.capjack.logging") version "0.12.1"
	id("ru.capjack.reflect") version "0.11.0"
}

allprojects {
	group = "ru.capjack.tool"
	repositories {
		jcenter()
		maven("https://dl.bintray.com/capjack/public")
	}
}

capjackBintray {
	publications(":", ":tool-depin-gradle")
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
	sourceSets {
		commonMain {
			dependencies {
				implementation(kotlin("stdlib-common"))
				implementation("ru.capjack.tool:tool-reflect-metadata")
				implementation("ru.capjack.tool:tool-logging-metadata")
			}
		}
		commonTest {
			dependencies {
				implementation(kotlin("test-common"))
				implementation(kotlin("test-annotations-common"))
			}
		}
	}
	
	jvm().compilations {
		all {
			kotlinOptions.jvmTarget = "1.8"
		}
		
		get(KotlinCompilation.MAIN_COMPILATION_NAME).defaultSourceSet {
			dependencies {
				implementation(kotlin("stdlib-jdk8"))
				implementation(kotlin("reflect"))
				implementation("ru.capjack.tool:tool-reflect-jvm")
				implementation("ru.capjack.tool:tool-logging-jvm")
			}
		}
		
		get(KotlinCompilation.TEST_COMPILATION_NAME).defaultSourceSet {
			dependencies {
				implementation(kotlin("test-junit"))
				implementation("ch.qos.logback:logback-classic:1.2.+")
			}
		}
	}
	
	js().compilations {
		all {
			kotlinOptions.moduleKind = K2JsArgumentConstants.MODULE_UMD
		}
		
		get(KotlinCompilation.MAIN_COMPILATION_NAME).defaultSourceSet {
			dependencies {
				implementation(kotlin("stdlib-js"))
				implementation("ru.capjack.tool:tool-reflect-js")
				implementation("ru.capjack.tool:tool-logging-js")
			}
		}
		
		get(KotlinCompilation.TEST_COMPILATION_NAME).defaultSourceSet {
			dependencies {
				implementation(kotlin("test-js"))
			}
		}
	}
}