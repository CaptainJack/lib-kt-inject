package ru.capjack.tool.depin.internal.bindings

import ru.capjack.tool.depin.internal.InjectorImpl

internal open class SupplierBinding<T : Any>(
	injector: InjectorImpl,
	private val supplier: (InjectorImpl) -> T
) : InjectedBinding<T>(injector) {
	override fun get(): T {
		return supplier.invoke(injector)
	}
}