package ru.capjack.kt.inject

import kotlin.test.Test
import kotlin.test.assertNotEquals

@Suppress("FunctionName")
class TestBindWeakDelegate {
	@Test
	fun self() {
		val injector = injector { bindSupplier<StubEmpty, StubEmpty>() }
		
		val a = injector.get<StubEmpty>()
		val b = injector.get<StubEmpty>()
		
		assertNotEquals(a, b)
	}
	
	@Test
	fun impl() {
		val injector = injector { bindSupplier<StubStorage, StubStorageImpl>() }
		
		val a = injector.get<StubStorage>()
		val b = injector.get<StubStorage>()
		
		assertNotEquals(a, b)
	}
	
	//
	
	@Test
	fun named_self() {
		val name = stubNameEmpty
		val injector = injector { bindSupplier(name) }
		
		val a = injector.get(name)
		val b = injector.get(name)
		
		assertNotEquals(a, b)
	}
	
	
	@Test
	fun named_impl() {
		val name = stubNameStorage
		val injector = injector { bindSupplier<StubStorage, StubStorageImpl>(name) }
		
		val a = injector.get(name)
		val b = injector.get(name)
		
		assertNotEquals(a, b)
	}
}