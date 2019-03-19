package dk.eboks.app.keychain.interactors.mobileacces

import dk.eboks.app.domain.models.local.Settings
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.ActivationDevice
import dk.eboks.app.domain.repositories.SettingsRepository
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import retrofit2.Response
import java.util.concurrent.CountDownLatch

class ActivateDeviceInteractorImplTest {
    private val executor = TestExecutor()
    private val settingsRepository = mockk<SettingsRepository>(relaxUnitFun = true)
    private val api = mockk<Api>()
    private val interactor = ActivateDeviceInteractorImpl(executor, settingsRepository, api)

    @Test
    fun `Test success`() {
        val deviceId = "deviceid"
        val settings = Settings(deviceId)
        val deviceName = "test device"
        val os = "Android"
        val key = "Key"
        every { api.activateDevice(any()).execute() } returns Response.success(null)
        every { settingsRepository.get() } returns settings
        interactor.input = ActivateDeviceInteractor.Input(key)
        val latch = CountDownLatch(1)
        interactor.output = object : ActivateDeviceInteractor.Output {
            override fun onActivateDeviceSuccess() {
                latch.countDown()
            }

            override fun onActivateDeviceError(error: ViewError, RSAKey: String?) {
                assert(false)
                latch.countDown()
            }
        }
        interactor.run()
        verify { api.activateDevice(ActivationDevice(deviceId, deviceName, os, key)) }
        latch.await()
    }

    @Test
    fun `Test error`() {
        val deviceId = "deviceid"
        val settings = Settings(deviceId)
        val deviceName = "test device"
        val os = "Android"
        val key = "Key"
        val exception = Exception()
        every { api.activateDevice(any()).execute() } throws exception
        every { settingsRepository.get() } returns settings
        interactor.input = ActivateDeviceInteractor.Input(key)
        val latch = CountDownLatch(1)
        interactor.output = object : ActivateDeviceInteractor.Output {
            override fun onActivateDeviceSuccess() {
                assert(false)
                latch.countDown()
            }

            override fun onActivateDeviceError(error: ViewError, RSAKey: String?) {
                assertEquals(error, interactor.exceptionToViewError(exception))
                assertEquals(RSAKey, key)
                latch.countDown()
            }
        }
        interactor.run()
        verify { api.activateDevice(ActivationDevice(deviceId, deviceName, os, key)) }
        latch.await()
    }

    @Test
    fun `Test no args error`() {
        val latch = CountDownLatch(1)
        interactor.output = object : ActivateDeviceInteractor.Output {
            override fun onActivateDeviceSuccess() {
                assert(false)
                latch.countDown()
            }

            override fun onActivateDeviceError(error: ViewError, RSAKey: String?) {
                assertNull(RSAKey)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }
}