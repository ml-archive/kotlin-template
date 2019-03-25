package dk.eboks.app.domain.senders.interactors.register

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import retrofit2.Response
import java.lang.Exception
import java.util.concurrent.CountDownLatch

class GetPendingInteractorImplTest {

    private val executor = TestExecutor()
    private val api: Api = mockk()

    private val interactor: GetPendingInteractor = GetPendingInteractorImpl(executor, api)

    @Test
    fun `Test Get Pending Registrations Loading Success`() {
        val latch = CountDownLatch(1)
        val body = listOf<CollectionContainer>()

        every { api.getPendingRegistrations().execute() } returns Response.success(body)

        interactor.output = object : GetPendingInteractor.Output {
            override fun onRegistrationsLoaded(registrations: List<CollectionContainer>) {
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
    fun `Test Get Pending Registrations Loading with API Error`() {
        val latch = CountDownLatch(1)

        every { api.getPendingRegistrations().execute() } returns Response.error(403, mockk(relaxed = true))

        interactor.output = object : GetPendingInteractor.Output {
            override fun onRegistrationsLoaded(registrations: List<CollectionContainer>) {
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
    fun `Test Get Pending Registrations Loading with Exception`() {
        val latch = CountDownLatch(1)

        every { api.getPendingRegistrations().execute() } throws Exception()

        interactor.output = object : GetPendingInteractor.Output {
            override fun onRegistrationsLoaded(registrations: List<CollectionContainer>) {
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