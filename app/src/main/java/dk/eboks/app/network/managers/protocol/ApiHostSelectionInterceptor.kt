package dk.eboks.app.network.managers.protocol

import dk.eboks.app.domain.config.Config

/**
 * Created by bison
 */
class ApiHostSelectionInterceptor : okhttp3.Interceptor {
    @Throws(java.io.IOException::class)
    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response {
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
