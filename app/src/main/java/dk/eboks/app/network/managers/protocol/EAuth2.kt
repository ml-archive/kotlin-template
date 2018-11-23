package dk.eboks.app.network.managers.protocol

import dk.eboks.app.App
import dk.eboks.app.domain.managers.*
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

/**
 * E-boks Authenticator, based on OAuth2
 */
class EAuth2(prefManager: PrefManager, val appStateManager: AppStateManager, val userSettingsManager: UserSettingsManager) : Authenticator {
    @Inject
    lateinit var executer: Executor
    @Inject
    lateinit var uiManager: UIManager

    @Inject
    lateinit var authClient: AuthClient


    init {
        App.instance().appComponent.inject(this)
    }

    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? {
        Timber.w("authenticate; attempt number-${responseCount(response)}")
        // If we've failed 3 times, give up. Otherwise this would be an infinite loop, asking for authentication
        // because of failing authentication...
        if (responseCount(response) >= 3) {
            Timber.e("Authenticate failed several times")
            return null
        }

        var token = appStateManager.state?.loginState?.token

        // Have we tried with the newest token? Give it a shot!
        // (happens if multiple calls triggered 401 while we were running authenticate() on another call )
        if (responseCount(response) <= 1) {
            token?.let {
                return response.request().newBuilder()
                        .header("Authorization", it.token_type + " " + it.access_token)
                        .build()
            }
        }

        // Any chance of a quick refresh?
        Timber.w("Trying refresh")
        token = refreshToken()

        token?.guard {
            Timber.w("Trying transform")
            // try to see if we can transform a web-token
            token = transformToken()
        }
        token?.guard {
            Timber.w("Trying newtoken")
            // None or invalid web-token - try for a new AccessToken
            token = newToken()
        }

        token?.let {
            // save the token
            appStateManager.state?.loginState?.token = it
            appStateManager.save()

            Timber.w("Got Authentication token : $it")
            // attach it to this request.
            // the main HttpClient will handle subsequent ones
            return response.request().newBuilder()
                    .header("Authorization", it.token_type + " " + it.access_token)
                    .build()
        }.guard {
            if(!ignoreFurtherLoginRequests) {
                ignoreFurtherLoginRequests = true
                Timber.e("Authenticator giving up and returning to login, session expired")
                uiManager.showLoginScreen()
                /*
            Timber.e("Sleep - login_condition")
            executer.sleepUntilSignalled("login_condition", 0)
            Timber.e("Wake - login_condition")
            return authenticate(route, response)
            */
            }
            else
            {
                Timber.e("Ignoring further login attempts, since we already requested one")
            }
        }

        return null
    }

    private fun refreshToken(): AccessToken? {
        val reToken = appStateManager.state?.loginState?.token?.refresh_token
        if (reToken.isNullOrBlank()) {
            return null // no refresh token
        }
        appStateManager.state?.loginState?.token = null // consume it. IUf we can't refresh it, we need a new one anyway

        try {
            return authClient.transformRefreshToken(reToken!!)
        } catch (e: Throwable) {
            Timber.e("Token refresh fail: $e")
        }
        return null
    }

    private fun newToken(): AccessToken? {
        try {
            val userName = appStateManager.state?.loginState?.userName
            val password = appStateManager.state?.loginState?.userPassWord
            val longToken = userSettingsManager.get(appStateManager.state?.loginState?.selectedUser?.id ?: 0).stayLoggedIn
            if (userName.isNullOrBlank() || password.isNullOrBlank()) {
                return null // todo much, much, much more drastic error here - this is when the authenticator was started without a user being selected
            }
            return authClient.login(userName!!, password!!, longToken)
        } catch (e: Throwable) {
            Timber.e("New token fail: $e")
        }
        return null
    }

    private fun transformToken(): AccessToken? {
        val kspToken = appStateManager.state?.loginState?.kspToken
        if (kspToken.isNullOrBlank()) {
            return null // no web token
        }
        appStateManager.state?.loginState?.kspToken = null // consume it
        try {
            return authClient.transformKspToken(kspToken!!)
        } catch (e: Throwable) {
            Timber.e("Token transform fail: $e")
        }
        return null
    }

    /**
     * count how many times we've tried
     */
    private fun responseCount(response: Response): Int {
        var result = 1
        var tempResponse: Response? = response

        while (tempResponse != null) {
            Timber.i("$result - $tempResponse")
            tempResponse = tempResponse.priorResponse()
            result++
        }

        return result
    }

    companion object {
        var ignoreFurtherLoginRequests = false
    }
}