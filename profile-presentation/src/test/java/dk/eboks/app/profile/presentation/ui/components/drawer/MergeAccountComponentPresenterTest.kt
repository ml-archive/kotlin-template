package dk.eboks.app.profile.presentation.ui.components.drawer

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.AppState
import dk.eboks.app.domain.models.login.VerificationState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MergeAccountComponentPresenterTest {

    private val appStateManager: AppStateManager = mockk(relaxUnitFun = true)

    private val view: MergeAccountComponentContract.View = mockk(relaxUnitFun = true)
    private val lifecycle: Lifecycle = mockk(relaxUnitFun = true)

    private val presenter = MergeAccountComponentPresenter(appStateManager)

    @Test
    fun `Set Merge Status Test`() {

        val verificationState = VerificationState("ud")
        val appState = AppState(verificationState = verificationState)
        every { appStateManager.state } returns appState
        presenter.onViewCreated(view, lifecycle)

        presenter.setMergeStatus(true)
        assert(appState.verificationState?.shouldMergeProfiles == true)

        presenter.setMergeStatus(false)
        assert(appState.verificationState?.shouldMergeProfiles == false)

        verify(exactly = 2) { view.close() }
    }
}