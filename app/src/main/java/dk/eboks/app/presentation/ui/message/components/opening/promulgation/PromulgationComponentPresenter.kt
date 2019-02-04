package dk.eboks.app.presentation.ui.message.components.opening.promulgation

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Translation
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class PromulgationComponentPresenter @Inject constructor(
    val appState: AppStateManager,
    val executor: Executor
) :
    PromulgationComponentContract.Presenter,
    BasePresenterImpl<PromulgationComponentContract.View>() {
    init {
        runAction { v ->
            v.setPromulgationHeader(Translation.message.promulgationHeader)
            v.setPromulgationText(Translation.message.promulgationMessage)
        }
    }

    override fun setShouldProceed(proceed: Boolean) {
        appState.state?.openingState?.let { state ->
            state.shouldProceedWithOpening = proceed
        }
        executor.signal("messageOpenDone")
    }
}