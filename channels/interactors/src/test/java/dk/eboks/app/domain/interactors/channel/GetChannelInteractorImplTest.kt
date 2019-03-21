package dk.eboks.app.domain.interactors.channel

import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.channel.ChannelColor
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.ChannelsRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.CountDownLatch

class GetChannelInteractorImplTest {
    private val executor = TestExecutor()
    private val channelsRepository = mockk<ChannelsRepository>()
    private val interactor = GetChannelInteractorImpl(executor, channelsRepository)

    @After
    fun tearDown() {
        interactor.input = null
        interactor.output = null
    }

    @Test
    fun `Test success`() {
        val channelMock = createMockChannel()
        every { channelsRepository.getChannel(channelMock.id) } returns channelMock
        interactor.input = GetChannelInteractor.Input(channelMock.id)
        val latch = CountDownLatch(1)
        interactor.output = object : GetChannelInteractor.Output {
            override fun onGetChannel(channel: Channel) {
                assertEquals(channel, channelMock)
                latch.countDown()
            }

            override fun onGetChannelError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test failure`() {
        every { channelsRepository.getChannel(any()) } throws Exception()
        interactor.input = GetChannelInteractor.Input(0)
        val latch = CountDownLatch(1)
        interactor.output = object : GetChannelInteractor.Output {
            override fun onGetChannel(channel: Channel) {
                assert(false)
                latch.countDown()
            }

            override fun onGetChannelError(error: ViewError) {
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test no args`() {
        val latch = CountDownLatch(1)
        interactor.output = object : GetChannelInteractor.Output {
            override fun onGetChannel(channel: Channel) {
                assert(false)
                latch.countDown()
            }

            override fun onGetChannelError(error: ViewError) {
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    private fun createMockChannel(): Channel {
        return Channel(
            id = 15,
            name = "",
            payoff = "",
            description = "",
            status = null,
            logo = null,
            image = null,
            background = ChannelColor(),
            requirements = listOf(),
            installed = true,
            pinned = true,
            supportPinned = true
        )
    }
}