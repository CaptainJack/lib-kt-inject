package ru.capjack.kt.inject

import kotlin.reflect.KClass
import kotlin.reflect.KParameter

interface Binder {
	fun <T : Any> bind(clazz: KClass<T>, instance: T)
	
	fun <T : Any> bindDelegate(clazz: KClass<T>, delegate: KClass<out T>)
	
	fun <T : Any> bindProducer(clazz: KClass<T>, function: () -> T)
	
	fun <T : Any> bindProducerInject(clazz: KClass<T>, function: Injector.() -> T)
	
	fun <T : Any> bindSupplier(clazz: KClass<T>, delegate: KClass<out T>)
	
	fun <T : Any> bindSupplier(clazz: KClass<T>, function: () -> T)
	
	fun <T : Any> bindSupplierInject(clazz: KClass<T>, function: Injector.() -> T)
	
	
	fun <T : Any> bind(name: TypedName<T>, instance: T)
	
	fun <T : Any> bindDelegate(name: TypedName<T>, delegate: KClass<out T>)
	
	fun <T : Any> bindProducer(name: TypedName<T>, function: () -> T)
	
	fun <T : Any> bindProducerInject(name: TypedName<T>, function: Injector.() -> T)
	
	fun <T : Any> bindSupplier(name: TypedName<T>, delegate: KClass<out T>)
	
	fun <T : Any> bindSupplier(name: TypedName<T>, function: () -> T)
	
	fun <T : Any> bindSupplierInject(name: TypedName<T>, function: Injector.() -> T)
	
	
	fun <T : Any> bindProxyFactory(factoryClass: KClass<T>, init: ProxyFactory.() -> Unit = {})
	
	fun <T : Any> bindProxyFactorySupplier(factoryClass: KClass<T>, init: ProxyFactory.() -> Unit = {})
	
	
	fun <T : Any> bindProxyFactory(name: TypedName<T>, factoryClass: KClass<out T>, init: ProxyFactory.() -> Unit = {})
	
	fun <T : Any> bindProxyFactorySupplier(name: TypedName<T>, factoryClass: KClass<out T>, init: ProxyFactory.() -> Unit = {})
	
	
	fun registerSmartProducerForClass(function: (KClass<out Any>) -> Any?)
	
	fun registerSmartProducerForClassInject(function: Injector.(KClass<out Any>) -> Any?)
	
	fun registerSmartProducerForParameter(function: (KParameter) -> Any?)
	
	fun registerSmartProducerForParameterInject(function: Injector.(KParameter) -> Any?)
	
	
	interface ProxyFactory {
		fun <T : Any> delegate(clazz: KClass<T>, delegate: KClass<out T>): ProxyFactory
	}
}