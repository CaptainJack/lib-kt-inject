package ru.capjack.tool.depin.internal

import ru.capjack.tool.depin.InjectException
import ru.capjack.tool.depin.Injector
import ru.capjack.tool.reflect.KParameter
import ru.capjack.tool.reflect.qualifiedNameRef
import ru.capjack.tool.reflect.valueParameters
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

internal typealias ProxyFactoryMemberArgument = (Injector, Array<*>) -> Any?

internal class ProxyFactoryMember(
	var clazz: KClass<*>,
	var member: KFunction<*>,
	var target: KClass<*>
) {
	
	fun compileArguments(): List<ProxyFactoryMemberArgument> {
		
		target.checkClassInjectable()
		
		val classParameters = target.extractPrimaryConstructor().valueParameters
		val memberParameters = member.valueParameters.toMutableList()
		
		val args = classParameters.map { classParameter ->
			val suitableMemberParameters = memberParameters.filter {
				it.type == classParameter.type
			}
			
			if (suitableMemberParameters.isNotEmpty()) {
				val memberParameter =
					if (suitableMemberParameters.size == 1) suitableMemberParameters[0]
					else suitableMemberParameters.find { it.name == classParameter.name } ?: suitableMemberParameters[0]
				
				memberParameters.remove(memberParameter)
				
				createReceivableArgument(memberParameter.index - 1)
			}
			else {
				createInjectableArgument(classParameter)
			}
		}
		
		if (memberParameters.isNotEmpty()) {
			throw InjectException("Failed to associate value parameters of member '${clazz.qualifiedNameRef}:${member.name}' to class constructor '$target'")
		}
		
		return args
	}
	
	private fun createReceivableArgument(index: Int): ProxyFactoryMemberArgument {
		return { _, args -> args[index] }
	}
	
	private fun createInjectableArgument(parameter: KParameter): ProxyFactoryMemberArgument {
		return { injector, _ -> injector.get(parameter) }
	}
}