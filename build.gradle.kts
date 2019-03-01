import org.jetbrains.kotlin.cli.common.arguments.K2JsArgumentConstants
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import ru.capjack.kt.reflect.gradle.JsReflectTarget.Unit.ANNOTATIONS
import ru.capjack.kt.reflect.gradle.JsReflectTarget.Unit.MEMBERS

plugins {
	kotlin("multiplatform") version "1.3.20"
	id("nebula.release") version "9.2.0"
	id("ru.capjack.ktjs-test") version "0.10.0"
	id("ru.capjack.capjack-bintray") version "0.16.0"
	id("ru.capjack.kt-logging") version "0.9.1"
	id("ru.capjack.kt-reflect") version "0.9.2"
}

allprojects {
	group = "ru.capjack.kt"
	repositories {
		jcenter()
		maven("https://dl.bintray.com/capjack/public")
	}
}

capjackBintray {
	publications(":", ":kt-inject-gradle")
}

ktReflectJs {
	withAnnotation("ru.capjack.kt.inject.Inject")
	withAnnotation("ru.capjack.kt.inject.InjectBind", ANNOTATIONS)
	withAnnotation("ru.capjack.kt.inject.InjectProxy", ANNOTATIONS, MEMBERS)
	withAnnotation("ru.capjack.kt.inject.InjectImplementation", ANNOTATIONS)
	
	withClass("ru.capjack.kt.inject.StubUser")
	withClass("ru.capjack.kt.inject.StubUserFactory")
	withClass("ru.capjack.kt.inject.StubStorage")
	withClass("ru.capjack.kt.inject.StubNotInjectable")
}

kotlin {
	sourceSets {
		commonMain {
			dependencies {
				implementation(kotlin("stdlib-common"))
				implementation("ru.capjack.kt:kt-reflect-metadata")
				implementation("ru.capjack.kt:kt-logging-metadata")
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
				implementation("ru.capjack.kt:kt-reflect-jvm")
				implementation("ru.capjack.kt:kt-logging-jvm")
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
				implementation("ru.capjack.kt:kt-reflect-js")
				implementation("ru.capjack.kt:kt-logging-js")
			}
		}
		
		get(KotlinCompilation.TEST_COMPILATION_NAME).defaultSourceSet {
			dependencies {
				implementation(kotlin("test-js"))
			}
		}
	}
}