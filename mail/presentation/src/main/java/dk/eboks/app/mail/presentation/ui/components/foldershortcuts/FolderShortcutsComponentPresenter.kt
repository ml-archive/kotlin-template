package dk.eboks.app.mail.presentation.ui.components.foldershortcuts

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.mail.domain.interactors.categories.GetCategoriesInteractor
import dk.eboks.app.mail.domain.interactors.folder.OpenFolderInteractor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class FolderShortcutsComponentPresenter @Inject constructor(
    private val getCategoriesInteractor: GetCategoriesInteractor,
    private val openFolderInteractor: OpenFolderInteractor
) :
    FolderShortcutsComponentContract.Presenter,
    BasePresenterImpl<FolderShortcutsComponentContract.View>(),
    GetCategoriesInteractor.Output,
    OpenFolderInteractor.Output {

    init {
        openFolderInteractor.output = this
        refresh(true)
        view { showProgress(true) }
    }

    override fun onViewCreated(view: FolderShortcutsComponentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)
        EventBus.getDefault().register(this)
    }

    override fun onViewDetached() {
        EventBus.getDefault().unregister(this)
        super.onViewDetached()
    }

    override fun openFolder(folder: Folder) {
        openFolderInteractor.input = OpenFolderInteractor.Input(folder)
        openFolderInteractor.run()
    }

    override fun onGetCategories(folders: List<Folder>) {
        view {
            EventBus.getDefault().post(RefreshFolderShortcutsDoneEvent())
            showProgress(false)
            showFolders(folders)
        }
    }

    override fun onGetCategoriesError(error: ViewError) {
        view {
            showProgress(false)
            EventBus.getDefault().post(RefreshFolderShortcutsDoneEvent())
            showErrorDialog(error)
        }
    }

    override fun onOpenFolderDone() {
    }

    override fun onOpenFolderError(error: ViewError) {
        view { showErrorDialog(error) }
    }

    fun refresh(cached: Boolean) {
        getCategoriesInteractor.output = this
        getCategoriesInteractor.input = GetCategoriesInteractor.Input(cached, null)
        getCategoriesInteractor.run()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: RefreshFolderShortcutsEvent) {
        refresh(false)
    }
}