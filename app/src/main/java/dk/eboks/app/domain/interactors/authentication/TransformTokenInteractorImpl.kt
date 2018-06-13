package dk.eboks.app.domain.interactors.authentication

import com.google.gson.Gson
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import okhttp3.Request
import timber.log.Timber
import okhttp3.RequestBody
import okhttp3.FormBody
import okhttp3.OkHttpClient


/**
 * Created by Christian on 5/28/2018.
 * @author   Christian
 * @since    5/28/2018.
 */
class TransformTokenInteractorImpl(executor: Executor, val api: Api, val appStateManager: AppStateManager, val userManager: UserManager, val httpClient: OkHttpClient, val gson: Gson) : BaseInteractor(executor), TransformTokenInteractor {
    override var output: TransformTokenInteractor.Output? = null
    override var input: TransformTokenInteractor.Input? = null

    override fun execute() {
        try {
            input?.loginState?.kspToken?.let {

                val formBody = FormBody.Builder()
                        .add("token", it)
                        .add("grant_type", "kspwebtoken")
                        .add("scope", "mobileapi offline_access")
                        .add("client_id", "MobileApp-Long-Custom-id") // custom stuff is prolly temporarily but who knows
                        .add("client_secret", "MobileApp-Long-Custom-secret")
                        .build()

                val request = Request.Builder()
                        .url(Config.getAuthUrl())
                        .post(formBody)
                        .build()

                val result = httpClient.newCall(request).execute()
                if(result.isSuccessful)
                {
                    result.body()?.string()?.let { json ->
                        gson.fromJson(json, AccessToken::class.java)?.let { token ->
                            appStateManager.state?.loginState?.token = token

                            val userResult = api.getUserProfile().execute()
                            userResult?.body()?.let { user->
                                // update the states
                                appStateManager.state?.loginState?.userLoginProviderId?.let { user.lastLoginProviderId = it }
                                Timber.e("Saving user $user")
                                val newUser = userManager.put(user)
                                appStateManager.state?.loginState?.lastUser = newUser
                                appStateManager.state?.currentUser = newUser
                            }
                            appStateManager.save()

                            runOnUIThread {
                                output?.onLoginSuccess(token)
                            }
                        }.guard { // could not deserialize token
                            runOnUIThread {
                                output?.onLoginError(ViewError(title = Translation.error.genericTitle, message = Translation.error.genericMessage, shouldCloseView = true)) // TODO better error
                            }
                        }
                    }
                }

                input?.loginState?.kspToken = null // consume the token - it's only usable once anyway

                /*
                val tokenResult = api.getToken(mapOf(
                        Pair("token", it),
                        Pair("grant_type", "kspwebtoken"),
                        Pair("scope", "mobileapi offline_access"),
                        Pair("client_id", "MobileApp-Long-Custom-id"), // TODO: shoul it really be |Custom-id| ??? YES
                        Pair("client_secret", "MobileApp-Long-Custom-secret")
//                        Pair("client_id", BuildConfig.OAUTH_LONG_ID), // TODO: use the correct id and secret if(and only if) Ukraine fixes theirs...
//                        Pair("client_secret", BuildConfig.OAUTH_LONG_SECRET)
                )).execute()

                if (tokenResult.isSuccessful) {

                    tokenResult?.body()?.let { token ->
                        appStateManager.state?.loginState?.token = token

                        val userResult = api.getUserProfile().execute()
                        userResult?.body()?.let { user->
                            // update the states
                            appStateManager.state?.loginState?.userLoginProviderId?.let { user.lastLoginProviderId = it }
                            Timber.e("Saving user $user")
                            val newUser = userManager.put(user)
                            appStateManager.state?.loginState?.lastUser = newUser
                            appStateManager.state?.currentUser = newUser
                        }
                        appStateManager.save()

                        runOnUIThread {
                            output?.onLoginSuccess(token)
                        }
                    }
                } else {
                    runOnUIThread {
                        output?.onLoginError(ViewError(title = Translation.error.genericTitle, message = Translation.error.genericMessage, shouldCloseView = true)) // TODO better error
                    }
                }
                */
            }
        } catch (t: Throwable) {
            Timber.e("Token transform fail: $t")
            runOnUIThread {
                output?.onLoginError(exceptionToViewError(t))
            }
        }
    }

}