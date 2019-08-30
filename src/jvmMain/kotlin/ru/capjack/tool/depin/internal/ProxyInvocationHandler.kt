package ru.capjack.tool.depin.internal

import ru.capjack.tool.depin.InjectException
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import kotlin.reflect.jvm.javaMethod

internal class ProxyInvocationHandler(
	private val typeName: String,
	private val injector: InjectorImpl,
	members: List<ProxyFactoryMember>
) : InvocationHandler {
	
	private val methods = members.associate {
		it.member.javaMethod!! to MethodInvocation(it)
	}
	
	override fun invoke(proxy: Any, method: Method, args: Array<Any>?): Any {
		return when (val m = methods[method]) {
			null -> when (method.name) {
				"toString" -> "$typeName\$Proxy"
				"equals"   -> proxy === args!![0]
				"hashCode" -> hashCode()
				else       -> throw InjectException("Unsupported method $method")
			}
			else -> m.invoke(args ?: emptyArray())
		}
	}
	
	private inner class MethodInvocation(method: ProxyFactoryMember) {
		
		private val clazz = method.target
		private val arguments = method.compileArguments()
		
		fun invoke(receivedArgs: Array<Any>): Any {
			return injector.produce(clazz, arguments, receivedArgs)
		}
	}
	
}