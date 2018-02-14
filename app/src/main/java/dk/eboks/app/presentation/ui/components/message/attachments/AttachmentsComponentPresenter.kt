package dk.eboks.app.presentation.ui.components.message.attachments

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class AttachmentsComponentPresenter @Inject constructor(val appState: AppStateManager) : AttachmentsComponentContract.Presenter, BasePresenterImpl<AttachmentsComponentContract.View>() {

    init {
        Timber.e("Current message ${appState.state?.currentMessage}")
        runAction { v->
            appState.state?.currentMessage?.let { v.updateView(it) }
        }
    }

}