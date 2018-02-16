package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UIManager
import dk.eboks.app.domain.repositories.RepositoryException
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by bison on 01/02/18.
 */
class OpenMessageInteractorImpl(executor: Executor, val appStateManager: AppStateManager, val uiManager: UIManager) : BaseInteractor(executor), OpenMessageInteractor {
    override var output: OpenMessageInteractor.Output? = null
    override var input: OpenMessageInteractor.Input? = null

    override fun execute() {
        try {
            input?.msg?.let {
                appStateManager.state?.currentMessage = it
                appStateManager.save()
                uiManager.showMessageScreen()
                runOnUIThread {
                    output?.onOpenMessageDone()
                }
            }
        } catch (e: RepositoryException) {
            runOnUIThread {
                output?.onOpenMessageError("Unknown error opening message ${input?.msg}")
            }
        }
    }
}