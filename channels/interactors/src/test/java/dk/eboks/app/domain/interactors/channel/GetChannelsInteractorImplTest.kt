package dk.eboks.app.domain.interactors.channel

import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.ChannelsRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.CountDownLatch

class GetChannelsInteractorImplTest {

    private val executor = TestExecutor()
    private val channelsRepository = mockk<ChannelsRepository>()
    private val interactor = GetChannelsInteractorImpl(executor, channelsRepository)

    @After
    fun tearDown() {
        interactor.input = null
        interactor.output = null
    }

    @Test
    fun `Test success`() {
        val listOf = listOf<Channel>(mockk(relaxed = true), mockk(relaxed = true))
        every { channelsRepository.getChannels(any()) } returns listOf
        val latch = CountDownLatch(1)
        interactor.output = object : GetChannelsInteractor.Output {
            override fun onGetChannels(channels: List<Channel>) {
                assertEquals(channels, listOf)
                latch.countDown()
            }

            override fun onGetChannelsError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test failure`() {
        val exception = Exception()
        every { channelsRepository.getChannels(any()) } throws exception
        val latch = CountDownLatch(1)
        interactor.output = object : GetChannelsInteractor.Output {
            override fun onGetChannels(channels: List<Channel>) {
                assert(false)
                latch.countDown()
            }

            override fun onGetChannelsError(error: ViewError) {
                assertEquals(error, interactor.exceptionToViewError(exception))
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }
}