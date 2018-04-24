package dk.eboks.app.network.managers.protocol

import dk.eboks.app.BuildConfig

/**
 * Created by bison on 01/02/18.
 */
class MockHeaderInterceptor : okhttp3.Interceptor  {
    @Throws(java.io.IOException::class)
    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()

        val newRequest = originalRequest.newBuilder()
                .header("X-Api-Key", BuildConfig.MOCK_API_KEY)
                .header("Authorization", "Basic c2ltcGxlbG9naW46c2VjcmV0")  // TODO is this the same everytime?
                .build()

        return chain.proceed(newRequest)
    }
}