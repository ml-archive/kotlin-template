package dk.eboks.app.domain.senders.interactors.register

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.models.sender.Registrations
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import retrofit2.Response
import java.lang.Exception
import java.util.concurrent.CountDownLatch

class GetRegistraionsInteractorImplTest {

    private val executor = TestExecutor()
    private val api: Api = mockk()

    private val interactor: GetRegistrationsInteractor = GetRegistrationsInteractorImpl(executor, api)

    @Test
    fun `Test Get Registrations Loading Success`() {
        val latch = CountDownLatch(1)
        val body = mockk<Registrations>(relaxed = true)

        every { api.getRegistrations().execute() } returns Response.success(body)

        interactor.output = object : GetRegistrationsInteractor.Output {
            override fun onRegistrationsLoaded(registrations: Registrations) {
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
    fun `Test Get Registrations Loading API Error`() {
        val latch = CountDownLatch(1)

        every { api.getRegistrations().execute() } returns Response.error(403, mockk(relaxed = true))

        interactor.output = object : GetRegistrationsInteractor.Output {
            override fun onRegistrationsLoaded(registrations: Registrations) {
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

    @Test
    fun `Test Get  Registrations Loading With Exception`() {
        val latch = CountDownLatch(1)
        every { api.getRegistrations().execute() } throws Exception()

        interactor.output = object : GetRegistrationsInteractor.Output {
            override fun onRegistrationsLoaded(registrations: Registrations) {
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