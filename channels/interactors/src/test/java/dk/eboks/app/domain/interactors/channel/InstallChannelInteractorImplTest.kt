package dk.eboks.app.domain.interactors.channel

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Test
import retrofit2.Response
import java.util.concurrent.CountDownLatch

class InstallChannelInteractorImplTest {
    private val executor = TestExecutor()
    private val apiMock = mockk<Api>()
    private val interactor = InstallChannelInteractorImpl(executor, apiMock)

    @After
    fun tearDown() {
        interactor.output = null
        interactor.input = null
    }

    @Test
    fun `Test success`() {
        val id = 1234
        every { apiMock.installChannel(id).execute() } returns Response.success(null)
        interactor.input = InstallChannelInteractor.Input(id)
        val latch = CountDownLatch(1)
        interactor.output = object : InstallChannelInteractor.Output {
            override fun onInstallChannel() {
                latch.countDown()
            }

            override fun onInstallChannelError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test failure`() {
        val id = 1234
        every { apiMock.installChannel(id).execute() } returns Response.error(400, mockk())
        interactor.input = InstallChannelInteractor.Input(id)
        val latch = CountDownLatch(1)
        interactor.output = object : InstallChannelInteractor.Output {
            override fun onInstallChannel() {
                assert(false)
                latch.countDown()
            }

            override fun onInstallChannelError(error: ViewError) {
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test no args`() {
        val latch = CountDownLatch(1)
        interactor.output = object : InstallChannelInteractor.Output {
            override fun onInstallChannel() {
                assert(false)
                latch.countDown()
            }

            override fun onInstallChannelError(error: ViewError) {
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }
}