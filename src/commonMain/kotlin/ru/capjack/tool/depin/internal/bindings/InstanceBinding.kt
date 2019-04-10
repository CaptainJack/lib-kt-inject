package ru.capjack.tool.depin.internal.bindings

import ru.capjack.tool.depin.internal.Binding

internal class InstanceBinding<T : Any>(
	private val instance: T
) : Binding<T> {
	override fun get(): T {
		return instance
	}
}