package ru.capjack.lib.kt.inject

import kotlin.test.Test
import kotlin.test.assertNotEquals

@Suppress("FunctionName")
class TestBindWeakDelegate {
	@Test
	fun self() {
		val injector = injector { bindWeakDelegate<StubEmpty, StubEmpty>() }
		
		val a = injector.get<StubEmpty>()
		val b = injector.get<StubEmpty>()
		
		assertNotEquals(a, b)
	}
	
	@Test
	fun impl() {
		val injector = injector { bindWeakDelegate<StubStorage, StubStorageImpl>() }
		
		val a = injector.get<StubStorage>()
		val b = injector.get<StubStorage>()
		
		assertNotEquals(a, b)
	}
	
	//
	
	@Test
	fun named_self() {
		val name = stubNameEmpty
		val injector = injector { bindWeakDelegate(name) }
		
		val a = injector.get(name)
		val b = injector.get(name)
		
		assertNotEquals(a, b)
	}
	
	
	@Test
	fun named_impl() {
		val name = stubNameStorage
		val injector = injector { bindWeakDelegate<StubStorage, StubStorageImpl>(name) }
		
		val a = injector.get(name)
		val b = injector.get(name)
		
		assertNotEquals(a, b)
	}
}