package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

class GetSignLinkInteractorImpl(executor: Executor, private val api: Api) :
        BaseInteractor(executor),
        GetSignLinkInteractor {
    override var output: GetSignLinkInteractor.Output? = null
    override var input: GetSignLinkInteractor.Input? = null

    override fun execute() {
        input?.let { args->
            try {
                val folderId = if(args.msg.folderId != 0) args.msg.folderId else args.msg.folder?.id ?: 0
                val result = api.getSignLink(args.msg.id, folderId, CANCEL_CALLBACK, SUCCESS_CALLBACK, ERROR_CALLBACK).execute()

                result.body()?.let {
                    runOnUIThread {
                        output?.onGetSignLink(it)
                    }
                }
            } catch (t: Throwable) {
                Timber.e(t)
                runOnUIThread {
                    output?.onGetSignLinkError(exceptionToViewError(t))
                }
            }
        }.guard { runOnUIThread { output?.onGetSignLinkError(ViewError()) } }
    }

    companion object {
        val CANCEL_CALLBACK = "eboksdk://cancel"
        val SUCCESS_CALLBACK = "eboksdk://success"
        val ERROR_CALLBACK = "eboksdk://error"
    }
}