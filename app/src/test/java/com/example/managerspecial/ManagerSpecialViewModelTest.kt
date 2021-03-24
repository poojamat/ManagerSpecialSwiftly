package com.example.managerspecial

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.managerspecial.network.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import junit.framework.Assert.*
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ManagerSpecialViewModelTest : KoinTest {
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
    }

    @After
    open fun tearDown() {
        stopKoin()
    }
    @Test
    fun testGetManagerSpecialList() {
        val countDownLatch = CountDownLatch(1)
        val expectedDisplayName = "testDisplay"
        val managerSpecialResource = mockk<ManagerSpecialResource> {
            every { fetch() } returns MutableLiveData(LiveDataResource.success( ManagerSpecials(
                5, listOf(
                    ManagerSpecial("", 1, 2, expectedDisplayName, 1.0, 1.0)
                )
            )))
        }
        declareMock<Repository> {
            every { managerSpecialResource() } returns managerSpecialResource
        }
        val subject = ManagerSpecialViewModel()
        subject.managerSpecialList.observe(TestLifecycleOwner(),  { result ->
         if (result.status == Status.SUCCESS) {
                assertEquals(
                    expectedDisplayName,
                    result.data!!.managerSpecials[0].display_name
                )
                countDownLatch.countDown()
            } })
        org.junit.Assert.assertTrue(countDownLatch.await(3, TimeUnit.SECONDS))
    }

    @Test
    fun testWouldFitInDimensionReturnsTrue() {

        val canvasUnit = 16
        val totalHeightPixels = 1080
        val tileDimension = 16
        val dimenUsed = 0
        val subject = ManagerSpecialViewModel()
        val result = subject.wouldFitInDimension(canvasUnit, totalHeightPixels, tileDimension, dimenUsed)
        assertTrue(result)
    }

    @Test
    fun testWouldFitInDimensionReturnsFalse() {

        val canvasUnit = 16
        val totalHeightPixels = 1080
        val tileDimension = 16
        val dimenUsed = 8
        val subject = ManagerSpecialViewModel()
        val result = subject.wouldFitInDimension(canvasUnit, totalHeightPixels, tileDimension, dimenUsed)
        assertFalse(result)
    }
}