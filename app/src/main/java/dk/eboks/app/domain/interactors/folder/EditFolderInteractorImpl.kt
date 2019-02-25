package dk.eboks.app.domain.interactors.folder

import dk.eboks.app.domain.models.folder.FolderRequest
import dk.eboks.app.domain.repositories.FoldersRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

class EditFolderInteractorImpl @Inject constructor(
    executor: Executor,
    private val foldersRepository: FoldersRepository
) : BaseInteractor(executor), EditFolderInteractor {
    override var output: EditFolderInteractor.Output? = null
    override var input: EditFolderInteractor.Input? = null

    override fun execute() {
        try {
            input?.folderId?.let { folderId ->
                // todo find out what the userId is used for and when it should be set.
                val folderRequest = FolderRequest(null, input?.parentFolderId, input?.folderName)
                foldersRepository.editFolder(folderId, folderRequest)
                runOnUIThread {
                    output?.onEditFolderSuccess()
                }
            }
        } catch (e: Throwable) {
            runOnUIThread {
                output?.onEditFolderError(exceptionToViewError(e))
            }
        }
    }
}