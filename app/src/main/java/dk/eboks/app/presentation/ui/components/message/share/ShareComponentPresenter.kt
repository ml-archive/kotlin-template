package dk.eboks.app.presentation.ui.components.message.share

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Message
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ShareComponentPresenter @Inject constructor(val appState: AppStateManager) :
        ShareComponentContract.Presenter,
        BasePresenterImpl<ShareComponentContract.View>()
{

    init {
        runAction { v->
            appState.state?.currentMessage?.let { v.updateView(it) }
        }
    }

    override fun openExternalViewer(message: Message) {
        appState.state?.currentViewerFileName?.let { filename->
            val mime = message.content?.mimeType ?: "*/*"
            Timber.e("Share mime type $mime")
            runAction { v-> v.openExternalViewer(filename, mime) }
        }
        .guard {
            Timber.e("External viewer has no filename")
        }
    }

}