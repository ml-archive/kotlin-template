package dk.eboks.app.presentation.ui.components.message.opening.protectedmessage

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ProtectedMessageComponentPresenter @Inject constructor(val appState: AppStateManager) : ProtectedMessageComponentContract.Presenter, BasePresenterImpl<ProtectedMessageComponentContract.View>() {

    init {
    }

}