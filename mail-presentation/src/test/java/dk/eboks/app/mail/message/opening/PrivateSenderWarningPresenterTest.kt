package dk.eboks.app.mail.message.opening

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.mail.presentation.ui.message.components.opening.privatesender.PrivateSenderWarningComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.opening.privatesender.PrivateSenderWarningComponentPresenter
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class PrivateSenderWarningPresenterTest {

    private val appStateManager : AppStateManager = mockk(relaxUnitFun = true)
    private val executor : Executor = TestExecutor()

    private val view : PrivateSenderWarningComponentContract.View = mockk(relaxUnitFun = true)
    private val lifecycle : Lifecycle = mockk(relaxUnitFun = true)

    private lateinit var presenter: PrivateSenderWarningComponentPresenter


    @Before
    fun setUp() {
        presenter = PrivateSenderWarningComponentPresenter(appStateManager, executor)
        presenter.onViewCreated(view, lifecycle)
    }


    @Test
    fun `Test Set Should Proceed`() {

        every { appStateManager.state } returns AppState()

        presenter.setShouldProceed(true)
        verify {
            view.showOpeningProgress(true)
            executor.signal("messageOpenDone")
        }
    }
}