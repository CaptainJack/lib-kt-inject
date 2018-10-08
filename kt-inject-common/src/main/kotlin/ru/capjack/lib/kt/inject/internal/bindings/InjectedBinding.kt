package ru.capjack.lib.kt.inject.internal.bindings

import ru.capjack.lib.kt.inject.internal.Binding
import ru.capjack.lib.kt.inject.internal.InjectorImpl

internal abstract class InjectedBinding<T : Any>(
	protected val injector: InjectorImpl
) : Binding<T>

