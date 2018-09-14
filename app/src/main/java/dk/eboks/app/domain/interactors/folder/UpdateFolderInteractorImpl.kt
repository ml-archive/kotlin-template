package dk.eboks.app.domain.interactors.folder

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.FoldersRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor


class UpdateMessageInteractorImpl(executor: Executor, val foldersRepository: FoldersRepository) : BaseInteractor(executor),
        UpdateFolderInteractor {
    override var input: UpdateFolderInteractor.Input? = null
    override var output: UpdateFolderInteractor.Output? = null


    override fun execute() {
        try {
            if (input?.folderPatch != null) {
                foldersRepository.updateFolder(input!!.folderId,input!!.folderPatch )
                runOnUIThread { output?.onUpdateFolderSuccess() }
            } else {
                runOnUIThread { output?.onUpdateFolderError(ViewError()) }
            }
        }
        catch (t: Throwable) {
            runOnUIThread {
                output?.onUpdateFolderError(exceptionToViewError(t))
            }
        }
    }
}