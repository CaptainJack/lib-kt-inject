package ru.capjack.lib.kt.inject

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
	@InjectName("userId")
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
class StubUserManagerWithNamedStorage(@InjectName override val storage: StubStorage) : StubUserManager

@Inject
@InjectBind
class StubAutoBinded

@Inject
class StubAutoBindReceiver(val a: StubAutoBinded)

@Inject
class StubWithAutoBindProperty {
	@InjectBind
	val user: StubUser = StubUserImpl(12)
}

@Inject
class StubAutoBindedByInterface : @InjectBind StubStorage

@InjectDelegate(StubAutoDelegateImpl::class)
interface StubAutoDelegate

@Inject
class StubAutoDelegateImpl : StubAutoDelegate

@InjectProxyFactory
interface StubAutoProxyFactory {
	@InjectDelegate(StubStorageImpl::class)
	fun createStorage(): StubStorage
}

@InjectProxyFactory
interface StubBadProxyFactory {
	fun createStorage(value: Int): StubEmpty
}