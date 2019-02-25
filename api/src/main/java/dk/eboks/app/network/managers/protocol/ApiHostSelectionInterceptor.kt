package dk.eboks.app.network.managers.protocol

import dk.eboks.app.domain.config.AppConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Created by bison
 */
class ApiHostSelectionInterceptor @Inject constructor(private val appConfig: AppConfig) :
    Interceptor {
    @Throws(java.io.IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val newUrl = request.url().newBuilder()
            .scheme(appConfig.getApiScheme())
            .host(appConfig.getApiHost())
            .build()
        request = request.newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(request)
    }
}
