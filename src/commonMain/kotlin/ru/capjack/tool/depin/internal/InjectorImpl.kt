package ru.capjack.tool.depin.internal

import ru.capjack.tool.depin.Bind
import ru.capjack.tool.depin.Implementation
import ru.capjack.tool.depin.InjectException
import ru.capjack.tool.depin.Injector
import ru.capjack.tool.depin.Named
import ru.capjack.tool.depin.NamedType
import ru.capjack.tool.depin.Proxy
import ru.capjack.tool.depin.internal.bindings.InstanceBinding
import ru.capjack.tool.depin.internal.bindings.MemberBinding
import ru.capjack.tool.logging.Logging
import ru.capjack.tool.logging.getLogger
import ru.capjack.tool.logging.trace
import ru.capjack.tool.reflect.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

internal class InjectorImpl : Injector {
	
	val registry = Registry()
	
	private val logger = Logging.getLogger<Injector>()
	private val producingStack = ProducingStack()
	
	override fun <T : Any> get(clazz: KClass<T>): T {
		logger.trace { "Getting '$clazz'" }
		
		val binding = registry.getBinding(clazz)
		
		return binding
			?.run { get() }
			?: make(clazz)
	}
	
	override fun <T : Any> get(name: NamedType<T>): T {
		logger.trace { "Getting '$name'" }
		
		val binding = registry.getBinding(name) ?: throw InjectException("Binding for '$name' is not defined")
		
		return binding.get()
	}
	
	override fun get(parameter: KParameter): Any {
		val name = parameter.name!!
		val type = parameter.typeRef.kClass
		
		parameter.findAnnotation<Named>()?.let {
			val n = if (it.name.isEmpty())
				name
			else
				it.name
			logger.trace { "Supple parameter '$name' by name '$n'" }
			return get(NamedType(type, n))
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
		val binding = InstanceBinding(obj)
		
		clazz.findAnnotation<Bind>()?.also {
			if (it.name.isEmpty()) {
				logger.trace { "Bind self '$clazz'" }
				registry.setBinding(clazz, binding)
			}
			else {
				logger.trace { "Bind self '$clazz' by name '${it.name}'" }
				registry.setBinding(NamedType(clazz, it.name), binding)
			}
		}
		
		clazz.getSupertypesWithAnnotation<Bind>().forEach {
			//TODO Bind by name
			logger.trace { "Bind self '$clazz' as '$it'" }
			registry.setBinding(it, binding)
		}
		
		clazz.publicDeclaredMembers
			.filter { it.hasAnnotation<Bind>() }
			.forEach {
				val t = it.returnTypeRef.kClass
				val a = it.findAnnotation<Bind>()!!
				val b = MemberBinding<T>(obj, it)
				
				if (a.name.isEmpty()) {
					logger.trace { "Bind member '$clazz.${it.name}' as '$t'" }
					registry.setBinding(t, b)
				}
				else {
					logger.trace { "Bind member '$clazz.${it.name}' as '$t' by name '${a.name}'" }
					registry.setBinding(NamedType(t, a.name), b)
				}
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
			return ProxyFactoryBuilder(clazz).build(this)
		}
		
		registry.trySmartProduce(this, clazz)?.let {
			logger.trace { "Supplied '$clazz' from smart producer" }
			@Suppress("UNCHECKED_CAST")
			return it as T
		}
		
		return produce(clazz)
	}
	
	fun <T : Any> produce(clazz: KClass<T>): T {
		return produce(clazz) {
			it.valueParameters.map(::get)
		}
	}
	
	fun <T : Any> produce(clazz: KClass<T>, argFactories: List<ProxyFactoryMemberArgument>, incomingArgs: Array<*>): T {
		return produce(clazz) {
			argFactories.map { it.invoke(this, incomingArgs) }
		}
	}
	
	private inline fun <T : Any> produce(clazz: KClass<T>, argsExtractor: (KFunction<T>) -> List<*>): T {
		logger.trace { "Produce '$clazz'" }
		
		clazz.checkClassInjectable()
		
		val circular = producingStack.contains(clazz)
		producingStack.add(clazz)
		
		if (circular) {
			throw InjectException("Circular dependency:\n" + producingStack.joinToString("\n") { " - $it" })
		}
		
		registry.observeProduce(clazz)
		
		val constructor = clazz.primaryConstructor!!
		val args = argsExtractor(constructor)
		
		val obj = constructor.callRef(*args.toTypedArray())
		
		producingStack.remove()
		registry.observeProduce(clazz, obj)
		
		return obj
	}
}