package dk.eboks.app.mail.domain.interactors.folder

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UIManager
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by bison on 01/02/18.
 */
class OpenFolderInteractorImpl(
    executor: Executor,
    val appStateManager: AppStateManager,
    val uiManager: UIManager
) : BaseInteractor(executor), OpenFolderInteractor {
    override var output: OpenFolderInteractor.Output? = null
    override var input: OpenFolderInteractor.Input? = null

    override fun execute() {
        try {
            input?.folder?.let {
                uiManager.showFolderContentScreen(it)
                runOnUIThread {
                    output?.onOpenFolderDone()
                }
            }
        } catch (e: Throwable) {
            runOnUIThread {
                Timber.e(e)
                output?.onOpenFolderError(exceptionToViewError(e))
            }
        }
    }
}