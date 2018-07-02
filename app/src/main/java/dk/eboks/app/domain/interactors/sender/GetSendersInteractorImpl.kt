package dk.eboks.app.domain.interactors.sender

import dk.eboks.app.domain.repositories.SenderCategoriesRepository
import dk.eboks.app.domain.repositories.SendersRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
* Created by bison on 01/02/18.
* @author   bison
* @since    01/02/18.
*/
class GetSendersInteractorImpl(executor: Executor, val sendersRepository: SendersRepository, val senderCategoriesRepository: SenderCategoriesRepository) : BaseInteractor(executor), GetSendersInteractor {

    override var output: GetSendersInteractor.Output? = null
    override var input: GetSendersInteractor.Input? = null


    override fun execute() {
        when {
            (input?.id ?: -1) > 0 -> doGetByCategory()  // id given: all all of that category
            !input?.name.isNullOrBlank() -> doSearch() // name field: do a search-by-name
            else -> doGet() // no name: just get all
        }
    }

    private fun doGet() {
        Timber.d("doGet")
        try {
            val senders = sendersRepository.getSenders(input?.cached ?: true)
            runOnUIThread {
                output?.onGetSenders(senders)
            }
        } catch (t: Throwable) {
            runOnUIThread {
                Timber.e(t)
                output?.onGetSendersError(exceptionToViewError(t))
            }
        }
    }

    fun doGetByCategory() {
        Timber.d("doGetByCategory")
        try {
            val category = senderCategoriesRepository.getSendersByCategory(input?.id ?: 0)
            runOnUIThread {
                output?.onGetSenders(category.senders ?: ArrayList())
            }
        } catch (t: Throwable) {
            runOnUIThread {
                //t.printStackTrace()
                output?.onGetSendersError(exceptionToViewError(t))
            }
        }
    }

    fun doSearch() {
        Timber.d("doSearch")
        try {
            val senders = sendersRepository.searchSenders(input?.name ?: "")
            runOnUIThread {
                output?.onGetSenders(senders)
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onGetSendersError(exceptionToViewError(t, shouldDisplay = false))
            }
        }
    }
}