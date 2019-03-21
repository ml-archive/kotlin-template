package dk.eboks.app.mail.domain.interactors.message

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.shared.Link
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import retrofit2.Response
import java.util.Date
import java.util.concurrent.CountDownLatch

class GetSignLinkInteractorImplTest {

    private val api = mockk<Api>()
    private val executor = TestExecutor()

    private val interactor = GetSignLinkInteractorImpl(executor, api)

    @Test
    fun `Get Sign Link Interactor Test`() {

        val latch = CountDownLatch(1)
        val link = Link("URL", "nodesagency.cz")
        every {
            api.getSignLink(any(), any(), any(), any(), any()).execute()
        } returns Response.success(link)

        interactor.input = GetSignLinkInteractor.Input(Message("id", "s", Date(), false))
        interactor.output = object : GetSignLinkInteractor.Output {
            override fun onGetSignLink(result: Link) {
                assert(link == result)
                assert(true)
                latch.countDown()
            }

            override fun onGetSignLinkError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Get Sign Link Interactor Test Body Error`() {

        val latch = CountDownLatch(1)

        // Api to return null body for some reason
        every {
            api.getSignLink(any(), any(), any(), any(), any()).execute()
        } returns Response.success(null)

        interactor.input = GetSignLinkInteractor.Input(Message("id", "s", Date(), false))
        interactor.output = object : GetSignLinkInteractor.Output {
            override fun onGetSignLink(result: Link) {
                assert(false)
                latch.countDown()
            }

            override fun onGetSignLinkError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Get Sign Link Interactor Test Exception Error`() {

        val latch = CountDownLatch(1)

        // Api to return null body for some reason
        every { api.getSignLink(any(), any(), any(), any(), any()).execute() } throws Exception()

        interactor.input = GetSignLinkInteractor.Input(Message("id", "s", Date(), false))
        interactor.output = object : GetSignLinkInteractor.Output {
            override fun onGetSignLink(result: Link) {
                assert(false)
                latch.countDown()
            }

            override fun onGetSignLinkError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }
}