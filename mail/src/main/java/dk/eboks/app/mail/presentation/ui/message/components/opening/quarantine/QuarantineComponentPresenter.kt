package dk.eboks.app.mail.presentation.ui.message.components.opening.quarantine

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class QuarantineComponentPresenter @Inject constructor(
    private val appState: AppStateManager,
    private val executor: Executor
) :
    QuarantineComponentContract.Presenter,
    BasePresenterImpl<QuarantineComponentContract.View>() {

    init {
    }

    override fun setShouldProceed(proceed: Boolean) {
        appState.state?.openingState?.let { state ->
            state.shouldProceedWithOpening = proceed
        }
        executor.signal("messageOpenDone")
    }
}