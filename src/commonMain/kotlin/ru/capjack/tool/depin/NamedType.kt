package ru.capjack.tool.depin

import kotlin.reflect.KClass

data class NamedType<T : Any>(
	val type: KClass<T>,
	val name: String
)

inline fun <reified T : Any> String.asNamedType(): NamedType<T> {
	return NamedType(T::class, this)
}
