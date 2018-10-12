package ru.capjack.kt.inject

import ru.capjack.kt.inject.internal.BinderImpl
import ru.capjack.kt.inject.internal.InjectorImpl

class InjectorBuilder {
	private val configurations: MutableList<Binder.() -> Unit> = mutableListOf()
	
	fun configure(configuration: Binder.() -> Unit): InjectorBuilder {
		configurations.add(configuration)
		return this
	}
	
	fun build(strong: Boolean = true): Injector {
		val injector = InjectorImpl()
		val binder = BinderImpl(injector, strong)
		configurations.forEach { it(binder) }
		return injector
	}
}