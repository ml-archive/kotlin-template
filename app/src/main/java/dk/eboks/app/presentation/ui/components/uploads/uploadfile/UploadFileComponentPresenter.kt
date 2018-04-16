package dk.eboks.app.presentation.ui.components.uploads.uploadfile

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class UploadFileComponentPresenter @Inject constructor(val appState: AppStateManager) : UploadFileComponentContract.Presenter, BasePresenterImpl<UploadFileComponentContract.View>() {

    init {
    }

}