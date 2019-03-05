package dk.eboks.app.presentation.ui.channels.components.overview

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.channel.GetChannelsInteractor
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.channel.ChannelColor
import dk.eboks.app.domain.models.local.ViewError
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class ChannelOverviewComponentPresenterTest {

    private lateinit var presenter: ChannelOverviewComponentPresenter
    private val getChannelsInteractor = mockk<GetChannelsInteractor>(relaxUnitFun = true)
    private val viewMock = mockk<ChannelOverviewComponentContract.View>(relaxUnitFun = true)
    private val lifecycleMock = mockk<Lifecycle>(relaxUnitFun = true)

    @Before
    fun setUp() {
        presenter = ChannelOverviewComponentPresenter(getChannelsInteractor)
        presenter.onViewCreated(viewMock, lifecycleMock)
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test refresh`() {
        val cached = true
        presenter.refresh(cached)
        verify {
            getChannelsInteractor.input = GetChannelsInteractor.Input(cached)
            getChannelsInteractor.run()
        }
    }

    @Test
    fun `Test open channel`() {
        val channelMock = createMockChannel()
        presenter.openChannel(channelMock)
        verify {
            viewMock.showChannelOpening(channelMock)
        }
    }

    @Test
    fun `Test on get channels`() {
        val channelListMock =
            listOf(createMockChannel(), createMockChannel(12), createMockChannel(51))
        presenter.onGetChannels(channelListMock)
        verify {
            viewMock.showChannels(channelListMock)
            viewMock.showProgress(false)
        }
    }

    @Test
    fun `Test on get channels error`() {
        val viewError = ViewError()
        presenter.onGetChannelsError(viewError)
        verify {
            viewMock.showProgress(false)
            viewMock.showErrorDialog(viewError)
        }
    }

    private fun createMockChannel(channelId: Int = 15): Channel {
        return Channel(
            id = channelId,
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