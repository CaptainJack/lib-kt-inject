package ru.capjack.kt.inject

import kotlin.reflect.KClass
import kotlin.reflect.KParameter

interface Binder {
	fun <T : Any> bind(clazz: KClass<T>, instance: T)
	
	fun <T : Any> bindDelegate(clazz: KClass<T>, delegate: KClass<out T>)
	
	fun <T : Any> bindProducer(clazz: KClass<T>, producer: Injector.() -> T)
	
	fun <T : Any> bindWeakDelegate(clazz: KClass<T>, delegate: KClass<out T>)
	
	fun <T : Any> bindWeakProducer(clazz: KClass<T>, producer: Injector.() -> T)
	
	
	fun <T : Any> bind(name: TypedName<T>, instance: T)
	
	fun <T : Any> bindDelegate(name: TypedName<T>, delegate: KClass<out T>)
	
	fun <T : Any> bindProducer(name: TypedName<T>, producer: Injector.() -> T)
	
	fun <T : Any> bindWeakDelegate(name: TypedName<T>, delegate: KClass<out T>)
	
	fun <T : Any> bindWeakProducer(name: TypedName<T>, producer: Injector.() -> T)
	
	
	fun <T : Any> bindProxyFactory(factoryClass: KClass<T>, init: ProxyFactory.() -> Unit = {})
	
	fun <T : Any> bindWeakProxyFactory(factoryClass: KClass<T>, init: ProxyFactory.() -> Unit = {})
	
	
	fun <T : Any> bindProxyFactory(name: TypedName<T>, factoryClass: KClass<out T>, init: ProxyFactory.() -> Unit = {})
	
	fun <T : Any> bindWeakProxyFactory(name: TypedName<T>, factoryClass: KClass<out T>, init: ProxyFactory.() -> Unit = {})
	
	
	fun registerSmartProducerForClass(producer: Injector.(KClass<out Any>) -> Any?)
	
	fun registerSmartProducerForParameter(producer: Injector.(KParameter) -> Any?)
	
	
	interface ProxyFactory {
		fun <T : Any> delegate(clazz: KClass<T>, delegate: KClass<out T>): ProxyFactory
	}
}