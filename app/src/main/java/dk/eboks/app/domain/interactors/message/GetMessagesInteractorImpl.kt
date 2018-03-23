package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.network.util.metaData
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by bison on 01/02/18.
 */
class GetMessagesInteractorImpl(executor: Executor, val messagesRepository: MessagesRepository) : BaseInteractor(executor), GetMessagesInteractor {
    override var output: GetMessagesInteractor.Output? = null
    override var input: GetMessagesInteractor.Input? = null

    override fun execute() {
        try {
            input?.folder?.let { folder->
                val messages = if(folder.type == FolderType.FOLDER) messagesRepository.getMessages(input?.cached ?: true, folder.id)
                    else messagesRepository.getMessages(input?.cached ?: true, folder.type)

                messages.metaData?.let { metadata -> Timber.e("Found metadata on messagelist: $metadata") }

                /* Simulated error
                throw(ServerErrorException(ServerError(
                        id = "",
                        type = ErrorType.ERROR,
                        description = Description(
                                title = "Hej Per",
                                text = "Har du savnet mig?"
                        )
                )))
                */

                runOnUIThread {
                    output?.onGetMessages(messages)
                }
            }.guard {
                        runOnUIThread {
                            output?.onGetMessagesError(ViewError())
                        }
                    }

        } catch (t: Throwable) {
            runOnUIThread {
                output?.onGetMessagesError(exceptionToViewError(t, shouldClose = true))
            }
        }
    }
}