package dk.nodes.data.network.oauth

import dk.nodes.okhttputils.oauth.OAuthCallback
import dk.nodes.okhttputils.oauth.entities.OAuthInfo
import dk.nodes.okhttputils.oauth.entities.OAuthResult
import javax.inject.Inject

class OAuthCallbackImpl @Inject constructor() : OAuthCallback {
    override fun provideAuthInfo(refreshToken: String?): OAuthResult<OAuthInfo> {
        return  OAuthResult.Success(OAuthInfo(
                accessToken = "newAccessToken",
                refreshToken = "new/old refresh token"
        ))
    }
}