package dk.eboks.app.network.managers.protocol

import dk.nodes.nstack.kotlin.NStack
import javax.inject.Inject

/**
 * Created by bison
 */
class AcceptLanguageHeaderInterceptor @Inject constructor() : okhttp3.Interceptor {
    @Throws(java.io.IOException::class)
    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response
    {
        val newRequest = chain.request().newBuilder()
                .header("Accept-Language", NStack.language.toLanguageTag())
                //.header("Accept", "*/*")
                .build()
        return chain.proceed(newRequest)
    }
}
