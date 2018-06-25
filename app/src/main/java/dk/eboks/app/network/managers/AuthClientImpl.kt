package dk.eboks.app.network.managers

import com.google.gson.Gson
import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.managers.AuthClient
import dk.eboks.app.domain.managers.AuthException
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.util.guard
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class AuthClientImpl : AuthClient {
    private var httpClient: OkHttpClient
    private var gson: Gson = Gson()

    init {
        val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor())

        if (BuildConfig.DEBUG) {
            val logging = okhttp3.logging.HttpLoggingInterceptor()
            logging.level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(logging)
        }

        httpClient = clientBuilder.build()
    }

    override fun transformKspToken(kspToken : String) : AccessToken? {
        val keys = getKeys(true, false)

        val formBody = FormBody.Builder()
                .add("token", kspToken)
                .add("grant_type", "kspwebtoken")
                .add("scope", "mobileapi offline_access")
                .add("client_id", keys.first)
                .add("client_secret", keys.second)
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
                    return token
                }
            }
        }
        return null
    }

    override fun transformRefreshToken(refreshToken : String, longClient: Boolean) : AccessToken? {
        val keys = getKeys(false, longClient)

        val formBody = FormBody.Builder()
                .add("refresh_token", refreshToken)
                .add("grant_type", "refresh_token")
                .add("scope", "mobileapi offline_access")
                .add("client_id", keys.first)
                .add("client_secret", keys.second)
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
                    return token
                }
            }
        }
        return null
    }

    // Throws AuthException with http error code on other values than 200 okay
    override fun login(username : String, password : String, activationCode : String?, longClient: Boolean) : AccessToken? {
        val keys = getKeys(false, longClient)

        val formBody = FormBody.Builder()
                .add("grant_type", "password")
                .add("scope", "mobileapi offline_access")
                .add("client_id", keys.first)
                .add("client_secret", keys.second)
                .add("username", username)
                .add("password", password)

        activationCode?.let {
            formBody.add("acr_values", "activationcode:$it nationality:DK")
        }

        val request = Request.Builder()
                .url(Config.getAuthUrl())
                .post(formBody.build())
                .build()

        val result = httpClient.newCall(request).execute()

        if(result.isSuccessful)
        {
            result.body()?.string()?.let { json ->
                gson.fromJson(json, AccessToken::class.java)?.let { token ->
                    return token
                }
            }
        }
        else
        {
            throw(AuthException(result.code()))
        }
        return null
    }

    private fun getKeys(isCustom: Boolean, isLong: Boolean) : Pair<String, String> {
        lateinit var idSecret: Pair<String, String>
        if(isCustom && isLong) {
            idSecret = Pair(Config.currentMode.environment?.longAuthCustomId ?: "", Config.currentMode.environment?.longAuthCustomSecret ?: "")
        } else if (isCustom && !isLong) {
            idSecret = Pair(Config.currentMode.environment?.shortAuthCustomId ?: "", Config.currentMode.environment?.shortAuthCustomSecret ?: "")
        } else if(!isCustom && isLong) {
            idSecret = Pair(Config.currentMode.environment?.longAuthId ?: "", Config.currentMode.environment?.longAuthSecret ?: "")
        } else if (!isCustom && !isLong) {
            idSecret = Pair(Config.currentMode.environment?.shortAuthId ?: "", Config.currentMode.environment?.shortAuthSecret ?: "")
        }
        return idSecret
    }

}
