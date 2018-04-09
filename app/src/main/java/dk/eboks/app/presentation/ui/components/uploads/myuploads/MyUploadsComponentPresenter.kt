package dk.eboks.app.presentation.ui.components.uploads.myuploads

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class MyUploadsComponentPresenter @Inject constructor(val appState: AppStateManager) : MyUploadsComponentContract.Presenter, BasePresenterImpl<MyUploadsComponentContract.View>() {

    init {
    }

}