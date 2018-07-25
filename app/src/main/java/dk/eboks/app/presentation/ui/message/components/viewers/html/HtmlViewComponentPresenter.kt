package dk.eboks.app.presentation.ui.message.components.viewers.html

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class HtmlViewComponentPresenter @Inject constructor(val appState: AppStateManager) : HtmlViewComponentContract.Presenter, BasePresenterImpl<HtmlViewComponentContract.View>() {

    init {
    }

    override fun setup(uriString: String?) {
        uriString?.let {
            runAction { v->
                v.showHtmlURI(uriString)
            }
        }.guard {
            appState.state?.currentViewerFileName?.let { filename->
                runAction { v->
                    v.showHtml(filename)
                }
            }
        }
    }
}