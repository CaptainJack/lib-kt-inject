package ru.capjack.tool.kt.inject.internal.bindings

import ru.capjack.tool.kt.inject.internal.Binding
import ru.capjack.tool.kt.inject.internal.InjectorImpl

internal abstract class InjectedBinding<T : Any>(
	protected val injector: InjectorImpl
) : Binding<T>

