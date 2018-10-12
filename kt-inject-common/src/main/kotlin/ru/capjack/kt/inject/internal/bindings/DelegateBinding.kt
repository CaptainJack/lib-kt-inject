package ru.capjack.kt.inject.internal.bindings

import ru.capjack.kt.inject.internal.InjectorImpl
import kotlin.reflect.KClass

internal open class DelegateBinding<T : Any>(
	injector: InjectorImpl,
	private val target: KClass<out T>
) : InjectedBinding<T>(injector) {
	override fun get(): T {
		return injector.get(target)
	}
}