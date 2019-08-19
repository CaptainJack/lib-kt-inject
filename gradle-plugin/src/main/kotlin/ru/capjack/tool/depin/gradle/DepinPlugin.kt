package ru.capjack.tool.depin.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.capjack.gradle.depver.DepverExtension
import ru.capjack.tool.reflect.gradle.JsReflectExtension
import ru.capjack.tool.reflect.gradle.JsReflectTarget.Unit.ANNOTATIONS
import ru.capjack.tool.reflect.gradle.JsReflectTarget.Unit.MEMBERS
import ru.capjack.tool.reflect.gradle.ReflectPlugin

open class DepinPlugin : Plugin<Project> {
	companion object {
		const val NAME = "tool-depin"
		const val ARTIFACT_GROUP = "ru.capjack.tool"
		
		val VERSION = this::class.java.classLoader.getResource("$NAME-version")!!.readText()
	}
	
	override fun apply(project: Project) {
		(project.extensions.findByName("depver") as? DepverExtension)?.set(ARTIFACT_GROUP, NAME, VERSION)
		
		project.pluginManager.apply(ReflectPlugin::class.java)
		
		project.extensions.getByType(JsReflectExtension::class.java).apply {
			withAnnotation("ru.capjack.tool.depin.Inject")
			withAnnotation("ru.capjack.tool.depin.Bind", ANNOTATIONS)
			withAnnotation("ru.capjack.tool.depin.Proxy", ANNOTATIONS, MEMBERS)
			withAnnotation("ru.capjack.tool.depin.Implementation", ANNOTATIONS)
		}
	}
}
