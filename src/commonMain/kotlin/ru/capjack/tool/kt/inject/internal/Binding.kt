package ru.capjack.tool.kt.inject.internal

internal interface Binding<T : Any> {
	fun get(): T
}

