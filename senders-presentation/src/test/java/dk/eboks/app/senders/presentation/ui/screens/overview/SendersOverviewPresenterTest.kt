package dk.eboks.app.senders.presentation.ui.screens.overview

import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.senders.interactors.GetCollectionsInteractor
import dk.eboks.app.domain.senders.interactors.GetSenderCategoriesInteractor
import dk.eboks.app.domain.senders.interactors.register.GetPendingInteractor
import dk.eboks.app.domain.senders.interactors.register.RegisterInteractor
import dk.eboks.app.domain.senders.interactors.register.UnRegisterInteractor
import dk.nodes.arch.presentation.base.runBlockingTest
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

@UseExperimental(InternalCoroutinesApi::class)
class SendersOverviewPresenterTest {

    private lateinit var presenter: SendersOverviewPresenter
    private val collectionsInteractor = mockk<GetCollectionsInteractor>(relaxUnitFun = true)
    private val getPendingInteractor = mockk<GetPendingInteractor>(relaxUnitFun = true)
    private val registerInteractor = mockk<RegisterInteractor>(relaxUnitFun = true)
    private val unRegisterInteractor = mockk<UnRegisterInteractor>(relaxUnitFun = true)
    private val getSenderCategoriesInteractor =
        mockk<GetSenderCategoriesInteractor>(relaxUnitFun = true)
    private val viewMock = mockk<SendersOverviewContract.View>(relaxUnitFun = true)

    @Before
    fun setUp() {
        presenter = SendersOverviewPresenter(
            collectionsInteractor,
            getPendingInteractor,
            registerInteractor,
            unRegisterInteractor,
            getSenderCategoriesInteractor
        )
        runBlockingTest(presenter) {
            presenter.onViewCreated(viewMock, mockk(relaxUnitFun = true))
        }
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test init`() {
        verify {
            collectionsInteractor.output = presenter
            registerInteractor.output = presenter
            unRegisterInteractor.output = presenter
            getPendingInteractor.output = presenter
            getSenderCategoriesInteractor.output = presenter
            getSenderCategoriesInteractor.input = GetSenderCategoriesInteractor.Input(true)
            collectionsInteractor.input = GetCollectionsInteractor.Input(false)
        }
        coVerify {
            collectionsInteractor.run()
            getPendingInteractor.run()
            getSenderCategoriesInteractor.run()
        }
    }

    @Test
    fun `Test on get collections`() {
        val collectionMock1 = mockk<CollectionContainer>()
        val collectionMock2 = mockk<CollectionContainer>()
        every { collectionMock1.type } returns "type"
        every { collectionMock2.type } returns "type2"
        val collections = listOf(collectionMock1, collectionMock2)
        presenter.onGetCollections(collections)
        verify {
            viewMock.showCollections(collections)
        }
    }

    @Test
    fun `Test on registrations loaded`() {
        // Test empty
        presenter.onRegistrationsLoaded(listOf())
        verify { viewMock.hidePendingRegistrations() }
        // Test not empty
        val registrations = listOf<CollectionContainer>(mockk())
        presenter.onRegistrationsLoaded(registrations)
        verify { viewMock.showPendingRegistrations(registrations) }
    }

    @Test
    fun `Test unregister sender`() {
        val senderId = Random.nextLong()
        presenter.unregisterSender(senderId)
        verify {
            unRegisterInteractor.inputSender = UnRegisterInteractor.InputSender(senderId)
            unRegisterInteractor.run()
        }
    }

    @Test
    fun `Test register sender`() {
        val senderId = Random.nextLong()
        presenter.registerSender(senderId)
        verify {
            registerInteractor.inputSender = RegisterInteractor.InputSender(senderId)
            registerInteractor.run()
        }
    }

    @Test
    fun `Test on success`() {
        presenter.onSuccess()
        verify {
            viewMock.showSuccess()
        }
    }

    @Test
    fun `Test on get categories`() {
        val list = listOf<SenderCategory>(mockk())
        presenter.onGetCategories(list)
        verify {
            viewMock.showCategories(list)
        }
    }

    @Test
    fun `Test on get categories error`() {
        val error = ViewError()
        presenter.onGetCategoriesError(error)
        verify {
            viewMock.showError(error.message ?: "")
        }
    }

    @Test
    fun `Test on error`() {
        val error = ViewError()
        presenter.onError(error)
        verify {
            viewMock.showError(error.message ?: "")
        }
    }
}