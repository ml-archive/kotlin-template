package dk.eboks.app.mail.presentation.ui.message.components.opening.receipt

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class OpeningReceiptComponentPresenter @Inject constructor(
    private val appState: AppStateManager,
    private val executor: Executor
) :
    OpeningReceiptComponentContract.Presenter,
    BasePresenterImpl<OpeningReceiptComponentContract.View>() {

    init {
    }

    override fun setShouldProceed(proceed: Boolean, receipt: Boolean) {
        appState.state?.openingState?.let { state ->
            state.shouldProceedWithOpening = proceed
            state.sendReceipt = receipt
        }
        view { showOpeningProgress(true) }
        executor.signal("messageOpenDone")
    }
}