package dk.eboks.app.network.rest

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
        else    // not logged in
        {
            header = "deviceid=\"${protocolManager.sessionData.deviceId}\",nonce=\"${protocolManager.sessionData.nonce}\",sessionid=\"${protocolManager.sessionData.sessionId}\",response=\"${protocolManager.sessionData.responseCode}\""
        }

        val newRequest = originalRequest.newBuilder()
                .header("X-EBOKS-AUTHENTICATE", header)
                .build()

        val response = chain.proceed(newRequest)
        val auth_header = response.header("X-EBOKS-AUTHENTICATE") ?: ""
        //Timber.e("response eboks header: $auth_header")
        if(!protocolManager.loggedIn)
        {
            val nonce_sess = protocolManager.parseNonceAndSessionId(auth_header)
            protocolManager.sessionData.nonce = nonce_sess.first
            protocolManager.sessionData.sessionId = nonce_sess.second
            protocolManager.sessionData.responseCode = protocolManager.generateResponseCode()
            protocolManager.sessionData.initialized = true
            Timber.e("Post login SessionData = ${protocolManager.sessionData}")
        }
        else // not logged in
        {
            protocolManager.sessionData.nonce = protocolManager.parseNonce(auth_header)
        }


        return response
    }
}
