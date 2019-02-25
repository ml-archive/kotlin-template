package dk.eboks.app.mail.domain.interactors.folder

import dk.eboks.app.domain.repositories.FoldersRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class DeleteFolderInteractorImpl(
    executor: Executor,
    val foldersRepository: FoldersRepository
) :
    BaseInteractor(executor),
        DeleteFolderInteractor {
    override var output: DeleteFolderInteractor.Output? = null
    override var input: DeleteFolderInteractor.Input? = null

    override fun execute() {
        try {
            input?.folderId?.let { folderId ->
                foldersRepository.deleteFolder(folderId)
                runOnUIThread {
                    output?.onDeleteFolderSuccess()
                }
            }
        } catch (e: Throwable) {
            runOnUIThread {
                output?.onDeleteFolderError(exceptionToViewError(e))
            }
        }
    }
}