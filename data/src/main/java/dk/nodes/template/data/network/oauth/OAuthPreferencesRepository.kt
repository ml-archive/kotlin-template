package dk.nodes.template.data.network.oauth

import dk.nodes.okhttputils.oauth.OAuthRepository
import dk.nodes.template.domain.managers.PrefManager
import javax.inject.Inject

class OAuthPreferencesRepository @Inject constructor(
        private val prefManager: PrefManager
) : OAuthRepository {

    override fun getAccessToken(): String? {
        return prefManager.getString(PREF_ACCESS_TOKEN, null)
    }

    override fun getRefreshToken(): String? {
        return prefManager.getString(PREF_REFRESH_TOKEN, null)
    }

    override fun setAccessToken(accessToken: String?) {
        accessToken?.let { token ->
            prefManager.setString(PREF_ACCESS_TOKEN, token)
        }
    }

    override fun setRefreshToken(refreshToken: String?) {
        refreshToken?.let { token ->
            prefManager.setString(PREF_REFRESH_TOKEN, token)
        }
    }

    override fun clear() {
        prefManager.remove(PREF_ACCESS_TOKEN)
        prefManager.remove(PREF_REFRESH_TOKEN)
    }

    companion object {
        private const val PREF_REFRESH_TOKEN = "pref_oauth_refresh_token"
        private const val PREF_ACCESS_TOKEN = "pref_oauth_access_token"
    }
}