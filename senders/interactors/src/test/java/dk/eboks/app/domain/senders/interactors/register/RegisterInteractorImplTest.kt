package dk.eboks.app.domain.senders.interactors.register

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.SenderGroup
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import retrofit2.Response
import java.util.concurrent.CountDownLatch

class RegisterInteractorImplTest {

    private val executor = TestExecutor()
    private val api: Api = mockk(relaxUnitFun = true)

    private val interactor = RegisterInteractorImpl(executor, api)

    @Test
    fun `Test Register with Inputs`() {
        val latch = CountDownLatch(3)

        every { api.registerSender(any()).execute() } returns Response.success(null)
        every { api.registerSegment(any() as Long).execute() } returns Response.success(null)
        every { api.registerSenderGroup(any(), any(), any()).execute() } returns Response.success(null)

        interactor.inputSegment = RegisterInteractor.InputSegment(2L)
        interactor.inputSenderGroup = RegisterInteractor.InputSenderGroup(12L, SenderGroup(12, description = mockk(), status = mockk()))
        interactor.inputSender = RegisterInteractor.InputSender(211L)

        interactor.output = object : RegisterInteractor.Output {
            override fun onSuccess() {
                assert(true)
                latch.countDown()
            }

            override fun onError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Test Register with Inputs Error`() {
        val latch = CountDownLatch(3)

        every { api.registerSender(any()).execute() } throws Exception()
        every { api.registerSegment(any() as Long).execute() } throws Exception()
        every { api.registerSenderGroup(any(), any(), any()).execute() } throws Exception()

        interactor.inputSegment = RegisterInteractor.InputSegment(2L)
        interactor.inputSenderGroup = RegisterInteractor.InputSenderGroup(12L, SenderGroup(12, description = mockk(), status = mockk()))
        interactor.inputSender = RegisterInteractor.InputSender(211L)

        interactor.output = object : RegisterInteractor.Output {
            override fun onSuccess() {
                assert(false)
                latch.countDown()
            }

            override fun onError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }
}