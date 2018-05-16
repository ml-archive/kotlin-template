package dk.eboks.app.presentation.ui.components.channels.content.ekey.pin

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class EkeyPinComponentPresenter @Inject constructor(val appState: AppStateManager) : EkeyPinComponentContract.Presenter, BasePresenterImpl<EkeyPinComponentContract.View>() {

    init {
    }

}