package ru.capjack.tool.kt.inject.internal.bindings

import ru.capjack.tool.kt.inject.TypedName
import ru.capjack.tool.kt.inject.internal.InjectorImpl

internal class ReplaceBindingNamed<T : Any>(
	private val name: TypedName<T>,
	injector: InjectorImpl,
	producer: InjectorImpl.() -> T
) : ReplaceBinding<T>(injector, producer) {
	override fun replace(binding: InstanceBinding<T>) {
		injector.registry.setBinding(name, binding)
	}
}

