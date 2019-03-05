package ru.capjack.tool.kt.inject.internal.bindings

import ru.capjack.tool.kt.inject.internal.InjectorImpl
import kotlin.reflect.KClass

internal class ImplementationBinding<T : Any>(
	injector: InjectorImpl,
	private val target: KClass<out T>
) : InjectedBinding<T>(injector) {
	override fun get(): T {
		return injector.get(target)
	}
}