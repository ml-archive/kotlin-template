package dk.eboks.app.presentation.ui.message.components.detail.attachments

import dk.eboks.app.domain.interactors.message.OpenAttachmentInteractor
import dk.eboks.app.domain.interactors.message.SaveAttachmentInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Content
import dk.eboks.app.domain.models.message.Message
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
    private val message: Message? = appState.state?.currentMessage

    init {
        openAttachmentInteractor.output = this
        saveAttachmentInteractor.output = this
        runAction { v->
            message?.let { v.updateView(it) }
        }
    }

    override fun openAttachment(content: Content) {
        message?.let {
            openAttachmentInteractor.input = OpenAttachmentInteractor.Input(it, content)
            openAttachmentInteractor.run()
        }
    }

    override fun saveAttachment(content: Content) {
        message?.let {
            saveAttachmentInteractor.input = SaveAttachmentInteractor.Input(it, content)
            saveAttachmentInteractor.run()
        }
    }

    override fun onOpenAttachment(attachment: Content, filename: String, mimeType: String) {
        runAction { v->
            v.openExternalViewer(attachment, filename, mimeType)
        }
    }

    override fun onOpenAttachmentError(error : ViewError) {
        runAction { it.showErrorDialog(error) }
    }

    override fun onSaveAttachment(filename: String) {
        runAction { v->v.showToast("_Attachment $filename saved to Downloads") }
        Timber.e("Saved attachment to $filename")
    }

    override fun onSaveAttachmentError(error : ViewError) {
        runAction { it.showErrorDialog(error) }
    }
}