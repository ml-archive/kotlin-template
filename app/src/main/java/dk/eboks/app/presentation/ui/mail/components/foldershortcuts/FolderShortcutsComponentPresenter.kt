package dk.eboks.app.presentation.ui.mail.components.foldershortcuts

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.GetCategoriesInteractor
import dk.eboks.app.domain.interactors.folder.OpenFolderInteractor
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class FolderShortcutsComponentPresenter @Inject constructor(
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
        runAction { v -> v.showProgress(true) }
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
        runAction { v ->
            EventBus.getDefault().post(RefreshFolderShortcutsDoneEvent())
            v.showProgress(false)
            v.showFolders(folders)
        }
    }

    override fun onGetCategoriesError(error: ViewError) {
        runAction { v ->
            v.showProgress(false)
            EventBus.getDefault().post(RefreshFolderShortcutsDoneEvent())
            v.showErrorDialog(error)
        }
    }

    override fun onOpenFolderDone() {
    }

    override fun onOpenFolderError(error: ViewError) {
        runAction { it.showErrorDialog(error) }
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