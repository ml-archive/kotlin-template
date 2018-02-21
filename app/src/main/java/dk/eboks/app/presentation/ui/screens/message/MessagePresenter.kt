package dk.eboks.app.presentation.ui.screens.message

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class MessagePresenter @Inject constructor(val appState: AppStateManager) : MessageContract.Presenter, BasePresenterImpl<MessageContract.View>() {

    init {
        Timber.e("Current message ${appState.state?.currentMessage}")
        val message = appState.state?.currentMessage
        runAction { v->
            message?.let { v.showTitle(it) }
        }
    }

}