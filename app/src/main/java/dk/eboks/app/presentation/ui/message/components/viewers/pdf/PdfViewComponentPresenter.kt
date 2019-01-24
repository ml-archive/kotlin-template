package dk.eboks.app.presentation.ui.message.components.viewers.pdf

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class PdfViewComponentPresenter @Inject constructor(val appState: AppStateManager) : PdfViewComponentContract.Presenter, BasePresenterImpl<PdfViewComponentContract.View>() {

    override fun onViewCreated(view: PdfViewComponentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)
        appState.state?.currentViewerFileName?.let {filename ->
            runAction {
                it.showPdfView(filename)
            }
        }

    }
}