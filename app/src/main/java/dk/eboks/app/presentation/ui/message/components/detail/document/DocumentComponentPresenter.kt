package dk.eboks.app.presentation.ui.message.components.detail.document

import dk.eboks.app.domain.interactors.message.SaveAttachmentInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Content
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class DocumentComponentPresenter @Inject constructor(
        private val appState: AppStateManager,
        private val saveAttachmentInteractor: SaveAttachmentInteractor
) :
    DocumentComponentContract.Presenter,
    BasePresenterImpl<DocumentComponentContract.View>(),
    SaveAttachmentInteractor.Output {

    init {
        saveAttachmentInteractor.output = this
        runAction { v ->
            appState.state?.currentMessage?.let { v.updateView(it) }
        }
    }

    override fun openExternalViewer(message: Message) {
        appState.state?.currentViewerFileName?.let { filename ->
            val mime = message.content?.mimeType ?: "*/*"
            Timber.e("Share mime type $mime")
            runAction { v -> v.openExternalViewer(filename, mime) }
        }
            .guard {
                Timber.e("External viewer has no filename")
            }
    }

    override fun saveAttachment(content: Content) {
        appState.state?.currentMessage?.let {
            saveAttachmentInteractor.input = SaveAttachmentInteractor.Input(it, content)
            saveAttachmentInteractor.run()
        }
    }

    override fun onSaveAttachment(filename: String) {
        runAction { v -> v.showToast("_Document $filename saved to Downloads") }
        Timber.e("Saved attachment to $filename")
    }

    override fun onSaveAttachmentError(error: ViewError) {
        runAction { it.showErrorDialog(error) }
    }
}