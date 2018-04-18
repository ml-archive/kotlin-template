package dk.eboks.app.presentation.ui.screens.message

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class MessagePresenter @Inject constructor(val appState: AppStateManager) : MessageContract.Presenter, BasePresenterImpl<MessageContract.View>() {
    var message: Message? = null

    override fun setup() {
        Timber.e("Current message ${appState.state?.currentMessage}")
        message = appState.state?.currentMessage
        runAction { v->
            v.addHeaderComponentFragment()
            v.addDocumentComponentFragment()
            message?.reply?.let {
                v.addReplyButtonComponentFragment()
            }
            v.addNotesComponentFragment()
            if(message?.attachments != null)
                v.addAttachmentsComponentFragment()
            v.addShareComponentFragment()
            v.addFolderInfoComponentFragment()
            message?.let { v.showTitle(it) }
        }
    }
}