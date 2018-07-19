package dk.eboks.app.presentation.ui.uploads.components

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class UploadOverviewComponentPresenter @Inject constructor(val appState: AppStateManager) : UploadOverviewComponentContract.Presenter, BasePresenterImpl<UploadOverviewComponentContract.View>() {

    init {
    }


    override fun poisonAccessToken() {
        Timber.e("Poisoning the well, run Timmy! go find Lassie")
        appState.state?.loginState?.token?.access_token = "THISTOKENISRUINEDLOL"
        appState.state?.loginState?.token?.refresh_token = "REFRESHTOKENEVENMORERUINED"
    }
}