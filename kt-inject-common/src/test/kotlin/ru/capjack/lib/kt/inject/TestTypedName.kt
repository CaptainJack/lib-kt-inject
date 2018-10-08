package ru.capjack.lib.kt.inject

import kotlin.test.Test
import kotlin.test.assertEquals

class TestTypedName {
	
	@Test
	fun name() {
		val value = "name".asType<Int>()
		assertEquals("name", value.name)
	}
	
	@Test
	fun type_Boolean() {
		val value = "name".asType<Boolean>()
		assertEquals(Boolean::class, value.type)
	}
	
	@Test
	fun type_Int() {
		val value = "name".asType<Int>()
		assertEquals(Int::class, value.type)
	}
	
	@Test
	fun type_Float() {
		val value = "name".asType<Float>()
		assertEquals(Float::class, value.type)
	}
	
	@Test
	fun type_Long() {
		val value = "name".asType<Long>()
		assertEquals(Long::class, value.type)
	}
	
	@Test
	fun type_Double() {
		val value = "name".asType<Double>()
		assertEquals(Double::class, value.type)
	}
	
	@Test
	fun type_String() {
		val value = "name".asType<String>()
		assertEquals(String::class, value.type)
	}
	
	@Test
	fun type_Interface() {
		val value = "name".asType<StubStorage>()
		assertEquals(StubStorage::class, value.type)
	}
	
	@Test
	fun type_Class() {
		val value = "name".asType<StubEmpty>()
		assertEquals(StubEmpty::class, value.type)
	}
}