package ru.capjack.kt.inject

import ru.capjack.kt.inject.internal.BinderImpl
import ru.capjack.kt.inject.internal.InjectorImpl

class Injection() {
	private val configurations: MutableList<Binder.() -> Unit> = mutableListOf()
	
	constructor(init: Binder.() -> Unit) : this() {
		configure(init)
	}
	
	fun configure(configuration: Binder.() -> Unit): Injection {
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