package dk.eboks.app.domain.interactors

import dk.eboks.app.domain.repositories.FoldersRepository
import dk.eboks.app.domain.repositories.RepositoryException
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by bison on 01/02/18.
 */
class GetFoldersInteractorImpl(executor: Executor, val foldersRepository: FoldersRepository) : BaseInteractor(executor), GetFoldersInteractor {
    override var output: GetFoldersInteractor.Output? = null
    override var input: GetFoldersInteractor.Input? = null

    override fun execute() {
        try {
            val senders = foldersRepository.getFolders(input?.cached ?: true)
            runOnUIThread {
                output?.onGetFolders(senders)
            }
        } catch (e: RepositoryException) {
            runOnUIThread {
                output?.onGetFoldersError(e.message ?: "Unknown error")
            }
        }
    }
}