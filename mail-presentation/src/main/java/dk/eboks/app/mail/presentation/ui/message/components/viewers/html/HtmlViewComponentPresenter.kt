package dk.eboks.app.mail.presentation.ui.message.components.viewers.html

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class HtmlViewComponentPresenter @Inject constructor(private val appState: AppStateManager) :
    HtmlViewComponentContract.Presenter, BasePresenterImpl<HtmlViewComponentContract.View>() {

    init {
    }

    override fun setup(uriString: String?) {
        uriString?.let {
            view { showHtmlURI(uriString) }
        }.guard {
            appState.state?.currentViewerFileName?.let { filename ->
                view { showHtml(filename) }
            }
        }
    }
}