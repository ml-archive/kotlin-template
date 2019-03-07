package dk.eboks.app.presentation.ui.uploads.components

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.StorageInfo
import dk.eboks.app.mail.domain.interactors.message.GetLatestUploadsInteractor
import dk.eboks.app.mail.domain.interactors.message.GetStorageInteractor
import dk.eboks.app.mail.domain.interactors.message.UploadFileInteractor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class UploadOverviewComponentPresenter @Inject constructor(
    private val appState: AppStateManager,
    private val getStorageInteractor: GetStorageInteractor,
    private val getLatestUploadsInteractor: GetLatestUploadsInteractor,
    private val uploadFileInteractor: UploadFileInteractor
) :
    UploadOverviewComponentContract.Presenter,
    BasePresenterImpl<UploadOverviewComponentContract.View>(),
    GetStorageInteractor.Output,
    GetLatestUploadsInteractor.Output,
    UploadFileInteractor.Output {

    init {
        getStorageInteractor.output = this
        getLatestUploadsInteractor.output = this
        uploadFileInteractor.output = this
    }

    override fun setup() {
        refresh()
    }

    override fun refresh() {
        appState.state?.currentUser?.let { user -> view { setupView(user.verified) } }
        getStorageInteractor.run()
        getLatestUploadsInteractor.input = GetLatestUploadsInteractor.Input()
        getLatestUploadsInteractor.run()
    }

    override fun poisonAccessToken() {
        Timber.e("Poisoning the well, run Timmy! go find Lassie")
        appState.state?.loginState?.token?.access_token = "THISTOKENISRUINEDLOL"
        appState.state?.loginState?.token?.refresh_token = "REFRESHTOKENEVENMORERUINED"
    }

    override fun upload(folderId: Int, filename: String, uriString: String, mimetype: String) {
        uploadFileInteractor.input =
            UploadFileInteractor.Input(folderId, filename, uriString, mimetype)
        uploadFileInteractor.run()
        view { showUploadProgress() }
    }

    /**
     * GetStorageInteractor callbacks
     */

    override fun onGetStorage(storageInfo: StorageInfo) {
        view { showStorageInfo(storageInfo) }
    }

    override fun onGetStorageError(error: ViewError) {
        view { showErrorDialog(error) }
    }

    override fun onGetLatestUploads(messages: List<Message>) {
        view { showLatestUploads(messages) }
    }

    override fun onGetLatestUploadsError(error: ViewError) {
        view { showErrorDialog(error) }
    }

    /**
     * UploadFileInteractor callbacks
     */
    override fun onUploadFileComplete() {
        Timber.e("onUploadFileComplete")
        view { hideUploadProgress() }
        refresh()
    }

    override fun onUploadFileProgress(pct: Double) {
        Timber.e("onUploadFileProgress $pct")
        view { updateUploadProgress(pct) }
    }

    override fun onUploadFileError(error: ViewError) {
        view {
            hideUploadProgress()
            showErrorDialog(error)
        }
    }
}