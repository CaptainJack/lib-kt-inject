package ru.capjack.kt.inject.internal

import ru.capjack.kt.inject.Binder
import ru.capjack.kt.inject.InjectException
import ru.capjack.kt.inject.Injector
import ru.capjack.kt.inject.TypedName
import ru.capjack.kt.inject.internal.bindings.DelegateBinding
import ru.capjack.kt.inject.internal.bindings.InstanceBinding
import ru.capjack.kt.inject.internal.bindings.ProducerBinding
import ru.capjack.kt.inject.internal.bindings.ReplaceBindingNamed
import ru.capjack.kt.inject.internal.bindings.ReplaceBindingTyped
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

internal class BinderImpl(
	private val injector: InjectorImpl,
	private val strong: Boolean
) : Binder {
	
	override fun <T : Any> bind(clazz: KClass<T>, instance: T) {
		check(clazz)
		injector.registry.setBinding(clazz, InstanceBinding(instance))
	}
	
	override fun <T : Any> bindDelegate(clazz: KClass<T>, delegate: KClass<out T>) {
		if (clazz == delegate) {
			bindProducer(clazz) { injector.make(delegate) }
		} else {
			bindProducerInject(clazz) { it.get(delegate) }
		}
	}
	
	override fun <T : Any> bindProducer(clazz: KClass<T>, function: () -> T) {
		bindProducerInject(clazz) { function() }
	}
	
	override fun <T : Any> bindProducerInject(clazz: KClass<T>, function: (Injector) -> T) {
		check(clazz)
		injector.registry.setBinding(clazz, ReplaceBindingTyped(clazz, injector, function))
	}
	
	override fun <T : Any> bindSupplier(clazz: KClass<T>, delegate: KClass<out T>) {
		check(clazz)
		if (clazz == delegate) {
			bindSupplier(clazz) { injector.make(delegate) }
		} else {
			injector.registry.setBinding(clazz, DelegateBinding(injector, delegate))
		}
	}
	
	override fun <T : Any> bindSupplier(clazz: KClass<T>, function: () -> T) {
		bindSupplierInject(clazz) { function() }
	}
	
	override fun <T : Any> bindSupplierInject(clazz: KClass<T>, function: (Injector) -> T) {
		check(clazz)
		injector.registry.setBinding(clazz, ProducerBinding(injector, function))
	}
	
	
	override fun <T : Any> bind(name: TypedName<T>, instance: T) {
		check(name)
		injector.registry.setBinding(name, InstanceBinding(instance))
	}
	
	override fun <T : Any> bindDelegate(name: TypedName<T>, delegate: KClass<out T>) {
		bindProducerInject(name) { it.get(delegate) }
	}
	
	override fun <T : Any> bindProducer(name: TypedName<T>, function: () -> T) {
		bindProducerInject(name) { function() }
	}
	
	override fun <T : Any> bindProducerInject(name: TypedName<T>, function: (Injector) -> T) {
		check(name)
		injector.registry.setBinding(name, ReplaceBindingNamed(name, injector, function))
	}
	
	
	override fun <T : Any> bindSupplier(name: TypedName<T>, delegate: KClass<out T>) {
		check(name)
		injector.registry.setBinding(name, DelegateBinding(injector, delegate))
	}
	
	override fun <T : Any> bindSupplier(name: TypedName<T>, function: () -> T) {
		bindSupplierInject(name) { function() }
	}
	
	override fun <T : Any> bindSupplierInject(name: TypedName<T>, function: (Injector) -> T) {
		check(name)
		injector.registry.setBinding(name, ProducerBinding(injector, function))
	}
	
	
	override fun <T : Any> bindProxyFactory(factoryClass: KClass<T>, init: (Binder.ProxyFactory) -> Unit) {
		check(factoryClass)
		injector.registry.setBinding(factoryClass, ReplaceBindingTyped(factoryClass, injector, createProxyFactory(factoryClass, init)))
	}
	
	override fun <T : Any> bindProxyFactorySupplier(factoryClass: KClass<T>, init: (Binder.ProxyFactory) -> Unit) {
		check(factoryClass)
		injector.registry.setBinding(factoryClass, ProducerBinding(injector, createProxyFactory(factoryClass, init)))
	}
	
	override fun <T : Any> bindProxyFactory(name: TypedName<T>, factoryClass: KClass<out T>, init: (Binder.ProxyFactory) -> Unit) {
		check(name)
		injector.registry.setBinding(name, ReplaceBindingNamed(name, injector, createProxyFactory(factoryClass, init)))
	}
	
	override fun <T : Any> bindProxyFactorySupplier(name: TypedName<T>, factoryClass: KClass<out T>, init: (Binder.ProxyFactory) -> Unit) {
		check(name)
		injector.registry.setBinding(name, ProducerBinding(injector, createProxyFactory(factoryClass, init)))
	}
	
	private fun <T : Any> createProxyFactory(factoryClass: KClass<T>, init: (Binder.ProxyFactory) -> Unit): (InjectorImpl) -> T {
		return ProxyFactoryBuilder(factoryClass).apply(init)::build
	}
	
	
	override fun registerSmartProducerForClass(function: (KClass<out Any>) -> Any?) {
		registerSmartProducerForClassInject { _, it -> function(it) }
	}
	
	override fun registerSmartProducerForClassInject(function: (Injector, KClass<out Any>) -> Any?) {
		injector.registry.addSmartProducerForClass(function)
	}
	
	override fun registerSmartProducerForParameter(function: (KParameter) -> Any?) {
		registerSmartProducerForParameterInject { _, it -> function(it) }
	}
	
	override fun registerSmartProducerForParameterInject(function: (Injector, KParameter) -> Any?) {
		injector.registry.addSmartProducerForParameter(function)
	}
	
	
	private fun check(type: KClass<*>) {
		if (strong && injector.registry.hasBinding(type)) {
			throw InjectException("Type '$type' is already binded")
		}
	}
	
	private fun check(name: TypedName<*>) {
		if (strong && injector.registry.hasBinding(name)) {
			throw InjectException("Name '$name' is already binded")
		}
	}
}