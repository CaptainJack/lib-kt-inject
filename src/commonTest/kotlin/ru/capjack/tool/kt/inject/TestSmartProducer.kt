package ru.capjack.tool.kt.inject

import kotlin.test.Test
import kotlin.test.assertTrue

@Suppress("FunctionName")
class TestSmartProducer {
	@Test
	fun for_class() {
		val injector = Injector {
			registerSmartProducerForClass {
				return@registerSmartProducerForClass if (it == StubStorage::class) StubStorageImpl() else null
			}
		}
		
		val a = injector.get<StubUserManagerImpl>().storage
		
		assertTrue(a is StubStorageImpl)
	}
	
	@Test
	fun for_parameter() {
		val injector = Injector {
			registerSmartProducerForParameter {
				return@registerSmartProducerForParameter if (it.name == "storage") StubStorageImpl() else null
			}
		}
		
		val a = injector.get<StubUserManagerImpl>().storage
		
		assertTrue(a is StubStorageImpl)
	}
	
	@Test
	fun for_annotated_class() {
		val injector = Injector {
			registerSmartProducerForAnnotatedClass<StubClass> { _, _ ->
				StubStorageImpl()
			}
		}
		
		val a = injector.get<StubUserManagerImpl>().storage
		
		assertTrue(a is StubStorageImpl)
	}
	
	@Test
	fun for_annotated_parameter() {
		val injector = Injector {
			registerSmartProducerForAnnotatedParameter<StubParameter>() { _, _ ->
				StubStorageImpl()
			}
		}
		
		val a = injector.get<StubUserManagerImpl>().storage
		
		assertTrue(a is StubStorageImpl)
	}
}