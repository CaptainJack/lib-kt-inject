package ru.capjack.lib.kt.inject.internal.bindings

import ru.capjack.lib.kt.inject.internal.Binding
import ru.capjack.lib.kt.reflect.callRef
import kotlin.reflect.KCallable

internal class MemberBinding<T : Any>(
	private val obj: Any,
	private val member: KCallable<*>
) : Binding<T> {
	override fun get(): T {
		@Suppress("UNCHECKED_CAST")
		return member.callRef(obj) as T
	}
}