package dk.eboks.app.presentation.ui.components.message.detail.notes

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class NotesComponentPresenter @Inject constructor(val appState: AppStateManager) : NotesComponentContract.Presenter, BasePresenterImpl<NotesComponentContract.View>() {

    init {
        runAction { v->
            appState.state?.currentMessage?.let { v.updateView(it) }
        }
    }

}