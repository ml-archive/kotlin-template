package dk.eboks.app.presentation.ui.components.message.viewers.pdf

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class PdfViewComponentPresenter @Inject constructor(val appState: AppStateManager) : PdfViewComponentContract.Presenter, BasePresenterImpl<PdfViewComponentContract.View>() {

    init {
        runAction { v->

        }
    }

}