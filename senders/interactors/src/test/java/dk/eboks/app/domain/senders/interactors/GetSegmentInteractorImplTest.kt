package dk.eboks.app.domain.senders.interactors

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Segment
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.lang.Exception
import java.util.concurrent.CountDownLatch

class GetSegmentInteractorImplTest {

    private val executor = TestExecutor()
    private val api = mockk<Api>()
    private val interactor = GetSegmentInteractorImpl(executor, api)

    @After
    fun tearDown() {
        interactor.input = null
        interactor.output = null
    }

    @Test
    fun `Test success`() {
        val segmentId: Long = 10
        every { api.getSegmentDetail(segmentId).execute() } returns Response.success(
            Segment(
                segmentId,
                "",
                ""
            )
        )
        interactor.input = GetSegmentInteractor.Input(segmentId)
        val latch = CountDownLatch(1)
        interactor.output = object : GetSegmentInteractor.Output {
            override fun onGetSegment(segment: Segment) {
                Assert.assertEquals(segment.id, segmentId)
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
    fun `Test failure`() {
        val segmentId: Long = 10
        every { api.getSegmentDetail(segmentId).execute() } throws Exception()
        interactor.input = GetSegmentInteractor.Input(segmentId)
        val latch = CountDownLatch(1)
        interactor.output = object : GetSegmentInteractor.Output {
            override fun onGetSegment(segment: Segment) {
                assert(false)
                latch.countDown()
            }

            override fun onError(error: ViewError) {
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test no args failure`() {
        val latch = CountDownLatch(1)
        interactor.output = object : GetSegmentInteractor.Output {
            override fun onGetSegment(segment: Segment) {
                assert(false)
                latch.countDown()
            }

            override fun onError(error: ViewError) {
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }
}