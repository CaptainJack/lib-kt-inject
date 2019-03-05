package ru.capjack.tool.kt.inject.internal

import ru.capjack.tool.kt.inject.InjectException
import ru.capjack.tool.kt.inject.Injector
import ru.capjack.tool.kt.reflect.indexRef
import ru.capjack.tool.kt.reflect.qualifiedNameRef
import ru.capjack.tool.kt.reflect.typeRef
import ru.capjack.tool.kt.reflect.valueParameters
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

internal typealias ProxyFactoryMemberArgument = (Injector, Array<out Any>) -> Any

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
				it.typeRef == classParameter.typeRef
			}
			
			if (suitableMemberParameters.isNotEmpty()) {
				val memberParameter =
					if (suitableMemberParameters.size == 1) suitableMemberParameters[0]
					else suitableMemberParameters.find { it.name == classParameter.name } ?: suitableMemberParameters[0]
				
				memberParameters.remove(memberParameter)
				
				createReceivableArgument(memberParameter.indexRef - 1)
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