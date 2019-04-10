package ru.capjack.tool.depin.gradle

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import ru.capjack.tool.reflect.gradle.JsReflectExtension
import ru.capjack.tool.reflect.gradle.JsReflectTarget.Unit.ANNOTATIONS
import ru.capjack.tool.reflect.gradle.JsReflectTarget.Unit.MEMBERS
import ru.capjack.tool.reflect.gradle.ReflectPlugin

open class DepinPlugin : Plugin<Project> {
	companion object {
		const val NAME = "tool-depin"
		const val ARTIFACT_GROUP = "ru.capjack.tool"
		
		val VERSION = this::class.java.classLoader.getResource("$NAME-version").readText()
	}
	
	override fun apply(project: Project) {
		configureDefaultVersionsResolutionStrategy(project)
		
		project.pluginManager.apply(ReflectPlugin::class)
		
		if (ReflectPlugin.isJsApplicable(project)) {
			project.configure<JsReflectExtension> {
				withAnnotation("ru.capjack.tool.depin.Inject")
				withAnnotation("ru.capjack.tool.depin.Bind", ANNOTATIONS)
				withAnnotation("ru.capjack.tool.depin.Proxy", ANNOTATIONS, MEMBERS)
				withAnnotation("ru.capjack.tool.depin.Implementation", ANNOTATIONS)
			}
		}
	}
	
	private fun configureDefaultVersionsResolutionStrategy(project: Project) {
		project.configurations.all {
			resolutionStrategy.eachDependency(Action {
				if (requested.group == ARTIFACT_GROUP && requested.name.startsWith(NAME) && requested.version.isNullOrEmpty()) {
					useVersion(VERSION)
				}
			})
		}
	}
}