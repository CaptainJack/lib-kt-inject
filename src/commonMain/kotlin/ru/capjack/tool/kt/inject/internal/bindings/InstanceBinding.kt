package ru.capjack.tool.kt.inject.internal.bindings

import ru.capjack.tool.kt.inject.internal.Binding

internal class InstanceBinding<T : Any>(
	private val instance: T
) : Binding<T> {
	override fun get(): T {
		return instance
	}
}