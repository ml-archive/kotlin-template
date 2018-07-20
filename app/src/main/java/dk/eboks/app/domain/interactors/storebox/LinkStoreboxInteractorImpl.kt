package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by Christian on 5/15/2018.
 * @author   Christian
 * @since    5/15/2018.
 */
class LinkStoreboxInteractorImpl(executor: Executor, private val api: Api) : BaseInteractor(executor), LinkStoreboxInteractor {
    override var input: LinkStoreboxInteractor.Input? = null
    override var output: LinkStoreboxInteractor.Output? = null

    override fun execute() {
        try {
            input?.let {
                var map = mapOf(
                    Pair("email", it.email)
                )
                if(it.mobile.isNotBlank()) {
                    map = mapOf(
                            Pair("email", it.email),
                            Pair("mobile", it.mobile)
                    )
                }

                val result = api.postLinkStorebox(map).execute()
                if(result.isSuccessful) {
                    result.body()?.let { body ->
                        val str = body.toString()
                        Timber.e("body : $str")
                        runOnUIThread {
                            output?.storeboxAccountFound(true, str)
                        }
                    }
                }
                else
                {
                    runOnUIThread {
                        output?.storeboxAccountFound(false, null)
                    }
                }
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onError(exceptionToViewError(t))
            }
        }
    }

}