package dk.eboks.app.presentation.ui.components.message.opening.promulgation

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class PromulgationComponentPresenter @Inject constructor(val appState: AppStateManager, val executor: Executor) :
        PromulgationComponentContract.Presenter,
        BasePresenterImpl<PromulgationComponentContract.View>()
{
    init {
        runAction { v ->

            //mocked string
            var mockText = "You have received a court message that you have opened or otherwise processed. The message and exhibits, if any, have been properly serviced to you.//n It is important that you thoroughly read the court message and exhibits, if any. The message may for example include a summons, a call for a court hearing, or a judicial decision that all may have certain ramifications for you. Any time limits apply as of today.The court has received a return receipt in evidence of the message being serviced to you. You will find the return receipt under the folder Sent Items."
            v.setPromulgationText(mockText)
        }
    }

    override fun setShouldProceed(proceed: Boolean) {
        appState.state?.openingState?.let { state ->
            state.shouldProceedWithOpening = proceed
        }
        executor.signal("messageOpenDone")
    }
}