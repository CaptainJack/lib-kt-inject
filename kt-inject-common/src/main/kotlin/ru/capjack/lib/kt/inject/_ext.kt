package ru.capjack.lib.kt.inject

inline fun <reified T : Any> String.asType(): TypedName<T> {
	return TypedName(T::class, this)
}
