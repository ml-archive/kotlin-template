package dk.eboks.app.presentation.ui.channels.components.opening

import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.interactors.channel.GetChannelInteractor
import dk.eboks.app.domain.interactors.channel.InstallChannelInteractor
import dk.eboks.app.domain.interactors.storebox.CreateStoreboxInteractor
import dk.eboks.app.domain.models.APIConstants
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.channel.Requirement
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.shared.Status
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ChannelOpeningComponentPresenterTest {

    private lateinit var presenter: ChannelOpeningComponentPresenter
    private val viewMock = mockk<ChannelOpeningComponentContract.View>(relaxUnitFun = true)
    private val getChannelInteractor = mockk<GetChannelInteractor>(relaxUnitFun = true)
    private val createStoreboxInteractor = mockk<CreateStoreboxInteractor>(relaxUnitFun = true)
    private val installChannelInteractor = mockk<InstallChannelInteractor>(relaxUnitFun = true)
    private val appConfig = mockk<AppConfig>(relaxUnitFun = true)
    @Before
    fun setUp() {
        presenter = ChannelOpeningComponentPresenter(
            getChannelInteractor,
            createStoreboxInteractor,
            installChannelInteractor,
            appConfig
        )
        presenter.onViewCreated(viewMock, mockk(relaxUnitFun = true))
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test setup`() {
        val channelId = 1235
        presenter.setup(channelId)
        assertEquals(presenter.channelId, channelId)
    }

    @Test
    fun `Test refresh channel`() {
        presenter.channelId = 1235
        presenter.refreshChannel()
        verify {
            viewMock.showProgress(true)
            getChannelInteractor.input = GetChannelInteractor.Input(1235)
            getChannelInteractor.run()
        }
    }

    @Test
    fun `Test install verification required`() {
        val channel = mockk<Channel>(relaxed = true)
        every { channel.requirements } returns listOf(
            Requirement(
                name = "",
                value = "",
                type = null,
                verified = false
            )
        )
        presenter.install(channel)
        verify {
            viewMock.showRequirementsDrawer(channel)
        }
    }

    @Test
    fun `Test install storebox`() {
        val channel = mockk<Channel>(relaxed = true)
        every { channel.id } returns 1
        presenter.install(channel)
        verify {
            createStoreboxInteractor.run()
        }
    }

    @Test
    fun `Test install ekey`() {
        val channel = mockk<Channel>(relaxed = true)
        val id = 11
        every { channel.id } returns id
        presenter.install(channel)
        verify {
            viewMock.showProgress(true)
            installChannelInteractor.input = InstallChannelInteractor.Input(id)
            installChannelInteractor.run()
        }
    }

    @Test
    fun `Test install channel`() {
        val channel = mockk<Channel>(relaxed = true)
        val id = 0
        every { channel.id } returns id
        presenter.install(channel)
        verify {
            viewMock.showProgress(true)
            installChannelInteractor.input = InstallChannelInteractor.Input(id)
            installChannelInteractor.run()
        }
    }

    @Test
    fun `Test open Ekey`() {
        val channel = mockk<Channel>(relaxed = true)
        val id = 11
        every { channel.id } returns id
        presenter.open(channel)
        verify { viewMock.openEkeyContent(channel) }
    }

    @Test
    fun `Test open channel`() {
        val channel = mockk<Channel>(relaxed = true)
        val id = 0
        every { channel.id } returns id
        presenter.open(channel)
        verify { viewMock.openChannelContent(channel) }
    }

    @Test
    fun `Test open Storebox`() {
        val channel = mockk<Channel>(relaxed = true)
        val id = 1
        every { channel.id } returns id
        presenter.open(channel)
        verify { viewMock.openStoreBoxContent(channel) }
    }

    @Test
    fun `Test on get channel`() {
        val channel = mockk<Channel>(relaxed = true)
        val id = 1
        every { channel.id } returns id
        every { channel.installed } returns false
        every { channel.status } returns Status(type = APIConstants.CHANNEL_STATUS_AVAILABLE)
        presenter.onGetChannel(channel)
        verify {
            viewMock.showInstallState(channel)
        }
    }

    @Test
    fun `Test on get channel error`() {
        val error = ViewError()
        presenter.onGetChannelError(error)
        verify {
            viewMock.showProgress(false)
            viewMock.showErrorDialog(error)
        }
    }

    @Test
    fun `Test on storebox account created error`() {
        val error = ViewError()
        presenter.onStoreboxAccountCreatedError(error)
        verify {
            viewMock.showErrorDialog(error)
        }
    }

    @Test
    fun `Test on storebox account exists`() {
        presenter.onStoreboxAccountExists()
        verify { viewMock.showStoreboxUserAlreadyExists() }
    }

    @Test
    fun `Test on install channel`() {
        presenter.onInstallChannel()
        verify {
            viewMock.showProgress(false)
        }
    }

    @Test
    fun `Test on install channel error`() {
        val error = ViewError()
        presenter.onInstallChannelError(error)
        verify {
            viewMock.showProgress(false)
            viewMock.showErrorDialog(error)
        }

    }
}