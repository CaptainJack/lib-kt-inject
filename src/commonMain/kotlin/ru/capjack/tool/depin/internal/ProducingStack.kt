package ru.capjack.tool.depin.internal

import kotlin.reflect.KClass

internal expect class ProducingStack() : Iterable<KClass<*>> {
	fun contains(clazz: KClass<*>): Boolean
	fun add(clazz: KClass<*>)
	fun remove()
}
