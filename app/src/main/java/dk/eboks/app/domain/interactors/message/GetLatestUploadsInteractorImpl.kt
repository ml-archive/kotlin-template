package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

class GetLatestUploadsInteractorImpl(executor: Executor, val messagesRepository: MessagesRepository) : BaseInteractor(executor), GetLatestUploadsInteractor {
    override var output: GetLatestUploadsInteractor.Output? = null
    override var input: GetLatestUploadsInteractor.Input? = null

    override fun execute() {
        try {
            val result = messagesRepository.getLatestUploads(input?.offset ?: 0, input?.limit ?: 5)
            runOnUIThread {
                output?.onGetLatestUploads(result)
            }
        }
        catch (t : Throwable)
        {
            Timber.e(t)
            runOnUIThread {
                output?.onGetLatestUploadsError(exceptionToViewError(t, shouldClose = true))
            }
        }
    }

}