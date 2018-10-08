package ru.capjack.lib.kt.inject

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@Suppress("FunctionName")
class TestInjectSimple {
	
	@Test
	fun fail_on_not_injectable() {
		val injector = injector {}
		
		assertFailsWith<InjectException> {
			injector.get<StubNotInjectable>()
		}
	}
	
	@Test
	fun fail_on_no_dependency() {
		val injector = injector {
			bindDelegate<StubUserManager, StubUserManagerImpl>()
		}
		
		assertFailsWith<InjectException> {
			injector.get<StubUserManager>()
		}
	}
	
	@Test
	fun dependency_from_delegated() {
		val injector = injector {
			bindDelegate<StubUserManager, StubUserManagerImpl>()
			bindDelegate<StubStorage, StubStorageImpl>()
		}
		
		val userManager = injector.get<StubUserManager>()
		
		assertEquals(injector.get(), userManager.storage)
	}
	
	@Test
	fun dependency_from_instance() {
		val storage = StubStorageImpl()
		
		val injector = injector {
			bindDelegate<StubUserManager, StubUserManagerImpl>()
			bind<StubStorage>(storage)
		}
		
		val userManager = injector.get<StubUserManager>()
		
		assertEquals(storage, userManager.storage)
	}
	
	@Test
	fun dependency_from_parameter_name() {
		val name = stubNameStorage
		
		val injector = injector {
			bindDelegate<StubUserManager, StubUserManagerWithNamedStorage>()
			bind(name, StubStorageImpl())
		}
		
		val userManager = injector.get<StubUserManager>()
		
		assertEquals(injector.get(name), userManager.storage)
	}
	
	@Test
	fun proxy_call_with_params_simple() {
		val injector = injector {
			bindProxyFactory<StubUserFactory> {
				delegate<StubUser, StubUserImpl>()
			}
		}
		
		val factory = injector.get<StubUserFactory>()
		
		val user = factory.createUser(30)
		
		assertEquals(30, user.id)
	}
	
	@Test
	fun proxy_call_with_params_complex() {
		val injector = injector {
			bindProxyFactory<StubUserFactory> { delegate<StubUser, StubUserImpl>() }
			bindDelegate<StubStorage, StubStorageImpl>()
			bindDelegate<StubEmpty, StubEmpty>()
		}
		
		val factory = injector.get<StubUserFactory>()
		
		val user = factory.createSuperUser(3)
		
		assertEquals(3, user.id)
		assertEquals(injector.get(), user.empty)
		assertEquals(injector.get(), user.storage)
	}
	
	@Test
	fun proxy_call_with_params_equal_types() {
		val injector = injector {
			bindProxyFactory<StubUserFactory> { delegate<StubUser, StubUserImpl>() }
		}
		
		val factory = injector.get<StubUserFactory>()
		
		val user = factory.createBabUser(1, 2)
		
		assertEquals(1, user.id)
		assertEquals(2, user.rank)
	}
	
	@Test
	fun proxy_call_with_params_equal_types_strange() {
		val injector = injector {
			bindProxyFactory<StubUserFactory> { delegate<StubUser, StubUserImpl>() }
		}
		
		val factory = injector.get<StubUserFactory>()
		
		val user = factory.createBabUserStrange(1, 2)
		
		assertEquals(1, user.id)
		assertEquals(2, user.rank)
	}
	
	@Test
	fun proxy_call_with_params_equal_types_tangled() {
		val injector = injector {
			bindProxyFactory<StubUserFactory> { delegate<StubUser, StubUserImpl>() }
		}
		
		val factory = injector.get<StubUserFactory>()
		
		val user = factory.createBabUserTangled(2, 1)
		
		assertEquals(1, user.id)
		assertEquals(2, user.rank)
	}
}