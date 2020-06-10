package ru.capjack.tool.depin.internal

import ru.capjack.tool.depin.Bind
import ru.capjack.tool.depin.Inject
import ru.capjack.tool.depin.InjectException
import ru.capjack.tool.reflect.hasAnnotation
import ru.capjack.tool.reflect.isInterface
import ru.capjack.tool.reflect.primaryConstructor
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

internal fun <T : Any> KClass<T>.extractPrimaryConstructor(): KFunction<T> {
	return primaryConstructor
		?: throw InjectException("Class '$this' not have constructor")
}

fun KClass<*>.checkClassInjectable() {
	if (isInterface) {
		throw InjectException("Class '$this' is an interface and cannot be instantiated")
	}
	
	if (!(hasAnnotation<Inject>() || hasAnnotation<Bind>())) {
		throw InjectException("Class '$this' is not injectable and cannot be instantiated")
	}
}