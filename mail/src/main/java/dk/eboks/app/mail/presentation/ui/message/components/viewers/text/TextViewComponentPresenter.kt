package dk.eboks.app.mail.presentation.ui.message.components.viewers.text

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class TextViewComponentPresenter @Inject constructor(private val appState: AppStateManager) :
    TextViewComponentContract.Presenter, BasePresenterImpl<TextViewComponentContract.View>() {

    init {
    }

    override fun setup(uriString: String?) {
        uriString?.let {
            runAction { v ->
                v.showTextURI(uriString)
            }
        }.guard {
            appState.state?.currentViewerFileName?.let { filename ->
                runAction { v ->
                    v.showText(filename)
                }
            }
        }
    }
}