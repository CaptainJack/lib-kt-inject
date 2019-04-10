package ru.capjack.tool.depin

inline fun <reified T : Any> String.asType(): TypedName<T> {
	return TypedName(T::class, this)
}
