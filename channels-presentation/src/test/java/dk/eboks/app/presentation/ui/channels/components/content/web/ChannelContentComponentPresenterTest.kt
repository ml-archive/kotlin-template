package dk.eboks.app.presentation.ui.channels.components.content.web

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.channel.GetChannelContentLinkInteractor
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.local.ViewError
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class ChannelContentComponentPresenterTest {

    private lateinit var presenter: ChannelContentComponentPresenter
    private val getChannelContentLinkInteractor =
        mockk<GetChannelContentLinkInteractor>(relaxUnitFun = true)
    private val viewMock = mockk<ChannelContentComponentContract.View>(relaxUnitFun = true)
    private val lifecycle = mockk<Lifecycle>(relaxUnitFun = true)
    @Before
    fun setUp() {
        presenter = ChannelContentComponentPresenter(getChannelContentLinkInteractor)
        presenter.onViewCreated(viewMock, lifecycle)
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test setup`() {
        val channel = mockk<Channel>(relaxed = true)
        presenter.setup(channel)
        verify {
            presenter.currentChannel = channel
            viewMock.showChannel(channel)
            getChannelContentLinkInteractor.input =
                GetChannelContentLinkInteractor.Input(channelId = channel.id)
            getChannelContentLinkInteractor.run()
        }
    }

    @Test
    fun `Test on get channel content link`() {
        val link = "link"
        presenter.onGetChannelContentLink(link)
        verify { viewMock.openChannelContent(link) }
    }

    @Test
    fun `Test on get channel content link error`() {
        val error = ViewError()
        presenter.onGetChannelContentLinkError(error)
        verify { viewMock.showErrorDialog(error) }
    }
}