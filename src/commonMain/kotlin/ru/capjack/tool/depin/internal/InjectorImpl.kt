package ru.capjack.tool.depin.internal

import ru.capjack.tool.depin.Bind
import ru.capjack.tool.depin.Implementation
import ru.capjack.tool.depin.Name
import ru.capjack.tool.depin.Proxy
import ru.capjack.tool.depin.Injector
import ru.capjack.tool.depin.TypedName
import ru.capjack.tool.depin.internal.bindings.InstanceBinding
import ru.capjack.tool.depin.internal.bindings.MemberBinding
import ru.capjack.tool.logging.Logging
import ru.capjack.tool.logging.getLogger
import ru.capjack.tool.logging.trace
import ru.capjack.tool.reflect.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

internal class InjectorImpl : Injector {
	
	val registry = Registry()
	
	private val logger = Logging.getLogger<Injector>()
	
	override fun <T : Any> get(clazz: KClass<T>): T {
		logger.trace { "Getting '$clazz'" }
		
		val binding = registry.getBinding(clazz)
		
		return binding
			?.run { get() }
			?: make(clazz)
	}
	
	override fun <T : Any> get(name: TypedName<T>): T {
		logger.trace { "Getting '$name'" }
		
		val binding = registry.getBinding(name) ?: throw NoSuchElementException("Binding for '$name' is not defined")
		
		return binding.get()
	}
	
	override fun get(parameter: KParameter): Any {
		val name = parameter.name!!
		val type = parameter.typeRef.kClass
		
		parameter.findAnnotation<Name>()?.let {
			logger.trace { "Supple parameter '$name' by name" }
			val n = if (it.name.isEmpty())
				name
			else
				it.name
			return get(TypedName(type, n))
		}
		
		registry.trySmartProduce(this, parameter)?.let {
			logger.trace { "Supplied parameter '$name' from smart producer" }
			return it
		}
		
		logger.trace { "Supple parameter '$name' from injector" }
		
		return get(type)
	}
	
	fun <T : Any> make(clazz: KClass<T>): T {
		val obj = supple(clazz)
		
		if (clazz.hasAnnotation<Bind>()) {
			logger.trace { "Bind self '$clazz'" }
			registry.setBinding(clazz, InstanceBinding(obj))
		}
		
		clazz.publicDeclaredMembers
			.filter { it.hasAnnotation<Bind>() }
			.forEach {
				val t = it.returnTypeRef.kClass
				logger.trace { "Bind member '$clazz.${it.name}' as '$t'" }
				registry.setBinding(t, MemberBinding<T>(obj, it))
			}
		
		clazz.getSupertypesWithAnnotation<Bind>().forEach {
			logger.trace { "Bind self '$clazz' as '$it'" }
			registry.setBinding(it, InstanceBinding(obj))
		}
		
		return obj
	}
	
	private fun <T : Any> supple(clazz: KClass<T>): T {
		
		clazz.findAnnotation<Implementation>()?.let {
			logger.trace { "Supple '$clazz' as delegate '${it.type}'" }
			
			@Suppress("UNCHECKED_CAST")
			return get(it.type) as T
		}
		
		if (clazz.hasAnnotation<Proxy>()) {
			logger.trace { "Supple '$clazz' as proxy factory" }
			return FactoryBuilder(clazz).build(this)
		}
		
		registry.trySmartProduce(this, clazz)?.let {
			logger.trace { "Supplied '$clazz' from smart producer" }
			@Suppress("UNCHECKED_CAST")
			return it as T
		}
		
		return produce(clazz)
	}
	
	fun <T : Any> produce(clazz: KClass<T>, withArgs: Array<Any>? = null): T {
		logger.trace { "Produce '$clazz'" }
		
		clazz.checkClassInjectable()
		
		registry.observeProduce(clazz)
		
		val constructor = clazz.primaryConstructor!!
		
		val args = withArgs
			?: constructor.valueParameters.map(::get).toTypedArray()
		
		val obj = constructor.callRef(*args)
		
		registry.observeProduce(clazz, obj)
		
		return obj
	}
}