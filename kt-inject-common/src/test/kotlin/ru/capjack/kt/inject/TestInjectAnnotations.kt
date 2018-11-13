package ru.capjack.kt.inject

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@Suppress("FunctionName")
class TestInjectAnnotations {
	
	@Test
	fun dependency_from_parameter_InjectName() {
		val name = stubNameUserId
		
		val injector = injector {
			bindInstance(name, 42)
		}
		
		val user = injector.get<StubUserImpl>()
		
		assertEquals(42, user.id)
	}
	
	@Test
	fun bind_by_InjectBind_on_class() {
		val injector = injector {}
		
		val a = injector.get<StubAutoBindReceiver>().a
		val b = injector.get<StubAutoBinded>()
		
		assertEquals(a, b)
	}
	
	@Test
	fun bind_by_InjectBind_on_property() {
		val injector = injector {}
		
		val a = injector.get<StubWithAutoBindProperty>().user
		val b = injector.get<StubUser>()
		
		assertEquals(a, b)
	}
	
	@Test
	fun bind_by_InjectBind_on_interface() {
		val injector = injector {}
		
		val a = injector.get<StubAutoBindedByInterface>()
		val b = injector.get<StubStorage>()
		
		assertEquals(a, b)
	}
	
	@Test
	fun delegate_by_InjectDelegate() {
		val injector = injector {}
		
		val a = injector.get<StubAutoDelegate>()
		
		assertTrue(a is StubAutoDelegateImpl)
	}
	
	@Test
	fun proxyFactory_by_InjectProxyFactory_and_with_InjectDelegate_member() {
		val injector = injector {}
		
		val f = injector.get<StubAutoProxy>()
		val a = f.createStorage()
		val b = f.createStorage()
		
		assertTrue(a is StubStorageImpl)
		assertNotEquals(a, b)
	}
	
	@Test
	fun proxyFactory_failed_on_wrong_member_parameters() {
		val injector = injector {}
		
		assertFailsWith<InjectException> {
			injector.get<StubBadProxy>()
		}
	}
}