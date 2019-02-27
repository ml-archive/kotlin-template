package dk.eboks.app.mail.domain.interactors

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.MailCategoriesRepository
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

/**
 * Created by bison on 01/02/18.
 */
internal class GetMailCategoriesInteractorImpl @Inject constructor(
    executor: Executor,
    private val foldersRepositoryMail: MailCategoriesRepository
) : BaseInteractor(executor), GetCategoriesInteractor {
    override var output: GetCategoriesInteractor.Output? = null
    override var input: GetCategoriesInteractor.Input? = null

    override fun execute() {
        try {
            input?.let { args ->
                loadAndEmit(args.cached, args.userId)
                if (args.cached) {
                    loadAndEmit(false, args.userId)
                }
            }.guard { runOnUIThread { output?.onGetCategoriesError(ViewError()) } }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onGetCategoriesError(exceptionToViewError(t))
            }
        }
    }

    private fun loadAndEmit(cached: Boolean, userId: Int?) {
        val senders = foldersRepositoryMail.getMailCategories(cached, userId)
        runOnUIThread {
            output?.onGetCategories(senders)
        }
    }
}