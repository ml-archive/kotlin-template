package dk.eboks.app.domain.interactors.channel

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by bison on 01/02/18.
 */
class GetChannelContentLinkInteractorImpl(executor: Executor, val api: Api, val appStateManager: AppStateManager) : BaseInteractor(executor), GetChannelContentLinkInteractor {
    override var output: GetChannelContentLinkInteractor.Output? = null
    override var input: GetChannelContentLinkInteractor.Input? = null

    override fun execute() {
        try {
            input?.let {
                val accessToken = appStateManager.state?.loginState?.token
                accessToken?.let { token->
                    val result = api.getChannelContentLink(it.channelId, token.access_token).execute()
                    result.body()?.let { link->
                        runOnUIThread {
                            output?.onGetChannelContentLink(link)
                        }
                    }
                }.guard { throw(RuntimeException("No access token found")) }
            }.guard {
                throw(RuntimeException("bad args"))
            }
        } catch (t: Throwable) {
            runOnUIThread {
                val ve = exceptionToViewError(t)
                output?.onGetChannelContentLinkError(ve)
            }
        }
    }
}