package dk.eboks.app.mail.presentation.ui.message.components.detail.share

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class ShareComponentPresenter @Inject constructor(private val appState: AppStateManager) :
    ShareComponentContract.Presenter,
    BasePresenterImpl<ShareComponentContract.View>() {

    init {
        view { appState.state?.currentMessage?.let(::updateView) }
    }

    override fun openExternalViewer(message: Message) {
        appState.state?.currentViewerFileName?.let { filename ->
            val mime = message.content?.mimeType ?: "*/*"
            Timber.e("Share mime type $mime")
            view { openExternalViewer(filename, mime) }
        }
            .guard {
                Timber.e("External viewer has no filename")
            }
    }
}