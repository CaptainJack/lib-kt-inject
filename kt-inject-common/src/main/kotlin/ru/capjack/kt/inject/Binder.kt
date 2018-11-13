package ru.capjack.kt.inject

import kotlin.reflect.KClass
import kotlin.reflect.KParameter

interface Binder {
	fun <T : Any> bindInstance(clazz: KClass<T>, instance: T)
	
	fun <T : Any> bind(clazz: KClass<T>, delegate: KClass<out T>)
	
	fun <T : Any> bind(clazz: KClass<T>, producer: () -> T)
	
	fun <T : Any> bindInjected(clazz: KClass<T>, producer: Injector.() -> T)
	
	fun <T : Any> bindSupplier(clazz: KClass<T>, delegate: KClass<out T>)
	
	fun <T : Any> bindSupplier(clazz: KClass<T>, producer: () -> T)
	
	fun <T : Any> bindSupplierInjected(clazz: KClass<T>, function: Injector.() -> T)
	
	
	fun <T : Any> bindInstance(name: TypedName<T>, instance: T)
	
	fun <T : Any> bind(name: TypedName<T>, delegate: KClass<out T>)
	
	fun <T : Any> bind(name: TypedName<T>, producer: () -> T)
	
	fun <T : Any> bindInjected(name: TypedName<T>, producer: Injector.() -> T)
	
	fun <T : Any> bindSupplier(name: TypedName<T>, delegate: KClass<out T>)
	
	fun <T : Any> bindSupplier(name: TypedName<T>, producer: () -> T)
	
	fun <T : Any> bindSupplierInjected(name: TypedName<T>, producer: Injector.() -> T)
	
	
	fun <T : Any> bindProxy(clazz: KClass<T>, init: Factory.() -> Unit = {})
	
	fun <T : Any> bindProxySupplier(clazz: KClass<T>, init: Factory.() -> Unit = {})
	
	
	fun <T : Any> bindProxy(name: TypedName<T>, clazz: KClass<out T>, init: Factory.() -> Unit = {})
	
	fun <T : Any> bindProxySupplier(name: TypedName<T>, clazz: KClass<out T>, init: Factory.() -> Unit = {})
	
	
	fun registerSmartProducerForClass(producer: (KClass<out Any>) -> Any?)
	
	fun registerSmartProducerForClassInjected(producer: Injector.(KClass<out Any>) -> Any?)
	
	fun registerSmartProducerForParameter(producer: (KParameter) -> Any?)
	
	fun registerSmartProducerForParameterInjected(producer: Injector.(KParameter) -> Any?)
	
	
	interface Factory {
		fun <T : Any> bind(clazz: KClass<T>, delegate: KClass<out T>): Factory
	}
}