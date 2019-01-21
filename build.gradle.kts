import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import ru.capjack.kt.reflect.js.gradle.ReflectExtension
import ru.capjack.kt.reflect.js.gradle.ReflectTarget

plugins {
	kotlin("multiplatform") version "1.3.11"
	id("nebula.release") version "9.2.0"
	id("ru.capjack.capjack-bintray") version "0.14.1"
	id("ru.capjack.ktjs-test") version "0.8.0"
	id("ru.capjack.kt-reflect-js") version "0.8.1"
	id("ru.capjack.kt-logging-js") version "0.8.2"
}


allprojects {
	group = "ru.capjack.kt.inject"
	repositories {
		jcenter()
		maven("https://dl.bintray.com/capjack/public")
	}
}

capjackBintray {
	publications("*", "kt-inject-js-gradle")
}

ktReflect {
	withAnnotation("ru.capjack.kt.inject.Inject")
	withAnnotation("ru.capjack.kt.inject.InjectBind", ReflectTarget.Unit.ANNOTATIONS)
	withAnnotation("ru.capjack.kt.inject.InjectProxy", ReflectTarget.Unit.ANNOTATIONS, ReflectTarget.Unit.MEMBERS)
	withAnnotation("ru.capjack.kt.inject.InjectImplementation", ReflectTarget.Unit.ANNOTATIONS)
	
	withClass("ru.capjack.kt.inject.StubUser")
	withClass("ru.capjack.kt.inject.StubUserFactory")
	withClass("ru.capjack.kt.inject.StubStorage")
	withClass("ru.capjack.kt.inject.StubNotInjectable")
}

afterEvaluate {
	// https://youtrack.jetbrains.com/issue/KT-29058
	publishing.publications.forEach { (it as MavenPublication).groupId = group.toString() }
}

kotlin {
	targets {
		add(presets["jvm"].createTarget("jvm").apply {
			compilations.all {
				tasks.getByName<KotlinJvmCompile>(compileKotlinTaskName).kotlinOptions.jvmTarget = "1.8"
			}
		})
		
		add(presets["js"].createTarget("js").apply {
			compilations.all {
				tasks.getByName<KotlinJsCompile>(compileKotlinTaskName).kotlinOptions {
					sourceMap = true
					sourceMapEmbedSources = "always"
					moduleKind = "umd"
				}
			}
		})
	}
	
	sourceSets {
		commonMain {
			dependencies {
				implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
				implementation("ru.capjack.kt.reflect:kt-reflect-metadata:0.8.1")
				implementation("ru.capjack.kt.logging:kt-logging-metadata:0.8.2")
			}
		}
		commonTest {
			dependencies {
				implementation("org.jetbrains.kotlin:kotlin-test-common")
				implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
			}
		}
		
		named("jsMain") {
			dependencies {
				implementation("org.jetbrains.kotlin:kotlin-stdlib-js")
				implementation("ru.capjack.kt.reflect:kt-reflect-js:0.8.1")
				implementation("ru.capjack.kt.logging:kt-logging-js:0.8.2")
			}
		}
		named("jsTest") {
			dependencies {
				implementation("org.jetbrains.kotlin:kotlin-test-js")
			}
		}
		
		named("jvmMain") {
			dependencies {
				implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
				implementation("org.jetbrains.kotlin:kotlin-reflect")
				implementation("ru.capjack.kt.reflect:kt-reflect-jvm:0.8.1")
				implementation("ru.capjack.kt.logging:kt-logging-jvm:0.8.2")
			}
		}
		named("jvmTest") {
			dependencies {
				implementation("org.jetbrains.kotlin:kotlin-test-junit")
				implementation("ch.qos.logback:logback-classic:1.2.+")
			}
		}
	}
}