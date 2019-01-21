package ru.capjack.kt.inject.internal.bindings

import ru.capjack.kt.inject.internal.Binding
import ru.capjack.kt.inject.internal.InjectorImpl

internal abstract class InjectedBinding<T : Any>(
	protected val injector: InjectorImpl
) : Binding<T>

