package dk.eboks.app.presentation.ui.message.sheet

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class MessageSheetPresenter @Inject constructor(val stateManager: AppStateManager) : MessageSheetContract.Presenter, BasePresenterImpl<MessageSheetContract.View>() {
    init {
        Timber.e("Current message ${stateManager.state?.currentMessage}")
        runAction { v->
            v.addHeaderComponentFragment()
            v.addNotesComponentFragment()
            v.addAttachmentsComponentFragment()
            v.addFolderInfoComponentFragment()
        }
    }


}