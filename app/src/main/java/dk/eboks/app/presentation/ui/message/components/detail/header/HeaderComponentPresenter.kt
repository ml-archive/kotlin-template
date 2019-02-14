package dk.eboks.app.presentation.ui.message.components.detail.header

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class HeaderComponentPresenter @Inject constructor(private val appState: AppStateManager) :
    HeaderComponentContract.Presenter, BasePresenterImpl<HeaderComponentContract.View>() {

    init {
        runAction { v ->
            appState.state?.currentMessage?.let { v.updateView(it) }
        }
    }
}