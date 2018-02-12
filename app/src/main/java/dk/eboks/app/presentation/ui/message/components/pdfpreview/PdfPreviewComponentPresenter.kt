package dk.eboks.app.presentation.ui.message.components.pdfpreview

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class PdfPreviewComponentPresenter @Inject constructor(val appState: AppStateManager) : PdfPreviewComponentContract.Presenter, BasePresenterImpl<PdfPreviewComponentContract.View>() {

    init {
        runAction { v->

        }
    }

}