package ru.capjack.tool.kt.inject.gradle

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import ru.capjack.tool.kt.reflect.gradle.JsReflectExtension
import ru.capjack.tool.kt.reflect.gradle.JsReflectTarget.Unit.ANNOTATIONS
import ru.capjack.tool.kt.reflect.gradle.JsReflectTarget.Unit.MEMBERS
import ru.capjack.tool.kt.reflect.gradle.ReflectPlugin

open class InjectPlugin : Plugin<Project> {
	companion object {
		const val ARTIFACT_GROUP = "ru.capjack.tool"
		
		val VERSION = this::class.java.classLoader.getResource("kt-inject-version").readText()
	}
	
	override fun apply(project: Project) {
		configureDefaultVersionsResolutionStrategy(project)
		
		project.pluginManager.apply(ReflectPlugin::class)
		
		project.configure<JsReflectExtension> {
			withAnnotation("ru.capjack.tool.kt.inject.Inject")
			withAnnotation("ru.capjack.tool.kt.inject.InjectBind", ANNOTATIONS)
			withAnnotation("ru.capjack.tool.kt.inject.InjectProxy", ANNOTATIONS, MEMBERS)
			withAnnotation("ru.capjack.tool.kt.inject.InjectImplementation", ANNOTATIONS)
		}
	}
	
	private fun configureDefaultVersionsResolutionStrategy(project: Project) {
		project.configurations.all {
			resolutionStrategy.eachDependency(Action {
				if (requested.group == ARTIFACT_GROUP && requested.name.startsWith("kt-inject-") && requested.version.isNullOrEmpty()) {
					useVersion(VERSION)
				}
			})
		}
	}
}