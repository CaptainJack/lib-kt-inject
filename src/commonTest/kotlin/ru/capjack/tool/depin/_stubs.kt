@file:Suppress("UNUSED_PARAMETER", "unused")

package ru.capjack.tool.depin

val stubNameEmpty = "empty".asType<StubEmpty>()
val stubNameStorage = "storage".asType<StubStorage>()
val stubNameFactory = "factory".asType<StubUserFactory>()
val stubNameUserId = "userId".asType<Int>()

annotation class StubClass
annotation class StubParameter

class StubNotInjectable

@Inject
class StubEmpty

@StubClass
interface StubStorage

@Inject
class StubStorageImpl : StubStorage

interface StubUser {
	val id: Int
}

@Inject
open class StubUserImpl(
	@Name("userId")
	override val id: Int
) : StubUser

@Inject
class StubUserSuper(val storage: StubStorage, id: Int, val empty: StubEmpty) : StubUserImpl(id)

@Inject
class StubUserBad(id: Int, val rank: Int) : StubUserImpl(id)

interface StubUserFactory {
	fun createUser(uid: Int): StubUser
	
	fun createSuperUser(id: Int): StubUserSuper
	
	fun createBabUser(id: Int, rank: Int): StubUserBad
	
	fun createBabUserTangled(rank: Int, id: Int): StubUserBad
	
	fun createBabUserStrange(idStrange: Int, rankStrange: Int): StubUserBad
}

interface StubUserManager {
	val storage: StubStorage
}

@Inject
class StubUserManagerImpl(@StubParameter override val storage: StubStorage) : StubUserManager

@Inject
class StubUserManagerWithNamedStorage(@Name override val storage: StubStorage) : StubUserManager

@Inject
@Bind
class StubAutoBinded

@Inject
class StubAutoBindReceiver(val a: StubAutoBinded)

@Inject
class StubWithAutoBindProperty {
	@Bind
	val user: StubUser = StubUserImpl(12)
}

@Inject
class StubAutoBindedByInterface : @Bind StubStorage

@Implementation(StubAutoImplementationImpl::class)
interface StubAutoImplementation

@Inject
class StubAutoImplementationImpl : StubAutoImplementation

@Proxy
interface StubAutoProxy {
	@Implementation(StubStorageImpl::class)
	fun createStorage(): StubStorage
}

@Proxy
interface StubBadProxy {
	fun createStorage(value: Int): StubEmpty
}

@Inject
class StubCircularA(b: StubCircularB)

@Implementation(StubCircularBImpl::class)
interface StubCircularB

@Inject
class StubCircularBImpl(c: StubCircularC)

@Inject
class StubCircularC(a: StubCircularA)