package dk.eboks.app.mail.presentation.ui.message.components.viewers.pdf

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class PdfViewComponentPresenter @Inject constructor(private val appState: AppStateManager) :
    PdfViewComponentContract.Presenter, BasePresenterImpl<PdfViewComponentContract.View>() {

    override fun onViewCreated(view: PdfViewComponentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)
        appState.state?.currentViewerFileName?.let { filename ->
            view { showPdfView(filename) }
        }
    }

    override val currentFile: String?
        get() = appState.state?.currentViewerFileName
}