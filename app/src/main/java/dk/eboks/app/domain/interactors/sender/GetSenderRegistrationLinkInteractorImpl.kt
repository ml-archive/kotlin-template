package dk.eboks.app.domain.interactors.sender

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.network.Api
import dk.eboks.app.util.throwableToViewError
import dk.nodes.arch.domain.interactor.Interactor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetSenderRegistrationLinkInteractorImpl(private val api: Api) : GetSenderRegistrationLinkInteractor {

    override var input: GetSenderRegistrationLinkInteractor.Input? = null
    override var output: GetSenderRegistrationLinkInteractor.Output? = null

    override fun run() {
        input?.id?.let { senderId ->
            api.getSenderRegistrationLink(senderId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe( {
                        output?.onLinkLoaded("link")
                    }, {output?.onLinkLoadingError(throwableToViewError(it))})
        }
    }
}