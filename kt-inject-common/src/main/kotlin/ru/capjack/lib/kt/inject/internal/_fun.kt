package ru.capjack.lib.kt.inject.internal

import kotlin.reflect.KClass

internal expect fun <T : Any> createProxyFactory(injector: InjectorImpl, clazz: KClass<T>, members: List<ProxyFactoryMember>): T

internal expect fun <K, V> createConcurrentMutableMap(): MutableMap<K, V>

internal expect fun <E> createConcurrentMutableCollection(): MutableCollection<E>