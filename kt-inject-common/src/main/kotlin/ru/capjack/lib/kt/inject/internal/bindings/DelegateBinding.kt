package ru.capjack.lib.kt.inject.internal.bindings

import ru.capjack.lib.kt.inject.internal.InjectorImpl
import kotlin.reflect.KClass

internal open class DelegateBinding<T : Any>(
	injector: InjectorImpl,
	private val target: KClass<out T>
) : InjectedBinding<T>(injector) {
	override fun get(): T {
		return injector.get(target)
	}
}