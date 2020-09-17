package ru.capjack.tool.depin

import ru.capjack.tool.reflect.KParameter
import kotlin.reflect.KClass

interface Binder {
	fun <T : Any> bindInstance(clazz: KClass<T>, instance: T)
	
	fun <T : Any> bind(clazz: KClass<T>, implementation: KClass<out T>)
	
	fun <T : Any> bind(clazz: KClass<T>, producer: () -> T)
	
	fun <T : Any> bindInjected(clazz: KClass<T>, producer: Injector.() -> T)
	
	fun <T : Any> bindSupplier(clazz: KClass<T>, implementation: KClass<out T>)
	
	fun <T : Any> bindSupplier(clazz: KClass<T>, producer: () -> T)
	
	fun <T : Any> bindSupplierInjected(clazz: KClass<T>, function: Injector.() -> T)
	
	
	fun <T : Any> bindInstance(name: NamedType<T>, instance: T)
	
	fun <T : Any> bind(name: NamedType<T>, implementation: KClass<out T>)
	
	fun <T : Any> bind(name: NamedType<T>, producer: () -> T)
	
	fun <T : Any> bindInjected(name: NamedType<T>, producer: Injector.() -> T)
	
	fun <T : Any> bindSupplier(name: NamedType<T>, implementation: KClass<out T>)
	
	fun <T : Any> bindSupplier(name: NamedType<T>, producer: () -> T)
	
	fun <T : Any> bindSupplierInjected(name: NamedType<T>, producer: Injector.() -> T)
	
	
	fun <T : Any> bindProxy(clazz: KClass<T>, init: Proxy.() -> Unit = {})
	
	fun <T : Any> bindProxySupplier(clazz: KClass<T>, init: Proxy.() -> Unit = {})
	
	
	fun <T : Any> bindProxy(name: NamedType<T>, clazz: KClass<out T>, init: Proxy.() -> Unit = {})
	
	fun <T : Any> bindProxySupplier(name: NamedType<T>, clazz: KClass<out T>, init: Proxy.() -> Unit = {})
	
	
	fun addSmartProducerForClass(producer: (KClass<*>) -> Any?)
	
	fun addSmartProducerForClassInjected(producer: Injector.(KClass<*>) -> Any?)
	
	fun addSmartProducerForParameter(producer: (KParameter) -> Any?)
	
	fun addSmartProducerForParameterInjected(producer: Injector.(KParameter) -> Any?)
	
	
	fun addProduceObserverBefore(observer: (KClass<*>) -> Unit)
	
	fun addProduceObserverAfter(observer: (KClass<*>, Any) -> Unit)
	
	
	interface Proxy {
		fun <T : Any> bind(clazz: KClass<T>, implementation: KClass<out T>): Proxy
	}
}