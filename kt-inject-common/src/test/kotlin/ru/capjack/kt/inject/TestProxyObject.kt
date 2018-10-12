package ru.capjack.kt.inject

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@Suppress("FunctionName")
class TestProxyObject {
	@Test
	fun check_toString() {
		val injector = injector {}
		val a = injector.get<StubAutoProxyFactory>().toString()
		
		assertTrue(a.contains("StubAutoProxyFactory\$Proxy"))
	}
	
	@Test
	fun check_hashCode_differed() {
		val injector = injector {}
		val a = injector.get<StubAutoProxyFactory>().hashCode()
		val b = injector.get<StubAutoProxyFactory>().hashCode()
		
		assertNotEquals(a, b)
	}
	
	@Test
	fun check_hashCode_equals() {
		val injector = injector {
			bindProxyFactory<StubAutoProxyFactory>()
		}
		
		val a = injector.get<StubAutoProxyFactory>().hashCode()
		val b = injector.get<StubAutoProxyFactory>().hashCode()
		
		assertEquals(a, b)
	}
	
	@Test
	fun check_equals_false() {
		val injector = injector {}
		val a = injector.get<StubAutoProxyFactory>().hashCode()
		val b = injector.get<StubAutoProxyFactory>().hashCode()
		
		@Suppress("ReplaceCallWithBinaryOperator")
		assertFalse(a.equals(b))
	}
	
	@Test
	fun check_equals_true() {
		val injector = injector {
			bindProxyFactory<StubAutoProxyFactory>()
		}
		
		val a = injector.get<StubAutoProxyFactory>().hashCode()
		val b = injector.get<StubAutoProxyFactory>().hashCode()
		
		@Suppress("ReplaceCallWithBinaryOperator")
		assertTrue(a.equals(b))
	}
}
