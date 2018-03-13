package dk.eboks.app.domain.interactors.sender

import dk.eboks.app.domain.exceptions.RepositoryException
import dk.eboks.app.domain.models.SenderCategory
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by bison on 01/02/18.
 */
class GetSenderCategoriesInteractorImpl(executor: Executor) : BaseInteractor(executor), GetSenderCategoriesInteractor {
    override var output: GetSenderCategoriesInteractor.Output? = null
    override var input: GetSenderCategoriesInteractor.Input? = null

    override fun execute() {
        try {
            // mock TODO: Change to REST
            val cats = ArrayList<SenderCategory>()
            for(i in 0..30) {
                cats.add(SenderCategory(i.toLong(), "Cat-$i", (Math.random()*100).toInt()))
            }

            runOnUIThread {
                output?.onGetCategories(cats)
            }
        } catch (e: RepositoryException) {
            runOnUIThread {
                output?.onGetCategoriesError(e.message ?: "Unknown error")
            }
        }
    }
}