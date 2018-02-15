package dk.eboks.app.presentation.ui.components.message.viewers.text

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class TextViewComponentPresenter @Inject constructor(val appState: AppStateManager) : TextViewComponentContract.Presenter, BasePresenterImpl<TextViewComponentContract.View>() {

    init {
    }

}