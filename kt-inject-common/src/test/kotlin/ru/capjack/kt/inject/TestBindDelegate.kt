package ru.capjack.kt.inject

import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("FunctionName")
class TestBindDelegate {
	
	@Test
	fun self() {
		val injector = injector { bindDelegate<StubEmpty, StubEmpty>() }
		
		val a = injector.get<StubEmpty>()
		val b = injector.get<StubEmpty>()
		
		assertEquals(a, b)
	}
	
	@Test
	fun impl() {
		val injector = injector { bindDelegate<StubStorage, StubStorageImpl>() }
		
		val a = injector.get<StubStorage>()
		val b = injector.get<StubStorage>()
		
		assertEquals(a, b)
	}
	
	//
	
	@Test
	fun named_self() {
		val name = stubNameEmpty
		val injector = injector { bindDelegate(name) }
		
		val a = injector.get(name)
		val b = injector.get(name)
		
		assertEquals(a, b)
	}
	
	
	@Test
	fun named_impl() {
		val name = stubNameStorage
		val injector = injector { bindDelegate<StubStorage, StubStorageImpl>(name) }
		
		val a = injector.get(name)
		val b = injector.get(name)
		
		assertEquals(a, b)
	}
}