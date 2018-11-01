import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import ru.capjack.gradle.ktjs.test.karma.KarmaBrowser
import ru.capjack.kt.reflect.js.gradle.ReflectExtension
import ru.capjack.kt.reflect.js.gradle.ReflectPlugin
import ru.capjack.kt.reflect.js.gradle.ReflectTarget
import ru.capjack.kt.reflect.js.gradle.ReflectTarget.Unit.*

plugins {
	kotlin("platform.js")
	id("io.freefair.sources-jar")
	id("ru.capjack.capjack-bintray")
	id("ru.capjack.ktjs-test")
	id("ru.capjack.kt-logging-js")
	id("ru.capjack.kt-reflect-js")
}

dependencies {
	expectedBy(project(":kt-inject-common"))
	
	implementation(kotlin("stdlib-js"))
	implementation("ru.capjack.kt.logging:kt-logging-js:${ext["version.kt-logging"]}")
	implementation("ru.capjack.kt.reflect:kt-reflect-js:${ext["version.kt-reflect"]}")
	
	testImplementation(kotlin("test-js"))
}

configure<ReflectExtension> {
	withAnnotation("ru.capjack.kt.inject.Inject")
	withAnnotation("ru.capjack.kt.inject.InjectBind", ANNOTATIONS)
	withAnnotation("ru.capjack.kt.inject.InjectProxyFactory", ANNOTATIONS, MEMBERS)
	withAnnotation("ru.capjack.kt.inject.InjectDelegate", ANNOTATIONS)
	
	withClass("ru.capjack.kt.inject.StubUser")
	withClass("ru.capjack.kt.inject.StubUserFactory")
	withClass("ru.capjack.kt.inject.StubStorage")
	withClass("ru.capjack.kt.inject.StubNotInjectable")
}

tasks.withType<KotlinJsCompile> {
	kotlinOptions {
		moduleKind = "umd"
		sourceMap = true
		sourceMapEmbedSources = "always"
	}
}