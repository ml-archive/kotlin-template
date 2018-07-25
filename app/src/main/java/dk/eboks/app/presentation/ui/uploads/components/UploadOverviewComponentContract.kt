package dk.eboks.app.presentation.ui.uploads.components

import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.StorageInfo
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface UploadOverviewComponentContract {
    interface View : BaseView {
        fun setupView(verifiedUser : Boolean)
        fun showStorageInfo(storageInfo: StorageInfo)
        fun showLatestUploads(messages : List<Message>)
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
        fun refresh()
        fun poisonAccessToken()
    }
}