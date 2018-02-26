package dk.eboks.app.domain.interactors

import dk.eboks.app.domain.repositories.CategoriesRepository
import dk.eboks.app.domain.exceptions.RepositoryException
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by bison on 01/02/18.
 */
class GetCategoriesInteractorImpl(executor: Executor, val foldersRepository: CategoriesRepository) : BaseInteractor(executor), GetCategoriesInteractor {
    override var output: GetCategoriesInteractor.Output? = null
    override var input: GetCategoriesInteractor.Input? = null

    override fun execute() {
        try {
            val senders = foldersRepository.getCategories(input?.cached ?: true)
            runOnUIThread {
                output?.onGetCategories(senders)
            }
        } catch (e: RepositoryException) {
            runOnUIThread {
                output?.onGetCategoriesError(e.message ?: "Unknown error")
            }
        }
    }
}