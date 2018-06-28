package dk.eboks.app.domain.interactors.channel

import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by bison on 01/02/18.
 */
class GetChannelContentLinkInteractorImpl(executor: Executor, val httpClient: OkHttpClient, val appStateManager: AppStateManager) : BaseInteractor(executor), GetChannelContentLinkInteractor {
    override var output: GetChannelContentLinkInteractor.Output? = null
    override var input: GetChannelContentLinkInteractor.Input? = null

    override fun execute() {
        try {
            input?.let { args ->
                val accessToken = appStateManager.state?.loginState?.token
                accessToken?.let { token->
                    val request = Request.Builder()
                            .url(Config.getApiUrl() + "channels/${args.channelId}/content/open?access_token=${token.access_token}")
                            .get()
                            .build()

                    val result = httpClient.newCall(request).execute()
                    if(result.isSuccessful)
                    {
                        result.body()?.string()?.let { content->
                            runOnUIThread {
                                output?.onGetChannelContentLink(content)
                            }
                        }
                        return
                    }
                    else
                    {
                        runOnUIThread {
                            output?.onGetChannelContentLinkError(ViewError())
                        }
                    }
                }.guard { throw(RuntimeException("No access token found")) }
            }.guard {
                throw(RuntimeException("bad args"))
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            runOnUIThread {
                val ve = exceptionToViewError(t)
                output?.onGetChannelContentLinkError(ve)
            }
        }
    }
}