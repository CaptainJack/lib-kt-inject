package ru.capjack.kt.inject.internal

import ru.capjack.kt.inject.Binder
import ru.capjack.kt.inject.InjectImplementation
import ru.capjack.kt.inject.InjectException
import ru.capjack.kt.reflect.findAnnotation
import ru.capjack.kt.reflect.kClass
import ru.capjack.kt.reflect.publicDeclaredMembers
import ru.capjack.kt.reflect.returnTypeRef
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

internal class FactoryBuilder<T : Any>(
	private val clazz: KClass<T>
) : Binder.Factory {
	
	private var members: List<ProxyFactoryMember> = clazz.publicDeclaredMembers.map {
		if (it is KFunction<*>)
			ProxyFactoryMember(clazz, it, it.findAnnotation<InjectImplementation>()?.type ?: it.returnTypeRef.kClass)
		else
			throw RuntimeException("Only function allowed for proxy")
	}
	
	override fun <T : Any> bind(clazz: KClass<T>, implementation: KClass<out T>): Binder.Factory {
		val memberProxy = members.find { it.member.returnTypeRef.kClass == clazz }
			?: throw InjectException("Member for return type ${this.clazz} not found in type ${this.clazz}")
		memberProxy.target = implementation
		return this
	}
	
	fun build(injector: InjectorImpl): T {
		return createProxyFactory(injector, clazz, members)
	}
}