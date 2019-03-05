package ru.capjack.tool.kt.inject

import kotlin.reflect.KClass

data class TypedName<T : Any>(
	val type: KClass<T>,
	val name: String
)