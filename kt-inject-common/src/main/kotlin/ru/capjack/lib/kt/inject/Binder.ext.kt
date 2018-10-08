package ru.capjack.lib.kt.inject

import ru.capjack.lib.kt.reflect.findAnnotation
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

inline fun <reified T : Any> Binder.bind(instance: T) =
	bind(T::class, instance)

inline fun <reified T : Any, reified D : T> Binder.bindDelegate() =
	bindDelegate(T::class, D::class)

inline fun <reified T : Any> Binder.bindProducer(noinline producer: Injector.() -> T) =
	bindProducer(T::class, producer)

inline fun <reified T : Any, reified D : T> Binder.bindWeakDelegate() =
	bindWeakDelegate(T::class, D::class)

inline fun <reified T : Any> Binder.bindWeakProducer(noinline factory: Injector.() -> T) =
	bindWeakProducer(T::class, factory)


inline fun <T : Any, reified D : T> Binder.bindDelegate(name: TypedName<T>) =
	bindDelegate(name, D::class)

inline fun <T : Any, reified D : T> Binder.bindWeakDelegate(name: TypedName<T>) =
	bindWeakDelegate(name, D::class)


inline fun <reified T : Any> Binder.bindProxyFactory(noinline init: Binder.ProxyFactory.() -> Unit = {}) =
	bindProxyFactory(T::class, init)

inline fun <reified T : Any> Binder.bindWeakProxyFactory(noinline init: Binder.ProxyFactory.() -> Unit = {}) =
	bindWeakProxyFactory(T::class, init)

inline fun <T : Any, reified F : T> Binder.bindProxyFactory(name: TypedName<T>, noinline init: Binder.ProxyFactory.() -> Unit = {}) =
	bindProxyFactory(name, F::class, init)

inline fun <T : Any, reified F : T> Binder.bindWeakProxyFactory(name: TypedName<T>, noinline init: Binder.ProxyFactory.() -> Unit = {}) =
	bindWeakProxyFactory(name, F::class, init)


fun <A : Annotation> Binder.registerSmartProducerForAnnotatedClass(annotationClass: KClass<A>, producer: Injector.(A, KClass<out Any>) -> Any?) =
	registerSmartProducerForClass { clazz ->
		clazz.findAnnotation(annotationClass)?.let {
			producer(it, clazz)
		}
	}

fun <A : Annotation> Binder.registerSmartProducerForAnnotatedParameter(annotationClass: KClass<A>, producer: Injector.(A, KParameter) -> Any?) =
	registerSmartProducerForParameter { parameter ->
		parameter.findAnnotation(annotationClass)?.let {
			producer(it, parameter)
		}
	}

inline fun <reified A : Annotation> Binder.registerSmartProducerForAnnotatedClass(noinline producer: Injector.(A, KClass<out Any>) -> Any?) =
	registerSmartProducerForAnnotatedClass(A::class, producer)

inline fun <reified A : Annotation> Binder.registerSmartProducerForAnnotatedParameter(noinline producer: Injector.(A, KParameter) -> Any?) =
	registerSmartProducerForAnnotatedParameter(A::class, producer)


inline fun <reified T : Any, reified I : T> Binder.ProxyFactory.delegate() =
	delegate(T::class, I::class)
