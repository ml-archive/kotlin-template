package dk.eboks.app.mail.presentation.ui.message.components.detail.attachments

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Content
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.mail.domain.interactors.message.OpenAttachmentInteractor
import dk.eboks.app.mail.domain.interactors.message.SaveAttachmentInteractor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class AttachmentsComponentPresenter @Inject constructor(
    appState: AppStateManager,
    private val openAttachmentInteractor: OpenAttachmentInteractor,
    private val saveAttachmentInteractor: SaveAttachmentInteractor
) :
    AttachmentsComponentContract.Presenter,
    BasePresenterImpl<AttachmentsComponentContract.View>(),
    OpenAttachmentInteractor.Output,
    SaveAttachmentInteractor.Output {
    private val message: Message? = appState.state?.currentMessage

    init {
        openAttachmentInteractor.output = this
        saveAttachmentInteractor.output = this
        view { message?.let(::updateView) }
    }

    override fun openAttachment(content: Content) {
        openAttachmentInteractor.input = OpenAttachmentInteractor.Input(message ?: return, content)
        openAttachmentInteractor.run()
    }

    override fun saveAttachment(content: Content) {
        saveAttachmentInteractor.input = SaveAttachmentInteractor.Input(message ?: return, content)
        saveAttachmentInteractor.run()
    }

    override fun onOpenAttachment(attachment: Content, filename: String, mimeType: String) {
        view { openExternalViewer(attachment, filename, mimeType) }
    }

    override fun onOpenAttachmentError(error: ViewError) {
        view { showErrorDialog(error) }
    }

    override fun onSaveAttachment(filename: String) {
        view { showToast("_Attachment $filename saved to Downloads") }
        Timber.e("Saved attachment to $filename")
    }

    override fun onSaveAttachmentError(error: ViewError) {
        view { showErrorDialog(error) }
    }
}