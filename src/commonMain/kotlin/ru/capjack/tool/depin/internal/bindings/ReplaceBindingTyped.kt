package ru.capjack.tool.depin.internal.bindings

import ru.capjack.tool.depin.internal.InjectorImpl
import kotlin.reflect.KClass

internal class ReplaceBindingTyped<T : Any>(
	private val type: KClass<T>,
	injector: InjectorImpl,
	provider: (InjectorImpl) -> T
) : ReplaceBinding<T>(injector, provider) {
	override fun replace(binding: InstanceBinding<T>) {
		injector.registry.setBinding(type, binding)
	}
}