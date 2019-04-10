package ru.capjack.tool.depin.internal.bindings

import ru.capjack.tool.depin.internal.Binding
import ru.capjack.tool.depin.internal.InjectorImpl

internal abstract class InjectedBinding<T : Any>(
	protected val injector: InjectorImpl
) : Binding<T>

