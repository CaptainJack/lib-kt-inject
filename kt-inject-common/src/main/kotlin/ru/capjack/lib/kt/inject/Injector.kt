package ru.capjack.lib.kt.inject

import kotlin.reflect.KClass

interface Injector {
	fun <T : Any> get(clazz: KClass<T>): T
	
	fun <T : Any> get(name: TypedName<T>): T
	
	companion object {
		operator fun invoke(strong: Boolean = true, init: InjectorBuilder.() -> Unit): Injector {
			return InjectorBuilder().also(init).build(strong)
		}
	}
}