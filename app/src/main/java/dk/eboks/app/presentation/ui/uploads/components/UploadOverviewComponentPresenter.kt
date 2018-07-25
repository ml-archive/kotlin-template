package dk.eboks.app.presentation.ui.uploads.components

import dk.eboks.app.domain.interactors.message.GetLatestUploadsInteractor
import dk.eboks.app.domain.interactors.message.GetStorageInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.StorageInfo
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class UploadOverviewComponentPresenter @Inject constructor(
        val appState: AppStateManager,
        val getStorageInteractor: GetStorageInteractor,
        val getLatestUploadsInteractor: GetLatestUploadsInteractor
) :
        UploadOverviewComponentContract.Presenter,
        BasePresenterImpl<UploadOverviewComponentContract.View>(),
        GetStorageInteractor.Output,
        GetLatestUploadsInteractor.Output
{

    init {
        getStorageInteractor.output = this
        getLatestUploadsInteractor.output = this
    }

    override fun setup() {
        refresh()
    }

    override fun refresh() {
        appState.state?.currentUser?.let { user->
            runAction { v->v.setupView(user.verified) }
        }
        getStorageInteractor.run()
        getLatestUploadsInteractor.input = GetLatestUploadsInteractor.Input()
        getLatestUploadsInteractor.run()
    }

    override fun poisonAccessToken() {
        Timber.e("Poisoning the well, run Timmy! go find Lassie")
        appState.state?.loginState?.token?.access_token = "THISTOKENISRUINEDLOL"
        appState.state?.loginState?.token?.refresh_token = "REFRESHTOKENEVENMORERUINED"
    }

    /**
     * GetStorageInteractor callbacks
     */

    override fun onGetStorage(storageInfo: StorageInfo) {
        runAction { v->v.showStorageInfo(storageInfo) }
    }

    override fun onGetStorageError(error: ViewError) {
        runAction { v->v.showErrorDialog(error) }
    }

    override fun onGetLatestUploads(messages: List<Message>) {
        runAction { v->v.showLatestUploads(messages)}
    }

    override fun onGetLatestUploadsError(error: ViewError) {
        runAction { v->v.showErrorDialog(error) }
    }
}