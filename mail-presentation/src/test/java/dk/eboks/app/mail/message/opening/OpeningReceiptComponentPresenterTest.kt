package dk.eboks.app.mail.message.opening

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.message.MessageOpeningState
import dk.eboks.app.mail.presentation.ui.message.components.opening.recalled.RecalledComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.opening.recalled.RecalledComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.components.opening.receipt.OpeningReceiptComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.opening.receipt.OpeningReceiptComponentPresenter
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class OpeningReceiptComponentPresenterTest {

    private val appStateManager : AppStateManager = mockk(relaxUnitFun = true)
    private val executor : Executor = TestExecutor()

    private val view : OpeningReceiptComponentContract.View = mockk(relaxUnitFun = true)
    private val lifecycle : Lifecycle = mockk(relaxUnitFun = true)

    private lateinit var presenter: OpeningReceiptComponentPresenter


    @Before
    fun setUp() {
        presenter = OpeningReceiptComponentPresenter(appStateManager, executor)
        presenter.onViewCreated(view, lifecycle)
    }


    @Test
    fun `Test Set Should Proceed`() {

        every { appStateManager.state } returns AppState(openingState = MessageOpeningState(shouldProceedWithOpening = false))

        presenter.setShouldProceed(true, true)
        assert(appStateManager.state?.openingState?.shouldProceedWithOpening == true)
        assert(appStateManager.state?.openingState?.sendReceipt == true)

        presenter.setShouldProceed(false, false)
        assert(appStateManager.state?.openingState?.sendReceipt == false)
        assert(appStateManager.state?.openingState?.shouldProceedWithOpening == false)
    }
}