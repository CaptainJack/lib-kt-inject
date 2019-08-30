package ru.capjack.tool.depin.internal

import ru.capjack.tool.reflect.accessName
import ru.capjack.tool.reflect.qualifiedNameRef
import kotlin.reflect.KClass

private var proxyCounter: Int = 0

@Suppress("UNUSED_PARAMETER")
private fun createNamedEmptyFunction(name: String): dynamic {
	return js("new Function(\"return function \"+name+\"(){}\")()")
}

internal actual fun <T : Any> createProxyFactory(injector: InjectorImpl, clazz: KClass<T>, members: List<ProxyFactoryMember>): T {
	++proxyCounter
	val proxyClass = createNamedEmptyFunction((clazz.qualifiedNameRef?.let { "_" + it.replace('.', '_') } ?: "") + "\$Proxy" + proxyCounter)
	
	proxyClass.prototype.toString = fun(): String {
		return js("this.constructor.name").unsafeCast<String>()
	}
	
	members.forEach { m ->
		val t = m.target
		val a = m.compileArguments()
		proxyClass.prototype[m.member.accessName] =
			if (a.isEmpty()) {
				{ injector.create(t, emptyArray()) }
			}
			else {
				{ injector.create(t, a.map { it.invoke(injector, js("arguments").unsafeCast<Array<Any>>()) }.toTypedArray()) }
			}
	}
	
	return js("new proxyClass()").unsafeCast<T>()
}

internal actual fun <K, V> createConcurrentMutableMap(): MutableMap<K, V> {
	return mutableMapOf()
}

internal actual fun <E> createConcurrentMutableCollection(): MutableCollection<E> {
	return mutableListOf()
}