package dk.eboks.app.presentation.ui.components.mail.foldershortcuts

import android.arch.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.GetCategoriesInteractor
import dk.eboks.app.domain.interactors.folder.OpenFolderInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Folder
import dk.nodes.arch.presentation.base.BasePresenterImpl
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import javax.inject.Inject
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe



/**
 * Created by bison on 20-05-2017.
 */
class FolderShortcutsComponentPresenter @Inject constructor(val appState: AppStateManager, val getCategoriesInteractor: GetCategoriesInteractor, val openFolderInteractor: OpenFolderInteractor) :
        FolderShortcutsComponentContract.Presenter,
        BasePresenterImpl<FolderShortcutsComponentContract.View>(),
        GetCategoriesInteractor.Output,
        OpenFolderInteractor.Output {

    init {
        openFolderInteractor.output = this
        refresh(true)
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
        Timber.e("Received them folders")
        runAction { v ->
            EventBus.getDefault().post(RefreshFolderShortcutsDoneEvent())
            v.showFolders(folders)
        }
    }

    override fun onGetCategoriesError(msg: String) {
        runAction { v->
            EventBus.getDefault().post(RefreshFolderShortcutsDoneEvent())
        }
        Timber.e(msg)
    }

    override fun onOpenFolderDone() {

    }

    override fun onOpenFolderError(msg: String) {
        Timber.e(msg)
    }

    fun refresh(cached : Boolean) {
        getCategoriesInteractor.output = this
        getCategoriesInteractor.input = GetCategoriesInteractor.Input(cached)
        getCategoriesInteractor.run()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: RefreshFolderShortcutsEvent) {
        refresh(false)
    }

}