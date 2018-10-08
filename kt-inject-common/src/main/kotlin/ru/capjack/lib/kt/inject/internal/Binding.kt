package ru.capjack.lib.kt.inject.internal

internal interface Binding<T : Any> {
	fun get(): T
}

