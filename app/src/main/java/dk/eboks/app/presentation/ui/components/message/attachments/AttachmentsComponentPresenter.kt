package dk.eboks.app.presentation.ui.components.message.attachments

import dk.eboks.app.domain.interactors.message.OpenAttachmentInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Content
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class AttachmentsComponentPresenter @Inject constructor(val appState: AppStateManager, val openAttachmentInteractor: OpenAttachmentInteractor) :
        AttachmentsComponentContract.Presenter,
        BasePresenterImpl<AttachmentsComponentContract.View>(),
        OpenAttachmentInteractor.Output
{

    init {
        openAttachmentInteractor.output = this
        runAction { v->
            appState.state?.currentMessage?.let { v.updateView(it) }
        }
    }

    override fun openAttachment(content: Content) {
        openAttachmentInteractor.input = OpenAttachmentInteractor.Input(content)
        openAttachmentInteractor.run()
    }

    override fun onOpenAttachment(filename: String, mimeType: String) {
        runAction { v->
            v.openExternalViewer(filename, mimeType)
        }
    }

    override fun onOpenAttachmentError(msg: String) {
        Timber.e(msg)
    }
}