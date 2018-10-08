package ru.capjack.lib.kt.inject

import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("FunctionName")
class TestBindProducer {
	
	@Test
	fun self() {
		val injector = injector { bindProducer { StubEmpty() } }
		
		val a = injector.get<StubEmpty>()
		val b = injector.get<StubEmpty>()
		
		assertEquals(a, b)
	}
	
	@Test
	fun impl() {
		val injector = injector { bindProducer<StubStorage> { StubStorageImpl() } }
		
		val a = injector.get<StubStorage>()
		val b = injector.get<StubStorage>()
		
		assertEquals(a, b)
	}
	
	//
	
	@Test
	fun named_self() {
		val name = stubNameEmpty
		val injector = injector { bindProducer(name) { StubEmpty() } }
		
		val a = injector.get(name)
		val b = injector.get(name)
		
		assertEquals(a, b)
	}
	
	
	@Test
	fun named_impl() {
		val name = stubNameStorage
		val injector = injector { bindProducer(name) { StubStorageImpl() } }
		
		val a = injector.get(name)
		val b = injector.get(name)
		
		assertEquals(a, b)
	}
}