package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
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
                val hasCached = messagesRepository.hasCachedMessageFolder(folder)

                val messages = getMessages(hasCached, folder)
                runOnUIThread {
                    output?.onGetMessages(messages)
                }

                if(hasCached)
                    Timber.e("Emitting cached messages")
                else {
                    Timber.e("Emitting fresh messages")
                    return
                }

                if(hasCached)
                {
                    val fresh_msgs = getMessages(false, folder)
                    runOnUIThread {
                        Timber.e("Emitting refreshed messages")
                        output?.onGetMessages(fresh_msgs)
                    }
                }
            }.guard {
                runOnUIThread {
                    output?.onGetMessagesError(ViewError())
                }
            }

        } catch (t: Throwable) {
            runOnUIThread {
                t.printStackTrace()
                output?.onGetMessagesError(exceptionToViewError(t, shouldClose = true))
            }
        }
    }

    private fun getMessages(cached : Boolean, folder: Folder) : List<Message>
    {
        return if(folder.type == FolderType.FOLDER) messagesRepository.getMessages(cached, folder.id)
        else messagesRepository.getMessages(cached, folder.type)
    }
}