package ru.capjack.kt.inject

import ru.capjack.kt.reflect.findAnnotation
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

inline fun <reified T : Any> Binder.bind(instance: T) =
	bind(T::class, instance)

inline fun <reified T : Any, reified D : T> Binder.bindDelegate() =
	bindDelegate(T::class, D::class)

inline fun <reified T : Any> Binder.bindProducer(noinline producer: () -> T) =
	bindProducer(T::class, producer)

inline fun <reified T : Any> Binder.bindProducerInject(noinline producer: Injector.() -> T) =
	bindProducerInject(T::class, producer)


inline fun <reified T : Any, reified D : T> Binder.bindSupplier() =
	bindSupplier(T::class, D::class)

inline fun <reified T : Any> Binder.bindSupplier(noinline factory: () -> T) =
	bindSupplier(T::class, factory)

inline fun <reified T : Any> Binder.bindSupplierInject(noinline factory: Injector.() -> T) =
	bindSupplierInject(T::class, factory)


inline fun <T : Any, reified D : T> Binder.bindDelegate(name: TypedName<T>) =
	bindDelegate(name, D::class)

inline fun <T : Any, reified D : T> Binder.bindSupplier(name: TypedName<T>) =
	bindSupplier(name, D::class)


inline fun <reified T : Any> Binder.bindProxyFactory(noinline init: Binder.ProxyFactory.() -> Unit = {}) =
	bindProxyFactory(T::class, init)

inline fun <reified T : Any> Binder.bindProxyFactorySupplier(noinline init: Binder.ProxyFactory.() -> Unit = {}) =
	bindProxyFactorySupplier(T::class, init)

inline fun <T : Any, reified F : T> Binder.bindProxyFactory(name: TypedName<T>, noinline init: Binder.ProxyFactory.() -> Unit = {}) =
	bindProxyFactory(name, F::class, init)

inline fun <T : Any, reified F : T> Binder.bindProxyFactorySupplier(name: TypedName<T>, noinline init: Binder.ProxyFactory.() -> Unit = {}) =
	bindProxyFactorySupplier(name, F::class, init)


fun <A : Annotation> Binder.registerSmartProducerForAnnotatedClassInject(annotationClass: KClass<A>, producer: Injector.(A, KClass<out Any>) -> Any?) =
	registerSmartProducerForClassInject { clazz ->
		clazz.findAnnotation(annotationClass)?.let {
			producer(it, clazz)
		}
	}

fun <A : Annotation> Binder.registerSmartProducerForAnnotatedClass(annotationClass: KClass<A>, producer: (A, KClass<out Any>) -> Any?) =
	registerSmartProducerForClass { clazz ->
		clazz.findAnnotation(annotationClass)?.let {
			producer(it, clazz)
		}
	}

fun <A : Annotation> Binder.registerSmartProducerForAnnotatedParameterInject(annotationClass: KClass<A>, producer: Injector.(A, KParameter) -> Any?) =
	registerSmartProducerForParameterInject { parameter ->
		parameter.findAnnotation(annotationClass)?.let {
			producer(it, parameter)
		}
	}

fun <A : Annotation> Binder.registerSmartProducerForAnnotatedParameter(annotationClass: KClass<A>, producer: (A, KParameter) -> Any?) =
	registerSmartProducerForParameter { parameter ->
		parameter.findAnnotation(annotationClass)?.let {
			producer(it, parameter)
		}
	}

inline fun <reified A : Annotation> Binder.registerSmartProducerForAnnotatedClass(noinline producer: (A, KClass<out Any>) -> Any?) =
	registerSmartProducerForAnnotatedClass(A::class, producer)

inline fun <reified A : Annotation> Binder.registerSmartProducerForAnnotatedClassInject(noinline producer: Injector.(A, KClass<out Any>) -> Any?) =
	registerSmartProducerForAnnotatedClassInject(A::class, producer)


inline fun <reified A : Annotation> Binder.registerSmartProducerForAnnotatedParameter(noinline producer: (A, KParameter) -> Any?) =
	registerSmartProducerForAnnotatedParameter(A::class, producer)

inline fun <reified A : Annotation> Binder.registerSmartProducerForAnnotatedParameterInject(noinline producer: (Injector, A, KParameter) -> Any?) =
	registerSmartProducerForAnnotatedParameterInject(A::class, producer)


inline fun <reified T : Any, reified I : T> Binder.ProxyFactory.delegate() =
	delegate(T::class, I::class)
