package dk.eboks.app.presentation.ui.components.message.document

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class DocumentComponentPresenter @Inject constructor(val appState: AppStateManager) : DocumentComponentContract.Presenter, BasePresenterImpl<DocumentComponentContract.View>() {

    init {
        Timber.e("Current message ${appState.state?.currentMessage}")
        runAction { v->
            appState.state?.currentMessage?.let { v.updateView(it) }
        }
    }

}