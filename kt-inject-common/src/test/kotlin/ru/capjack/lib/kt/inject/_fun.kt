package ru.capjack.lib.kt.inject

fun injector(init: Binder.() -> Unit): Injector {
	return Injector { configure(init) }
}