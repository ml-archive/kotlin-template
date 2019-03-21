package dk.eboks.app.mail.domain.interactors.message

import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber
import javax.inject.Inject

internal class GetStorageInteractorImpl @Inject constructor(
    executor: Executor,
    private val messagesRepository: MessagesRepository
) :
    BaseInteractor(executor), GetStorageInteractor {
    override var output: GetStorageInteractor.Output? = null

    override fun execute() {
        try {
            val result = messagesRepository.getStorageInfo()
            runOnUIThread {
                output?.onGetStorage(result)
            }
        } catch (t: Throwable) {
            Timber.e(t)
            runOnUIThread {
                output?.onGetStorageError(exceptionToViewError(t, shouldClose = true))
            }
        }
    }
}