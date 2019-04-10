package ru.capjack.tool.depin.internal

internal interface Binding<T : Any> {
	fun get(): T
}

