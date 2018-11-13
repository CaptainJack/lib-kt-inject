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
	
	override fun <T : Any> bindInstance(clazz: KClass<T>, instance: T) {
		check(clazz)
		injector.registry.setBinding(clazz, InstanceBinding(instance))
	}
	
	override fun <T : Any> bind(clazz: KClass<T>, delegate: KClass<out T>) {
		if (clazz == delegate) {
			bind(clazz) { injector.make(delegate) }
		}
		else {
			bindInjected(clazz) { get(delegate) }
		}
	}
	
	override fun <T : Any> bind(clazz: KClass<T>, producer: () -> T) {
		bindInjected(clazz) { producer() }
	}
	
	override fun <T : Any> bindInjected(clazz: KClass<T>, producer: Injector.() -> T) {
		check(clazz)
		injector.registry.setBinding(clazz, ReplaceBindingTyped(clazz, injector, producer))
	}
	
	override fun <T : Any> bindSupplier(clazz: KClass<T>, delegate: KClass<out T>) {
		check(clazz)
		if (clazz == delegate) {
			bindSupplier(clazz) { injector.make(delegate) }
		}
		else {
			injector.registry.setBinding(clazz, DelegateBinding(injector, delegate))
		}
	}
	
	override fun <T : Any> bindSupplier(clazz: KClass<T>, producer: () -> T) {
		bindSupplierInjected(clazz) { producer() }
	}
	
	override fun <T : Any> bindSupplierInjected(clazz: KClass<T>, function: Injector.() -> T) {
		check(clazz)
		injector.registry.setBinding(clazz, ProducerBinding(injector, function))
	}
	
	
	override fun <T : Any> bindInstance(name: TypedName<T>, instance: T) {
		check(name)
		injector.registry.setBinding(name, InstanceBinding(instance))
	}
	
	override fun <T : Any> bind(name: TypedName<T>, delegate: KClass<out T>) {
		bindInjected(name) { get(delegate) }
	}
	
	override fun <T : Any> bind(name: TypedName<T>, producer: () -> T) {
		bindInjected(name) { producer() }
	}
	
	override fun <T : Any> bindInjected(name: TypedName<T>, producer: Injector.() -> T) {
		check(name)
		injector.registry.setBinding(name, ReplaceBindingNamed(name, injector, producer))
	}
	
	
	override fun <T : Any> bindSupplier(name: TypedName<T>, delegate: KClass<out T>) {
		check(name)
		injector.registry.setBinding(name, DelegateBinding(injector, delegate))
	}
	
	override fun <T : Any> bindSupplier(name: TypedName<T>, producer: () -> T) {
		bindSupplierInjected(name) { producer() }
	}
	
	override fun <T : Any> bindSupplierInjected(name: TypedName<T>, producer: Injector.() -> T) {
		check(name)
		injector.registry.setBinding(name, ProducerBinding(injector, producer))
	}
	
	
	override fun <T : Any> bindProxy(clazz: KClass<T>, init: (Binder.Factory) -> Unit) {
		check(clazz)
		injector.registry.setBinding(clazz, ReplaceBindingTyped(clazz, injector, createProxyFactory(clazz, init)))
	}
	
	override fun <T : Any> bindProxySupplier(clazz: KClass<T>, init: (Binder.Factory) -> Unit) {
		check(clazz)
		injector.registry.setBinding(clazz, ProducerBinding(injector, createProxyFactory(clazz, init)))
	}
	
	override fun <T : Any> bindProxy(name: TypedName<T>, clazz: KClass<out T>, init: (Binder.Factory) -> Unit) {
		check(name)
		injector.registry.setBinding(name, ReplaceBindingNamed(name, injector, createProxyFactory(clazz, init)))
	}
	
	override fun <T : Any> bindProxySupplier(name: TypedName<T>, clazz: KClass<out T>, init: (Binder.Factory) -> Unit) {
		check(name)
		injector.registry.setBinding(name, ProducerBinding(injector, createProxyFactory(clazz, init)))
	}
	
	private fun <T : Any> createProxyFactory(factoryClass: KClass<T>, init: (Binder.Factory) -> Unit): (InjectorImpl) -> T {
		return FactoryBuilder(factoryClass).apply(init)::build
	}
	
	
	override fun registerSmartProducerForClass(producer: (KClass<out Any>) -> Any?) {
		registerSmartProducerForClassInjected { it -> producer(it) }
	}
	
	override fun registerSmartProducerForClassInjected(producer: Injector.(KClass<out Any>) -> Any?) {
		injector.registry.addSmartProducerForClass(producer)
	}
	
	override fun registerSmartProducerForParameter(producer: (KParameter) -> Any?) {
		registerSmartProducerForParameterInjected { it -> producer(it) }
	}
	
	override fun registerSmartProducerForParameterInjected(producer: Injector.(KParameter) -> Any?) {
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