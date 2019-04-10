package ru.capjack.tool.depin

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@Suppress("FunctionName")
class TestProxyObject {
	@Test
	fun check_toString() {
		val injector = Injector {}
		val a = injector.get<StubAutoProxy>().toString()
		
		assertTrue(a.contains("StubAutoProxy\$Proxy"))
	}
	
	@Test
	fun check_hashCode_differed() {
		val injector = Injector {}
		val a = injector.get<StubAutoProxy>().hashCode()
		val b = injector.get<StubAutoProxy>().hashCode()
		
		assertNotEquals(a, b)
	}
	
	@Test
	fun check_hashCode_equals() {
		val injector = Injector {
			bindProxy<StubAutoProxy>()
		}
		
		val a = injector.get<StubAutoProxy>().hashCode()
		val b = injector.get<StubAutoProxy>().hashCode()
		
		assertEquals(a, b)
	}
	
	@Test
	fun check_equals_false() {
		val injector = Injector {}
		val a = injector.get<StubAutoProxy>().hashCode()
		val b = injector.get<StubAutoProxy>().hashCode()
		
		@Suppress("ReplaceCallWithBinaryOperator")
		assertFalse(a.equals(b))
	}
	
	@Test
	fun check_equals_true() {
		val injector = Injector {
			bindProxy<StubAutoProxy>()
		}
		
		val a = injector.get<StubAutoProxy>().hashCode()
		val b = injector.get<StubAutoProxy>().hashCode()
		
		@Suppress("ReplaceCallWithBinaryOperator")
		assertTrue(a.equals(b))
	}
}
