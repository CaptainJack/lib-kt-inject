package ru.capjack.kt.inject

import ru.capjack.kt.reflect.findAnnotation
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

inline fun <reified T : Any> Binder.bind(instance: T) =
	bindInstance(T::class, instance)

inline fun <reified T : Any, reified D : T> Binder.bind() =
	bind(T::class, D::class)

inline fun <reified T : Any> Binder.bind(noinline producer: () -> T) =
	bind(T::class, producer)

inline fun <reified T : Any> Binder.bindInjected(noinline producer: Injector.() -> T) =
	bindInjected(T::class, producer)


inline fun <reified T : Any, reified D : T> Binder.bindSupplier() =
	bindSupplier(T::class, D::class)

inline fun <reified T : Any> Binder.bindSupplier(noinline producer: () -> T) =
	bindSupplier(T::class, producer)

inline fun <reified T : Any> Binder.bindSupplierInjected(noinline producer: Injector.() -> T) =
	bindSupplierInjected(T::class, producer)


inline fun <T : Any, reified D : T> Binder.bind(name: TypedName<T>) =
	bind(name, D::class)

inline fun <T : Any, reified D : T> Binder.bindSupplier(name: TypedName<T>) =
	bindSupplier(name, D::class)


inline fun <reified T : Any> Binder.bindProxy(noinline init: Binder.Factory.() -> Unit = {}) =
	bindProxy(T::class, init)

inline fun <reified T : Any> Binder.bindProxySupplier(noinline init: Binder.Factory.() -> Unit = {}) =
	bindProxySupplier(T::class, init)


inline fun <T : Any, reified F : T> Binder.bindProxy(name: TypedName<T>, noinline init: Binder.Factory.() -> Unit = {}) =
	bindProxy(name, F::class, init)

inline fun <T : Any, reified F : T> Binder.bindProxySupplier(name: TypedName<T>, noinline init: Binder.Factory.() -> Unit = {}) =
	bindProxySupplier(name, F::class, init)

inline fun <reified T : Any, reified I : T> Binder.Factory.bind() =
	bind(T::class, I::class)


fun <A : Annotation> Binder.registerSmartProducerForAnnotatedClassInject(annotationClass: KClass<A>, producer: Injector.(A, KClass<out Any>) -> Any?) =
	registerSmartProducerForClassInjected { clazz ->
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
	registerSmartProducerForParameterInjected { parameter ->
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
