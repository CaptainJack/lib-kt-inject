package ru.capjack.tool.kt.inject.internal.bindings

import ru.capjack.tool.kt.inject.internal.InjectorImpl

internal abstract class ReplaceBinding<T : Any>(
	injector: InjectorImpl,
	provider: (InjectorImpl) -> T
) : ProducerBinding<T>(injector, provider) {
	
	override fun get(): T {
		return super.get().also {
			replace(InstanceBinding(it))
		}
	}
	
	abstract fun replace(binding: InstanceBinding<T>)
}