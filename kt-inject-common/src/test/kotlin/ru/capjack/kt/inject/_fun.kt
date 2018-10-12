package ru.capjack.kt.inject

fun injector(init: Binder.() -> Unit): Injector {
	return Injector { configure(init) }
}