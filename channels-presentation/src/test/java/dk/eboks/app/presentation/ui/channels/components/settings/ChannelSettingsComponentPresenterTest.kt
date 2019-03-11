package dk.eboks.app.presentation.ui.channels.components.settings

import dk.eboks.app.domain.interactors.channel.GetChannelInteractor
import dk.eboks.app.domain.interactors.channel.UninstallChannelInteractor
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxAccountLinkInteractor
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxCreditCardInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxCardLinkInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxCreditCardsInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxProfileInteractor
import dk.eboks.app.domain.interactors.storebox.PutStoreboxProfileInteractor
import dk.eboks.app.domain.interactors.storebox.UpdateStoreboxFlagsInteractor
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.channel.ChannelFlags
import dk.eboks.app.domain.models.channel.storebox.StoreboxCreditCard
import dk.eboks.app.domain.models.channel.storebox.StoreboxProfile
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.shared.Link
import dk.eboks.app.presentation.base.ViewController
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class ChannelSettingsComponentPresenterTest {

    private lateinit var presenter: ChannelSettingsComponentPresenter
    private val viewMock = mockk<ChannelSettingsComponentContract.View>(relaxUnitFun = true)
    private val storeboxCreditCardsInteractor =
        mockk<GetStoreboxCreditCardsInteractor>(relaxUnitFun = true)
    private val deleteStoreboxCreditCardInteractor =
        mockk<DeleteStoreboxCreditCardInteractor>(relaxUnitFun = true)
    private val getStoreboxProfileInteractor =
        mockk<GetStoreboxProfileInteractor>(relaxUnitFun = true)
    private val putStoreboxProfileInteractor =
        mockk<PutStoreboxProfileInteractor>(relaxUnitFun = true)
    private val getStoreboxCardLinkInteractor =
        mockk<GetStoreboxCardLinkInteractor>(relaxUnitFun = true)
    private val deleteStoreboxAccountLinkInteractor =
        mockk<DeleteStoreboxAccountLinkInteractor>(relaxUnitFun = true)
    private val updateStoreboxFlagsInteractor =
        mockk<UpdateStoreboxFlagsInteractor>(relaxUnitFun = true)
    private val getChannelInteractor = mockk<GetChannelInteractor>(relaxUnitFun = true)
    private val uninstallChannelInteractor = mockk<UninstallChannelInteractor>(relaxUnitFun = true)
    private val viewController = mockk<ViewController>(relaxUnitFun = true)

    @Before
    fun setUp() {
        presenter = ChannelSettingsComponentPresenter(
            storeboxCreditCardsInteractor,
            deleteStoreboxCreditCardInteractor,
            getStoreboxProfileInteractor,
            putStoreboxProfileInteractor,
            getStoreboxCardLinkInteractor,
            deleteStoreboxAccountLinkInteractor,
            updateStoreboxFlagsInteractor,
            getChannelInteractor,
            uninstallChannelInteractor,
            viewController
        )
        presenter.onViewCreated(viewMock, mockk(relaxUnitFun = true))
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test get credit cards`() {
        presenter.getCreditCards()
        verify {
            storeboxCreditCardsInteractor.run()
        }
    }

    @Test
    fun `Test delete credit card`() {
        val cardId = "cardString"
        presenter.deleteCreditCard(cardId)
        verify {
            deleteStoreboxCreditCardInteractor.input =
                DeleteStoreboxCreditCardInteractor.Input(cardId)
            deleteStoreboxCreditCardInteractor.run()
        }
    }

    @Test
    fun `Test get storebox profile`() {
        presenter.getStoreboxProfile()
        verify {
            getStoreboxProfileInteractor.run()
        }
    }

    @Test
    fun `Test save storebox profile`() {
        val storeboxProfile = StoreboxProfile()
        presenter.saveStoreboxProfile(storeboxProfile)
        verify {
            putStoreboxProfileInteractor.input = PutStoreboxProfileInteractor.Input(storeboxProfile)
            putStoreboxProfileInteractor.run()
        }
    }

    @Test
    fun `Test get storebox card link`() {
        presenter.getStoreboxCardLink()
        verify {
            viewMock.showProgress(true)
            getStoreboxCardLinkInteractor.run()
        }
    }

    @Test
    fun `Test delete storebox account link`() {
        presenter.deleteStoreboxAccountLink()
        verify {
            viewMock.showProgress(true)
            deleteStoreboxAccountLinkInteractor.run()
        }
    }

    @Test
    fun `Test update channel flags`() {
        val channel = mockk<Channel>(relaxed = true)
        val flags = mockk<ChannelFlags>(relaxed = true)
        presenter.updateChannelFlags(channel, flags)
        verify {
            updateStoreboxFlagsInteractor.input =
                UpdateStoreboxFlagsInteractor.Input(channel.id, flags)
            updateStoreboxFlagsInteractor.run()
        }
    }

    @Test
    fun `Test remove channel`() {
        val channel = mockk<Channel>(relaxed = true)
        every { channel.id } returns 50
        presenter.currentChannel = channel
        presenter.removeChannel()
        verify {
            viewMock.showProgress(true)
            uninstallChannelInteractor.input = UninstallChannelInteractor.Input(50)
            uninstallChannelInteractor.run()
        }
    }

    @Test
    fun `Test on get cards successful`() {
        val list = listOf<StoreboxCreditCard>(mockk(relaxed = true))
        presenter.onGetCardsSuccessful(list)
        verify {
            viewMock.showProgress(false)
            viewMock.setCreditCards(list)
        }
    }

    @Test
    fun `Test on get cards error`() {
        val error = ViewError()
        presenter.onGetCardsError(error)
        verify {
            viewMock.showProgress(false)
            viewMock.showErrorDialog(error)
        }
    }

    @Test
    fun `Test on delete card success`() {
        presenter.onDeleteCardSuccess(true)
        verify {
            storeboxCreditCardsInteractor.run()
        }
    }

    @Test
    fun `Test on delete card error`() {
        val error = ViewError()
        presenter.onDeleteCardError(error)
        verify {
            viewMock.showProgress(false)
            viewMock.showErrorDialog(error)
        }
    }

    @Test
    fun `Test on get profile`() {
        val storeboxProfile = mockk<StoreboxProfile>()
        every { storeboxProfile.greenProfile } returns false
        presenter.onGetProfile(storeboxProfile)
        verify {
            viewMock.setOnlyDigitalReceipts(false)
            storeboxCreditCardsInteractor.run()
        }
    }

    @Test
    fun `Test on get profile error`() {
        val error = ViewError()
        presenter.onGetProfileError(error)
        verify {
            viewMock.showProgress(false)
            viewMock.showErrorDialog(error)
        }
    }

    @Test
    fun `Test on put profile`() {
        presenter.onPutProfile()
        verify {
            viewMock.showProgress(false)
        }
    }

    @Test
    fun `Test on put profile error`() {
        val error = ViewError()
        presenter.onPutProfileError(error)
        verify {
            viewMock.showProgress(false)
            viewMock.showErrorDialog(error)
        }
    }

    @Test
    fun `Test on get storebox card link`() {
        val link = Link()
        presenter.onGetStoreboxCardLink(link)
        verify {
            viewMock.showAddCardView(link)
        }
    }

    @Test
    fun `Test on get storebox card link error`() {
        val error = ViewError()
        presenter.onGetStoreboxCardLinkError(error)
        verify {
            viewMock.showProgress(false)
            viewMock.showErrorDialog(error)
        }
    }

    @Test
    fun `Test on storebox account link delete`() {
        presenter.onStoreboxAccountLinkDelete()
        verify {
            viewController.refreshChannelComponent = true
            viewMock.showProgress(false)
            viewMock.broadcastCloseChannel()
            viewMock.closeView()
        }
    }

    @Test
    fun `Test on storebox account link delete error`() {
        val error = ViewError()
        presenter.onStoreboxAccountLinkDeleteError(error)
        verify {
            viewMock.showProgress(false)
            viewMock.showErrorDialog(error)
        }
    }

    @Test
    fun `Test on update flags success`() {
        presenter.onUpdateFlagsSuccess()
        verify {
            viewController.refreshChannelComponent = true
        }
    }

    @Test
    fun `Test on update flags error`() {
        val error = ViewError()
        presenter.onUpdateFlagsError(error)
        verify { viewMock.showErrorDialog(error) }
    }

    @Test
    fun `Test on get channel`() {
        val mockChannel = mockk<Channel>(relaxed = true)
        presenter.onGetChannel(mockChannel)
        verify {
            presenter.currentChannel = mockChannel
            viewMock.setupChannel(mockChannel)
        }
    }

    @Test
    fun `Test on get channel error`() {
        val error = ViewError()
        presenter.onGetChannelError(error)
        verify {
            viewMock.showErrorDialog(error)
        }
    }

    @Test
    fun `Test on uninstall channel`() {
        presenter.onUninstallChannel()
        verify {
            viewController.refreshChannelComponent = true
            viewMock.run {
                showProgress(false)
                broadcastCloseChannel()
                closeView()
            }
        }
    }

    @Test
    fun `Test on uninstall channel error`() {
        val error = ViewError()
        presenter.onUninstallChannelError(error)
        verify {
            viewMock.showProgress(false)
            viewMock.showErrorDialog(error)
        }
    }
}