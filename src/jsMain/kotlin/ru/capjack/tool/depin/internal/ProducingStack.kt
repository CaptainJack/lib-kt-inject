package ru.capjack.tool.depin.internal

import kotlin.reflect.KClass

internal actual class ProducingStack : Iterable<KClass<*>> {
	private val list = mutableListOf<KClass<*>>()
	
	actual fun contains(clazz: KClass<*>): Boolean {
		return list.contains(clazz)
	}
	
	actual fun add(clazz: KClass<*>) {
		list.add(clazz)
	}
	
	actual fun remove() {
		list.removeAt(list.lastIndex)
	}
	
	override fun iterator(): Iterator<KClass<*>> {
		return list.iterator()
	}
}