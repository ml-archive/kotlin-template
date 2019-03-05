package dk.eboks.app.mail.domain.interactors.shares

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.SharedUser
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import retrofit2.Response
import java.util.concurrent.CountDownLatch

class GetAllSharesInteractorImplTest {

    private val executor = TestExecutor()
    private val api = mockk<Api>()

    private val interactor = GetAllSharesInteractorImpl(executor, api)

    private val dummyShares = List(3) {
        SharedUser(it, it, "name", "permision", null, null)
    }

    @Test
    fun `Get All Shares Test`() {

        val latch = CountDownLatch(1)
        every { api.getAllShares().execute() } returns Response.success(dummyShares)

        interactor.output = object : GetAllSharesInteractor.Output {
            override fun onGetAllShares(shares: List<SharedUser>) {
                assert(true)
                assert(shares.containsAll(dummyShares))
                assert(shares.size == dummyShares.size)
                latch.countDown()
            }

            override fun onGetAllSharesError(viewError: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()

    }


    @Test
    fun `Get All Shares Empty Body Test`() {

        val latch = CountDownLatch(1)
        every { api.getAllShares().execute() } returns Response.success(null)

        interactor.output = object : GetAllSharesInteractor.Output {
            override fun onGetAllShares(shares: List<SharedUser>) {
                assert(false)
                latch.countDown()
            }

            override fun onGetAllSharesError(viewError: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()

    }

    @Test
    fun `Get All Shares Exception Test`() {

        val latch = CountDownLatch(1)
        every { api.getAllShares().execute() } throws Exception()

        interactor.output = object : GetAllSharesInteractor.Output {
            override fun onGetAllShares(shares: List<SharedUser>) {
                assert(false)
                latch.countDown()
            }

            override fun onGetAllSharesError(viewError: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()

    }


}