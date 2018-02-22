package dk.eboks.app.presentation.ui.components.message.attachments

import dk.eboks.app.domain.interactors.message.OpenAttachmentInteractor
import dk.eboks.app.domain.interactors.message.SaveAttachmentInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Content
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class AttachmentsComponentPresenter @Inject constructor(val appState: AppStateManager, val openAttachmentInteractor: OpenAttachmentInteractor, val saveAttachmentInteractor: SaveAttachmentInteractor) :
        AttachmentsComponentContract.Presenter,
        BasePresenterImpl<AttachmentsComponentContract.View>(),
        OpenAttachmentInteractor.Output,
        SaveAttachmentInteractor.Output
{

    init {
        openAttachmentInteractor.output = this
        saveAttachmentInteractor.output = this
        runAction { v->
            appState.state?.currentMessage?.let { v.updateView(it) }
        }
    }

    override fun openAttachment(content: Content) {
        openAttachmentInteractor.input = OpenAttachmentInteractor.Input(content)
        openAttachmentInteractor.run()
    }

    override fun saveAttachment(content: Content) {
        saveAttachmentInteractor.input = SaveAttachmentInteractor.Input(content)
        saveAttachmentInteractor.run()
    }

    override fun onOpenAttachment(attachment: Content, filename: String, mimeType: String) {
        runAction { v->
            v.openExternalViewer(attachment, filename, mimeType)
        }
    }

    override fun onOpenAttachmentError(msg: String) {
        Timber.e(msg)
    }

    override fun onSaveAttachment(filename: String) {
        Timber.e("Saved attachment to $filename")
    }

    override fun onSaveAttachmentError(msg: String) {
        Timber.e("Error saving attachment: $msg")
    }
}