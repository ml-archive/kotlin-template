package dk.eboks.app.domain.senders.interactors.register

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

internal class GetSenderRegistrationLinkInteractorImpl @Inject constructor(executor: Executor, private val api: Api)
    : BaseInteractor(executor), GetSenderRegistrationLinkInteractor {

    override var input: GetSenderRegistrationLinkInteractor.Input? = null
    override var output: GetSenderRegistrationLinkInteractor.Output? = null



    override fun execute() {
        input?.id?.let { senderId ->
            try {
                val response = api.getSenderRegistrationLink(senderId).execute()
                if (response.isSuccessful) {
                    response.body()
                            ?.let { output?.onLinkLoaded(it) }
                            .guard { output?.onLinkLoadingError(ViewError()) }
                } else {
                    output?.onLinkLoadingError(ViewError())
                }
            } catch (exception: Exception) {
                output?.onLinkLoadingError(exceptionToViewError(exception))
            }

        }
    }
}