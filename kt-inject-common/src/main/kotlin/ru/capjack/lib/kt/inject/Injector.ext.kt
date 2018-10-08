package ru.capjack.lib.kt.inject

import kotlin.reflect.KClass

inline fun <reified T : Any> Injector.get(): T {
	return get(T::class)
}

inline fun <reified T : Any> Injector.get(name: String): T {
	return get(T::class, name)
}

fun <T : Any> Injector.get(type: KClass<T>, name: String): T {
	return get(TypedName(type, name))
}