package dk.eboks.app.mail.message.opening

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.message.MessageOpeningState
import dk.eboks.app.mail.presentation.ui.message.components.opening.recalled.RecalledComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.opening.recalled.RecalledComponentPresenter
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class RecalledComponentPresnterTest {

    private val appStateManager: AppStateManager = mockk(relaxUnitFun = true)
    private val executor: Executor = TestExecutor()

    private val view: RecalledComponentContract.View = mockk(relaxUnitFun = true)
    private val lifecycle: Lifecycle = mockk(relaxUnitFun = true)

    private lateinit var presenter: RecalledComponentPresenter

    @Before
    fun setUp() {
        presenter = RecalledComponentPresenter(appStateManager, executor)
        presenter.onViewCreated(view, lifecycle)
    }

    @Test
    fun `Test Set Should Proceed`() {

        every { appStateManager.state } returns AppState(
            openingState = MessageOpeningState(
                shouldProceedWithOpening = false
            )
        )

        presenter.setShouldProceed(true)
        assert(appStateManager.state?.openingState?.shouldProceedWithOpening == true)

        presenter.setShouldProceed(false)
        assert(appStateManager.state?.openingState?.shouldProceedWithOpening == false)
    }
}