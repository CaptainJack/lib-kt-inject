package ru.capjack.tool.depin.internal

import ru.capjack.tool.depin.Injector
import ru.capjack.tool.depin.TypedName
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

internal class Registry {
	private val classBindings = createConcurrentMutableMap<KClass<out Any>, Binding<out Any>>()
	private val nameBindings = createConcurrentMutableMap<TypedName<out Any>, Binding<out Any>>()
	private val smartClassProducers = createConcurrentMutableCollection<Injector.(KClass<out Any>) -> Any?>()
	private val smartParameterProducers = createConcurrentMutableCollection<Injector.(KParameter) -> Any?>()
	
	fun hasBinding(clazz: KClass<out Any>): Boolean {
		return classBindings.containsKey(clazz)
	}
	
	fun hasBinding(name: TypedName<out Any>): Boolean {
		return nameBindings.containsKey(name)
	}
	
	fun setBinding(clazz: KClass<out Any>, binding: Binding<out Any>) {
		classBindings[clazz] = binding
	}
	
	fun setBinding(name: TypedName<out Any>, binding: Binding<out Any>) {
		nameBindings[name] = binding
	}
	
	fun addSmartProducerForClass(producer: Injector.(KClass<out Any>) -> Any?) {
		smartClassProducers.add(producer)
	}
	
	fun addSmartProducerForParameter(producer: Injector.(KParameter) -> Any?) {
		smartParameterProducers.add(producer)
	}
	
	fun <T : Any> getBinding(clazz: KClass<T>): Binding<T>? {
		@Suppress("UNCHECKED_CAST")
		return classBindings[clazz] as Binding<T>?
	}
	
	fun <T : Any> getBinding(name: TypedName<T>): Binding<T>? {
		@Suppress("UNCHECKED_CAST")
		return nameBindings[name] as Binding<T>?
	}
	
	fun tryProduce(injector: Injector, clazz: KClass<out Any>): Any? {
		for (producer in smartClassProducers) {
			producer(injector, clazz)?.let {
				return it
			}
		}
		return null
	}
	
	fun tryProduce(injector: Injector, parameter: KParameter): Any? {
		for (producer in smartParameterProducers) {
			producer(injector, parameter)?.let {
				return it
			}
		}
		return null
	}
}