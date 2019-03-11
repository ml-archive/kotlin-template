package dk.eboks.app.presentation.ui.channels.screens.content.storebox

import dk.eboks.app.domain.interactors.storebox.ConfirmStoreboxInteractor
import dk.eboks.app.domain.interactors.storebox.CreateStoreboxInteractor
import dk.eboks.app.domain.interactors.storebox.LinkStoreboxInteractor
import dk.eboks.app.domain.models.local.ViewError
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class ConnectStoreboxPresenterTest {

    private lateinit var presenter: ConnectStoreboxPresenter
    private val linkStoreboxInteractor = mockk<LinkStoreboxInteractor>(relaxUnitFun = true)
    private val confirmStoreboxInteractor = mockk<ConfirmStoreboxInteractor>(relaxUnitFun = true)
    private val createStoreboxInteractor = mockk<CreateStoreboxInteractor>(relaxUnitFun = true)
    private val viewMock = mockk<ConnectStoreboxContract.View>(relaxUnitFun = true)

    @Before
    fun setUp() {
        presenter = ConnectStoreboxPresenter(
            linkStoreboxInteractor,
            confirmStoreboxInteractor,
            createStoreboxInteractor
        )
        presenter.onViewCreated(viewMock, mockk(relaxUnitFun = true))
    }

    @After
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun `Test sign in`() {
        val email = "email"
        val password = "password"
        presenter.signIn(email, password)
        verify {
            viewMock.showProgress(true)
            linkStoreboxInteractor.input = LinkStoreboxInteractor.Input(email, password)
            linkStoreboxInteractor.run()
        }
    }

    @Test
    fun `Test confirm`() {
        presenter.returnCode = "returnCode"
        val code = "code"
        presenter.confirm(code)
        verify {
            viewMock.showProgress(true)
            confirmStoreboxInteractor.input =
                ConfirmStoreboxInteractor.Input("returnCode", code)
            confirmStoreboxInteractor.run()
        }
    }

    @Test
    fun `Test create storebox user`() {
        presenter.createStoreboxUser()
        verify {
            viewMock.showProgress(true)
            createStoreboxInteractor.run()
        }
    }

    @Test
    fun `Test storebox account found`() {
        presenter.storeboxAccountFound(true, "code")
        verify {
            presenter.returnCode = "code"
            viewMock.showProgress(false)
            viewMock.showFound()
        }
        presenter.storeboxAccountFound(false, "code")
        verify {
            presenter.returnCode = "code"
            viewMock.showProgress(false)
            viewMock.showNotFound()
        }
    }

    @Test
    fun `Test on linking success`() {
        presenter.onLinkingSuccess(false)
        verify {
            viewMock.run {
                showProgress(false)
                showWrongCode()
            }
        }
        presenter.onLinkingSuccess(true)
        verify {
            viewMock.showSuccess()
        }
    }

    @Test
    fun `Test on error`() {
        val error = ViewError()
        presenter.onError(error)
        verify {
            viewMock.run {
                showProgress(false)
                showErrorDialog(error)
            }
        }
    }

    @Test
    fun `Test on storebox account created`() {
        presenter.onStoreboxAccountCreated()
        verify { viewMock.finish() }
    }

    @Test
    fun `Test on storebox account exists`() {
        presenter.onStoreboxAccountExists()
        verify {
            viewMock.run {
                showProgress(false)
                showErrorDialog(ViewError())
            }
        }
    }

    @Test
    fun `Test on storebox account created error`() {
        val error = ViewError()
        presenter.onStoreboxAccountCreatedError(error)
        verify {
            viewMock.run {
                showProgress(false)
                showErrorDialog(error)
            }
        }
    }
}