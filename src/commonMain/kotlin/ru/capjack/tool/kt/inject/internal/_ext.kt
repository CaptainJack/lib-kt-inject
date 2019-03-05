package ru.capjack.tool.kt.inject.internal

import ru.capjack.tool.kt.inject.Inject
import ru.capjack.tool.kt.inject.InjectException
import ru.capjack.tool.kt.reflect.hasAnnotation
import ru.capjack.tool.kt.reflect.isInterface
import ru.capjack.tool.kt.reflect.primaryConstructor
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

internal fun <T : Any> KClass<T>.extractPrimaryConstructor(): KFunction<T> {
	return primaryConstructor
		?: throw InjectException("Class '$this' not have constructor")
}

fun KClass<out Any>.checkClassInjectable() {
	if (isInterface) {
		throw InjectException("Class '$this' is an interface and cannot be instantiated")
	}
	
	if (!hasAnnotation<Inject>()) {
		throw InjectException("Class '$this' is not injectable and cannot be instantiated")
	}
}