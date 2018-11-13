package ru.capjack.kt.inject

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class Inject

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
annotation class InjectBind

@Target(AnnotationTarget.CLASS)
annotation class InjectProxy

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class InjectDelegate(val type: KClass<out Any>)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class InjectName(val name: String = "_")
