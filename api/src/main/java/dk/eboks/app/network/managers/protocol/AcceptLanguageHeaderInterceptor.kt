package dk.eboks.app.network.managers.protocol

import dk.nodes.nstack.kotlin.NStack
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Created by bison
 */
class AcceptLanguageHeaderInterceptor @Inject constructor() : Interceptor {
    @Throws(java.io.IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .header("Accept-Language", NStack.language.toLanguageTag())
            // .header("Accept", "*/*")
            .build()
        return chain.proceed(newRequest)
    }
}
