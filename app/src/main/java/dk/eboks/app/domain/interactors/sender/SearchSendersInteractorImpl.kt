package dk.eboks.app.domain.interactors.sender

import dk.eboks.app.domain.exceptions.RepositoryException
import dk.eboks.app.domain.models.Sender
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by bison on 01/02/18.
 */
class SearchSendersInteractorImpl(executor: Executor) : BaseInteractor(executor), SearchSendersInteractor {
    override var output: SearchSendersInteractor.Output? = null
    override var input: SearchSendersInteractor.Input? = null

    override fun execute() {
        try {
            // mock! TODO: Change to REST
            val senders = ArrayList<Sender>()
            for(i in 0..20) {
                val s = Sender(i.toLong(), "${input?.searchText}senderName$i", 0, "https://qu6oa42ax6a2pyq2c11ozwvm-wpengine.netdna-ssl.com/wp-content/uploads/2017/10/nodes-logo-2017.png")
                senders.add(s)
            }

            runOnUIThread {
                output?.onSearchResult(senders)
            }
        } catch (e: RepositoryException) {
            output?.onSearchResult(ArrayList<Sender>()) // empty
        }
    }
}