package dk.eboks.app.network.managers.protocol

import dk.eboks.app.domain.config.Config
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by bison
 */
class ApiHostSelectionInterceptor : Interceptor {
    @Throws(java.io.IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val newUrl = request.url().newBuilder()
            .scheme(Config.getApiScheme())
            .host(Config.getApiHost())
            .build()
        request = request.newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(request)
    }
}
