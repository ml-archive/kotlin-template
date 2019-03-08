package dk.eboks.app.mail.message.opening

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.message.MessageOpeningState
import dk.eboks.app.mail.presentation.ui.message.components.opening.privatesender.PrivateSenderWarningComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.opening.privatesender.PrivateSenderWarningComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.components.opening.protectedmessage.ProtectedMessageComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.opening.protectedmessage.ProtectedMessageComponentPresenter
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class ProtectedMessagePresenterTest {

    private val appStateManager : AppStateManager = mockk(relaxUnitFun = true)
    private val executor : Executor = TestExecutor()

    private val view : ProtectedMessageComponentContract.View = mockk(relaxUnitFun = true)
    private val lifecycle : Lifecycle = mockk(relaxUnitFun = true)

    private lateinit var presenter: ProtectedMessageComponentPresenter


    @Before
    fun setUp() {
        presenter = ProtectedMessageComponentPresenter(appStateManager, executor)
        presenter.onViewCreated(view, lifecycle)
    }


    @Test
    fun `Test Set Should Proceed`() {

        every { appStateManager.state } returns AppState(openingState = MessageOpeningState(shouldProceedWithOpening = false))

        presenter.setShouldProceed(true)
        assert(appStateManager.state?.openingState?.shouldProceedWithOpening == true)

        presenter.setShouldProceed(false)
        assert(appStateManager.state?.openingState?.shouldProceedWithOpening == false)
    }

}