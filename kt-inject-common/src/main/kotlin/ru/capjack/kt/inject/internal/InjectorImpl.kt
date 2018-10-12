package ru.capjack.kt.inject.internal

import ru.capjack.kt.inject.InjectBind
import ru.capjack.kt.inject.InjectDelegate
import ru.capjack.kt.inject.InjectName
import ru.capjack.kt.inject.InjectProxyFactory
import ru.capjack.kt.inject.Injector
import ru.capjack.kt.inject.TypedName
import ru.capjack.kt.inject.internal.bindings.InstanceBinding
import ru.capjack.kt.inject.internal.bindings.MemberBinding
import ru.capjack.kt.logging.Logging
import ru.capjack.kt.logging.getLogger
import ru.capjack.kt.logging.trace
import ru.capjack.kt.reflect.*
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
	
	
	fun <T : Any> make(clazz: KClass<T>): T {
		val obj = supple(clazz)
		
		if (clazz.hasAnnotation<InjectBind>()) {
			logger.trace { "Bind self '$clazz'" }
			registry.setBinding(clazz, InstanceBinding(obj))
		}
		
		clazz.publicDeclaredMembers
			.filter { it.hasAnnotation<InjectBind>() }
			.forEach {
				val t = it.returnTypeRef.kClass
				logger.trace { "Bind member '$clazz.${it.name}' as '$t'" }
				registry.setBinding(t, MemberBinding<T>(obj, it))
			}
		
		clazz.getSupertypesWithAnnotation<InjectBind>().forEach {
			logger.trace { "Bind self '$clazz' as '$it'" }
			registry.setBinding(it, InstanceBinding(obj))
		}
		
		return obj
	}
	
	private fun <T : Any> supple(clazz: KClass<T>): T {
		
		clazz.findAnnotation<InjectDelegate>()?.let {
			logger.trace { "Supple '$clazz' as delegate '${it.type}'" }
			
			@Suppress("UNCHECKED_CAST")
			return get(it.type) as T
		}
		
		if (clazz.hasAnnotation<InjectProxyFactory>()) {
			logger.trace { "Supple '$clazz' as proxy factory" }
			return ProxyFactoryBuilder(clazz).build(this)
		}
		
		registry.tryProduce(this, clazz)?.let {
			logger.trace { "Supplied '$clazz' from smart producer" }
			@Suppress("UNCHECKED_CAST")
			return it as T
		}
		
		return create(clazz)
	}
	
	fun suppleParameter(parameter: KParameter): Any {
		val name = parameter.name!!
		val type = parameter.typeRef.kClass
		
		parameter.findAnnotation<InjectName>()?.let {
			logger.trace { "Supple parameter '$name' by name" }
			val n = if (it.name == "_")
				name
			else
				it.name
			return get(TypedName(type, n))
		}
		
		registry.tryProduce(this, parameter)?.let {
			logger.trace { "Supplied parameter '$name' from smart producer" }
			return it
		}
		
		logger.trace { "Supple parameter '$name' from injector" }
		
		return get(type)
	}
	
	fun <T : Any> create(clazz: KClass<T>, withArgs: Array<Any>? = null): T {
		logger.trace { "Creating '$clazz'" }
		
		clazz.checkClassInjectable()
		
		val constructor = clazz.primaryConstructor!!
		
		val args = withArgs
			?: constructor.valueParameters.map { suppleParameter(it) }.toTypedArray()
		
		return constructor.callRef(*args)
	}
}