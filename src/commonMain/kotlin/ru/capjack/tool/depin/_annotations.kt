package ru.capjack.tool.depin

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class Inject

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
annotation class Bind

@Target(AnnotationTarget.CLASS)
annotation class Proxy

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Implementation(val type: KClass<out Any>)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Name(val name: String = "")
