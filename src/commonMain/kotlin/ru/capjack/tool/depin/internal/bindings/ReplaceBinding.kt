package ru.capjack.tool.depin.internal.bindings

import ru.capjack.tool.depin.internal.InjectorImpl

internal abstract class ReplaceBinding<T : Any>(
	injector: InjectorImpl,
	provider: (InjectorImpl) -> T
) : SupplierBinding<T>(injector, provider) {
	
	override fun get(): T {
		return super.get().also {
			replace(InstanceBinding(it))
		}
	}
	
	abstract fun replace(binding: InstanceBinding<T>)
}