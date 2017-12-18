package dk.nodes.nstack.providers

import dk.eboks.app.domain.managers.ProtocolManager
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison
 */
class EboksHeaderInterceptor @Inject constructor(var protocolManager: ProtocolManager) : okhttp3.Interceptor {
    @Throws(java.io.IOException::class)
    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response {
        val originalRequest = chain.request()

        var header = ""

        if(!protocolManager.loggedIn)
        {
            val challenge = protocolManager.generateChallenge(protocolManager.userInfo.loginDateTime)
            header = "logon deviceid=\"${protocolManager.sessionData.deviceId}\", datetime=\"${protocolManager.userInfo.loginDateTime}\", challenge=\"$challenge\""
        }

        val newRequest = originalRequest.newBuilder()
                .header("X-EBOKS-AUTHENTICATE", header)
                .build()

        return chain.proceed(newRequest)
    }
}
