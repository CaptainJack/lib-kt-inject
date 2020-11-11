package ru.capjack.tool.depin.internal

import ru.capjack.tool.depin.Injector
import ru.capjack.tool.depin.NamedType
import ru.capjack.tool.reflect.KParameter
import ru.capjack.tool.reflect.isSubclassOf
import kotlin.reflect.KClass

internal class Registry {
	private val classBindings = createConcurrentMutableMap<KClass<*>, Binding<*>>()
	private val nameBindings = createConcurrentMutableMap<NamedType<*>, Binding<*>>()
	private val smartClassProducers = createConcurrentMutableCollection<Injector.(KClass<*>) -> Any?>()
	private val smartParameterProducers = createConcurrentMutableCollection<Injector.(KParameter) -> Any?>()
	private val produceObserversBefore = createConcurrentMutableCollection<(KClass<*>) -> Unit>()
	private val produceObserversAfter = createConcurrentMutableCollection<(KClass<*>, Any) -> Unit>()
	
	
	fun hasBinding(clazz: KClass<*>): Boolean {
		return classBindings.containsKey(clazz)
	}
	
	fun hasBinding(name: NamedType<*>): Boolean {
		return nameBindings.containsKey(name)
	}
	
	fun setBinding(clazz: KClass<*>, binding: Binding<*>) {
		classBindings[clazz] = binding
	}
	
	fun setBinding(name: NamedType<*>, binding: Binding<*>) {
		nameBindings[name] = binding
	}
	
	fun addSmartProducerForClass(producer: Injector.(KClass<*>) -> Any?) {
		smartClassProducers.add(producer)
	}
	
	fun addSmartProducerForParameter(producer: Injector.(KParameter) -> Any?) {
		smartParameterProducers.add(producer)
	}
	
	fun addProduceObserver(observer: (KClass<*>) -> Unit) {
		produceObserversBefore.add(observer)
	}
	
	fun addProduceObserver(observer: (KClass<*>, Any) -> Unit) {
		produceObserversAfter.add(observer)
	}
	
	@Suppress("UNCHECKED_CAST")
	fun <T : Any> getBinding(clazz: KClass<T>): Binding<T>? {
		return classBindings[clazz] as Binding<T>?
	}
	
	@Suppress("UNCHECKED_CAST")
	fun <T : Any> getBinding(name: NamedType<T>): Binding<T>? {
		return nameBindings[name] as Binding<T>?
	}
	
	@Suppress("UNCHECKED_CAST")
	fun <T: Any> findBindingSimilar(name: String, clazz: KClass<T>): Binding<T>? {
		nameBindings.forEach {
			if (it.key.name == name && it.key.type.isSubclassOf(clazz)) {
				return it.value as Binding<T>
			}
		}
		classBindings.forEach {
			if (it.key.isSubclassOf(clazz)) {
				return it.value as Binding<T>
			}
		}
		return null
	}
	
	fun trySmartProduce(injector: Injector, clazz: KClass<*>): Any? {
		for (producer in smartClassProducers) {
			producer(injector, clazz)?.let {
				return it
			}
		}
		return null
	}
	
	fun trySmartProduce(injector: Injector, parameter: KParameter): Any? {
		for (producer in smartParameterProducers) {
			producer(injector, parameter)?.let {
				return it
			}
		}
		return null
	}
	
	fun observeProduce(clazz: KClass<*>) {
		produceObserversBefore.forEach { it(clazz) }
	}
	
	fun observeProduce(clazz: KClass<*>, obj: Any) {
		produceObserversAfter.forEach { it(clazz, obj) }
	}
}