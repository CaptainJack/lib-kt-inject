package ru.capjack.kt.inject

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@Suppress("FunctionName")
class TestBind {
	
	@Test
	fun typed() {
		val obj = StubEmpty()
		val injector = injector { bind(obj) }
		
		assertEquals(obj, injector.get())
	}
	
	@Test
	fun named() {
		val obj = StubEmpty()
		val name = stubNameEmpty
		
		val injector = injector { bind(name, obj) }
		
		assertEquals(obj, injector.get(name))
	}
	
	@Test
	fun fail_on_strong_typed() {
		val builder = InjectorBuilder()
		
		builder.configure {
			bind(StubEmpty())
			bind(StubEmpty())
		}
		
		assertFailsWith<InjectException> {
			builder.build(true)
		}
	}
	
	@Test
	fun fail_on_strong_named() {
		val name = stubNameEmpty
		
		val builder = InjectorBuilder()
		
		builder.configure {
			bind(name, StubEmpty())
			bind(name, StubEmpty())
		}
		
		assertFailsWith<InjectException> {
			builder.build(true)
		}
	}
	
	@Test
	fun success_on_not_strong_typed() {
		val builder = InjectorBuilder()
		
		builder.configure {
			bind(StubEmpty())
			bind(StubEmpty())
		}
		
		builder.build(false)
	}
	
	@Test
	fun success_on_not_strong_named() {
		val builder = InjectorBuilder()
		
		val name = stubNameEmpty
		
		builder.configure {
			bind(name, StubEmpty())
			bind(name, StubEmpty())
		}
		
		builder.build(false)
	}
}