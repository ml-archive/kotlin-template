package dk.eboks.app.domain.interactors.folder

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UIManager
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by bison on 01/02/18.
 */
class OpenFolderInteractorImpl(executor: Executor, val appStateManager: AppStateManager, val uiManager: UIManager) : BaseInteractor(executor), OpenFolderInteractor {
    override var output: OpenFolderInteractor.Output? = null
    override var input: OpenFolderInteractor.Input? = null

    override fun execute() {
        try {
            input?.folder?.let {
                appStateManager.state?.currentFolder = it
                appStateManager.save()
                uiManager.showFolderContentScreen()
                runOnUIThread {
                    output?.onOpenFolderDone()
                }
            }
        } catch (e: Throwable) {
            runOnUIThread {
                output?.onOpenFolderError("Could not open folder")
            }
        }
    }
}