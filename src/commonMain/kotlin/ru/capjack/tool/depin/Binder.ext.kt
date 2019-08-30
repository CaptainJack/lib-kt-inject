package ru.capjack.tool.depin

import ru.capjack.tool.reflect.findAnnotation
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


fun <A : Annotation> Binder.addSmartProducerForAnnotatedClassInject(annotationClass: KClass<A>, producer: Injector.(A, KClass<*>) -> Any?) =
	addSmartProducerForClassInjected { clazz ->
		clazz.findAnnotation(annotationClass)?.let {
			producer(it, clazz)
		}
	}

fun <A : Annotation> Binder.addSmartProducerForAnnotatedClass(annotationClass: KClass<A>, producer: (A, KClass<*>) -> Any?) =
	addSmartProducerForClass { clazz ->
		clazz.findAnnotation(annotationClass)?.let {
			producer(it, clazz)
		}
	}

fun <A : Annotation> Binder.addSmartProducerForAnnotatedParameterInject(annotationClass: KClass<A>, producer: Injector.(A, KParameter) -> Any?) =
	addSmartProducerForParameterInjected { parameter ->
		parameter.findAnnotation(annotationClass)?.let {
			producer(it, parameter)
		}
	}

fun <A : Annotation> Binder.addSmartProducerForAnnotatedParameter(annotationClass: KClass<A>, producer: (A, KParameter) -> Any?) =
	addSmartProducerForParameter { parameter ->
		parameter.findAnnotation(annotationClass)?.let {
			producer(it, parameter)
		}
	}

inline fun <reified A : Annotation> Binder.addSmartProducerForAnnotatedClass(noinline producer: (A, KClass<*>) -> Any?) =
	addSmartProducerForAnnotatedClass(A::class, producer)

inline fun <reified A : Annotation> Binder.addSmartProducerForAnnotatedClassInject(noinline producer: Injector.(A, KClass<*>) -> Any?) =
	addSmartProducerForAnnotatedClassInject(A::class, producer)


inline fun <reified A : Annotation> Binder.addSmartProducerForAnnotatedParameter(noinline producer: (A, KParameter) -> Any?) =
	addSmartProducerForAnnotatedParameter(A::class, producer)

inline fun <reified A : Annotation> Binder.addSmartProducerForAnnotatedParameterInject(noinline producer: (Injector, A, KParameter) -> Any?) =
	addSmartProducerForAnnotatedParameterInject(A::class, producer)
