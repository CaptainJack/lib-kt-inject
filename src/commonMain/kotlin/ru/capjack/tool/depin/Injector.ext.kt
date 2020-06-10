package ru.capjack.tool.depin

import ru.capjack.tool.reflect.callRef
import ru.capjack.tool.reflect.valueParameters
import kotlin.reflect.KCallable
import kotlin.reflect.KClass

inline fun <reified T : Any> Injector.get(): T {
	return get(T::class)
}

inline fun <reified T : Any> Injector.get(name: String): T {
	return get(T::class, name)
}

fun <T : Any> Injector.get(type: KClass<T>, name: String): T {
	return get(NamedType(type, name))
}

fun <T> Injector.inject(callable: KCallable<T>): (target: Any) -> T {
	return { target ->
		val args = callable.valueParameters.map(this::get)
		callable.callRef(target, *args.toTypedArray())
	}
}