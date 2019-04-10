package ru.capjack.tool.depin.internal

import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.reflect.KClass

internal actual fun <T : Any> createProxyFactory(injector: InjectorImpl, clazz: KClass<T>, members: List<ProxyFactoryMember>): T {
	@Suppress("UNCHECKED_CAST")
	return Proxy.newProxyInstance(clazz.java.classLoader, arrayOf(clazz.java), ProxyInvocationHandler(clazz.qualifiedName!!, injector, members)) as T
}

internal actual fun <K, V> createConcurrentMutableMap(): MutableMap<K, V> {
	return ConcurrentHashMap()
}

internal actual fun <E> createConcurrentMutableCollection(): MutableCollection<E> {
	return ConcurrentLinkedQueue()
}