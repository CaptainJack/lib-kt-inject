package ru.capjack.tool.kt.inject.internal.bindings

import ru.capjack.tool.kt.inject.internal.InjectorImpl

internal open class ProducerBinding<T : Any>(
	injector: InjectorImpl,
	private val producer: (InjectorImpl) -> T
) : InjectedBinding<T>(injector) {
	override fun get(): T {
		return producer.invoke(injector)
	}
}