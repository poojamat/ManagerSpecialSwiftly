package com.example.managerspecial.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.example.managerspecial.networkModule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ManagerSpecialResourceTest : KoinTest {
    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz, relaxed = true)
    }

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    open fun setup() {
        startKoin { modules(networkModule) }
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    open fun tearDown() {
        stopKoin()
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")


    @Test
    fun managerSpecialAPIFetchReturnsSuccess() {
        val countDownLatch = CountDownLatch(2)
        val expectedDisplayName = "testDisplay"
        val managerSpecialAPI = mockk<ManagerSpecialAPI> {
            coEvery { getManagerSpecials() } returns ManagerSpecials(
                5, listOf(
                    ManagerSpecial("", 1, 2, expectedDisplayName, 1.0, 1.0)
                )
            )
        }
        val subject = ManagerSpecialResource(managerSpecialAPI)
        subject.fetch().observe(TestLifecycleOwner(),  { result ->
            if (result.status == Status.LOADING) {
                countDownLatch.countDown()
            } else if (result.status == Status.SUCCESS) {
                assertEquals(expectedDisplayName, result.data!!.managerSpecials[0].display_name)
                countDownLatch.countDown()
            } })
        Assert.assertTrue(countDownLatch.await(3, TimeUnit.SECONDS))
    }
}


class TestLifecycleOwner : LifecycleOwner {
    val lifecycleRegistry = init()
    private fun init(): LifecycleRegistry {
        val registry = LifecycleRegistry(this)
        registry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        registry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        registry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        return registry
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }


}