package ru.capjack.kt.inject.internal

internal interface Binding<T : Any> {
	fun get(): T
}

