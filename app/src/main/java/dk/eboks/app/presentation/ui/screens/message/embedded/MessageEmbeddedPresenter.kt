package dk.eboks.app.presentation.ui.screens.message.embedded

import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class MessageEmbeddedPresenter @Inject constructor(val stateManager: AppStateManager) : MessageEmbeddedContract.Presenter, BasePresenterImpl<MessageEmbeddedContract.View>() {
    var message: Message? = null

    override fun setup() {
        Timber.e("Current message ${stateManager.state?.currentMessage}")
        message = stateManager.state?.currentMessage
        setupDrawerHeader()
        startViewer()
        runAction { v->
            v.addHeaderComponentFragment()
            if (BuildConfig.ENABLE_REPLY) {
                message?.reply?.let {
                    v.addReplyButtonComponentFragment(message!!)
                }
            }
            v.addNotesComponentFragment()
            if(message?.attachments != null)
                v.addAttachmentsComponentFragment()
            v.addShareComponentFragment()
            v.addFolderInfoComponentFragment()
            message?.let { v.showTitle(it) }
        }
    }

    private fun setupDrawerHeader() {
        if (message?.numberOfAttachments?:0 > 0)
        runAction { v-> v.setHighPeakHeight() }
    }

    fun startViewer()
    {
        message?.content?.mimeType?.let { mimetype ->
            if(mimetype.startsWith("image/", true))
            {
                runAction { v-> v.addImageViewer() }
                return
            }
            if(mimetype == "application/pdf")
            {
                runAction { v-> v.addPdfViewer() }
                return
            }
            if(mimetype == "text/html")
            {
                runAction { v-> v.addHtmlViewer() }
                return
            }
            if(mimetype.startsWith("text/", true))
            {
                runAction { v-> v.addTextViewer() }
                return
            }
        }
    }


}