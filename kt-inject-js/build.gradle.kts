import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import ru.capjack.gradle.ktjs.test.karma.KarmaBrowser
import ru.capjack.lib.kt.reflect.js.gradle.ReflectExtension
import ru.capjack.lib.kt.reflect.js.gradle.ReflectPlugin
import ru.capjack.lib.kt.reflect.js.gradle.ReflectTarget
import ru.capjack.lib.kt.reflect.js.gradle.ReflectTarget.Unit.*

plugins {
	kotlin("platform.js")
	id("io.freefair.sources-jar")
	id("ru.capjack.capjack-bintray")
	id("ru.capjack.ktjs-test") version "0.3.0"
	id("ru.capjack.lib.kt-logging-js") version "0.5.0"
	id("ru.capjack.lib.kt-reflect-js") version "0.6.2"
}

dependencies {
	expectedBy(project(":kt-inject-common"))
	
	implementation(kotlin("stdlib-js"))
	implementation("ru.capjack.lib.kt.logging:kt-logging-js")
	implementation("ru.capjack.lib.kt.reflect:kt-reflect-js")
	
	testImplementation(kotlin("test-js"))
}

configure<ReflectExtension> {
	withAnnotation("ru.capjack.lib.kt.inject.Inject")
	withAnnotation("ru.capjack.lib.kt.inject.InjectBind", ANNOTATIONS)
	withAnnotation("ru.capjack.lib.kt.inject.InjectProxyFactory", ANNOTATIONS, MEMBERS)
	withAnnotation("ru.capjack.lib.kt.inject.InjectDelegate", ANNOTATIONS)
	
	withClass("ru.capjack.lib.kt.inject.StubUser")
	withClass("ru.capjack.lib.kt.inject.StubUserFactory")
	withClass("ru.capjack.lib.kt.inject.StubStorage")
	withClass("ru.capjack.lib.kt.inject.StubNotInjectable")
}

tasks.withType<KotlinJsCompile> {
	kotlinOptions {
		moduleKind = "umd"
		sourceMap = true
		sourceMapEmbedSources = "always"
	}
}