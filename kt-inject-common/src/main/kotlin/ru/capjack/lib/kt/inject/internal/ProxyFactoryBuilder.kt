package ru.capjack.lib.kt.inject.internal

import ru.capjack.lib.kt.inject.Binder
import ru.capjack.lib.kt.inject.InjectDelegate
import ru.capjack.lib.kt.inject.InjectException
import ru.capjack.lib.kt.reflect.findAnnotation
import ru.capjack.lib.kt.reflect.kClass
import ru.capjack.lib.kt.reflect.publicDeclaredMembers
import ru.capjack.lib.kt.reflect.returnTypeRef
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

internal class ProxyFactoryBuilder<T : Any>(
	private val clazz: KClass<T>
) : Binder.ProxyFactory {
	
	private var members: List<ProxyFactoryMember> = clazz.publicDeclaredMembers.map {
		if (it is KFunction<*>)
			ProxyFactoryMember(clazz, it, it.findAnnotation<InjectDelegate>()?.type ?: it.returnTypeRef.kClass)
		else
			throw RuntimeException("Only function allowed for proxy")
	}
	
	override fun <T : Any> delegate(clazz: KClass<T>, delegate: KClass<out T>): Binder.ProxyFactory {
		val memberProxy = members.find { it.member.returnTypeRef.kClass == clazz }
			?: throw InjectException("Member for return type ${this.clazz} not found in type ${this.clazz}")
		memberProxy.target = delegate
		return this
	}
	
	fun build(injector: InjectorImpl): T {
		return createProxyFactory(injector, clazz, members)
	}
}