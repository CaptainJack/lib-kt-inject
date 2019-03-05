package ru.capjack.tool.kt.inject

inline fun <reified T : Any> String.asType(): TypedName<T> {
	return TypedName(T::class, this)
}
