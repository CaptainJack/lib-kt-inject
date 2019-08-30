package ru.capjack.tool.depin.internal

import kotlin.reflect.KClass

internal actual class ProducingStack actual constructor() : Iterable<KClass<*>> {
	private val list = object : ThreadLocal<MutableList<KClass<*>>>() {
		override fun initialValue(): MutableList<KClass<*>> = mutableListOf()
	}
	
	actual fun contains(clazz: KClass<*>): Boolean {
		return list.get().contains(clazz)
	}
	
	actual fun add(clazz: KClass<*>) {
		list.get().add(clazz)
	}
	
	actual fun remove() {
		list.get().apply { removeAt(lastIndex) }
	}
	
	override fun iterator(): Iterator<KClass<*>> {
		return list.get().iterator()
	}
}