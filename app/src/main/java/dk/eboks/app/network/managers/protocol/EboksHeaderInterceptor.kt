package dk.eboks.app.network.managers.protocol

import dk.eboks.app.domain.managers.AppStateManager
import javax.inject.Inject

/**
 * Created by bison
 */
class EboksHeaderInterceptor @Inject constructor(val appStateManager: AppStateManager) : okhttp3.Interceptor {
    @Throws(java.io.IOException::class)
    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response
    {
        appStateManager.state?.loginState?.token?.let { token->
            val newRequest = chain.request().newBuilder()
                    .header("Authorization", token.token_type + " " + token.access_token)
                    .build()
            return chain.proceed(newRequest)
        }
        return chain.proceed(chain.request())
    }
}
