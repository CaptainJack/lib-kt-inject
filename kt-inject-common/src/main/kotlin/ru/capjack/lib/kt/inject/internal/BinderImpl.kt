package ru.capjack.lib.kt.inject.internal

import ru.capjack.lib.kt.inject.Binder
import ru.capjack.lib.kt.inject.InjectException
import ru.capjack.lib.kt.inject.Injector
import ru.capjack.lib.kt.inject.TypedName
import ru.capjack.lib.kt.inject.internal.bindings.DelegateBinding
import ru.capjack.lib.kt.inject.internal.bindings.InstanceBinding
import ru.capjack.lib.kt.inject.internal.bindings.ProducerBinding
import ru.capjack.lib.kt.inject.internal.bindings.ReplaceBindingNamed
import ru.capjack.lib.kt.inject.internal.bindings.ReplaceBindingTyped
import kotlin.reflect.KClass
import kotlin.reflect.KFunction1
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
		}
		else {
			bindProducer(clazz) { get(delegate) }
		}
	}
	
	override fun <T : Any> bindProducer(clazz: KClass<T>, producer: Injector.() -> T) {
		check(clazz)
		injector.registry.setBinding(clazz, ReplaceBindingTyped(clazz, injector, producer))
	}
	
	override fun <T : Any> bindWeakDelegate(clazz: KClass<T>, delegate: KClass<out T>) {
		check(clazz)
		if (clazz == delegate) {
			bindWeakProducer(clazz) { injector.make(delegate) }
		}
		else {
			injector.registry.setBinding(clazz, DelegateBinding(injector, delegate))
		}
	}
	
	override fun <T : Any> bindWeakProducer(clazz: KClass<T>, producer: Injector.() -> T) {
		check(clazz)
		injector.registry.setBinding(clazz, ProducerBinding(injector, producer))
	}
	
	
	override fun <T : Any> bind(name: TypedName<T>, instance: T) {
		check(name)
		injector.registry.setBinding(name, InstanceBinding(instance))
	}
	
	override fun <T : Any> bindDelegate(name: TypedName<T>, delegate: KClass<out T>) {
		bindProducer(name) { get(delegate) }
	}
	
	override fun <T : Any> bindProducer(name: TypedName<T>, producer: Injector.() -> T) {
		check(name)
		injector.registry.setBinding(name, ReplaceBindingNamed(name, injector, producer))
	}
	
	override fun <T : Any> bindWeakDelegate(name: TypedName<T>, delegate: KClass<out T>) {
		check(name)
		injector.registry.setBinding(name, DelegateBinding(injector, delegate))
	}
	
	override fun <T : Any> bindWeakProducer(name: TypedName<T>, producer: Injector.() -> T) {
		check(name)
		injector.registry.setBinding(name, ProducerBinding(injector, producer))
	}
	
	
	override fun <T : Any> bindProxyFactory(factoryClass: KClass<T>, init: Binder.ProxyFactory.() -> Unit) {
		check(factoryClass)
		injector.registry.setBinding(factoryClass, ReplaceBindingTyped(factoryClass, injector, createProxyFactory(factoryClass, init)))
	}
	
	override fun <T : Any> bindWeakProxyFactory(factoryClass: KClass<T>, init: Binder.ProxyFactory.() -> Unit) {
		check(factoryClass)
		injector.registry.setBinding(factoryClass, ProducerBinding(injector, createProxyFactory(factoryClass, init)))
	}
	
	override fun <T : Any> bindProxyFactory(name: TypedName<T>, factoryClass: KClass<out T>, init: Binder.ProxyFactory.() -> Unit) {
		check(name)
		injector.registry.setBinding(name, ReplaceBindingNamed(name, injector, createProxyFactory(factoryClass, init)))
	}
	
	override fun <T : Any> bindWeakProxyFactory(name: TypedName<T>, factoryClass: KClass<out T>, init: Binder.ProxyFactory.() -> Unit) {
		check(name)
		injector.registry.setBinding(name, ProducerBinding(injector, createProxyFactory(factoryClass, init)))
	}
	
	private fun <T : Any> createProxyFactory(factoryClass: KClass<T>, init: Binder.ProxyFactory.() -> Unit): KFunction1<InjectorImpl, T> {
		return ProxyFactoryBuilder(factoryClass).apply(init)::build
	}
	
	
	override fun registerSmartProducerForClass(producer: Injector.(KClass<out Any>) -> Any?) {
		injector.registry.addSmartProducerForClass(producer)
	}
	
	override fun registerSmartProducerForParameter(producer: Injector.(KParameter) -> Any?) {
		injector.registry.addSmartProducerForParameter(producer)
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