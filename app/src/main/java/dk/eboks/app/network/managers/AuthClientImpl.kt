package dk.eboks.app.network.managers

import android.util.Base64
import com.google.gson.Gson
import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.exceptions.InteractorException
import dk.eboks.app.domain.managers.AuthClient
import dk.eboks.app.domain.managers.AuthException
import dk.eboks.app.domain.models.login.AccessToken
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import timber.log.Timber
import java.io.UnsupportedEncodingException
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

    override fun transformKspToken(kspToken : String, oauthToken : String?, longClient: Boolean) : AccessToken? {
        val keys = getKeys(true, longClient)

        val formBody = FormBody.Builder()
                .add("kspwebtoken", kspToken)
                .add("grant_type", "kspwebtoken")
                .add("scope", "mobileapi offline_access")
                .add("client_id", keys.first)
                .add("client_secret", keys.second)


        if(oauthToken != null)
            formBody.add("oauthtoken", oauthToken)

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
        return null
    }

    override fun impersonate(token : String, userId : String) {
        val keys = getKeys(true, false)

        val formBody = FormBody.Builder()
                .add("token", token)
                .add("userid", userId)
                .add("grant_type", "impersonate")
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
            return
        }
        throw(InteractorException("impersonate failed"))
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
    override fun login(username : String, password : String, activationCode : String?, longClient: Boolean, bearerToken : String?, verifyOnly : Boolean) : AccessToken? {
        val keys = getKeys(false, longClient)

        val formBody = FormBody.Builder()
                .add("grant_type", "password")
                .add("client_id", keys.first)
                .add("client_secret", keys.second)
                .add("username", username)
                .add("password", password)

        if(verifyOnly)
            formBody.add("scope", "mobileapi")
        else
            formBody.add("scope", "mobileapi offline_access")

        activationCode?.let {
            formBody.add("acr_values", "activationcode:$it nationality:DK")
        }

        val requestBuilder = Request.Builder()
                .url(Config.getAuthUrl())
                .post(formBody.build())

        bearerToken?.let { token->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        val request = requestBuilder.build()

        val result = httpClient.newCall(request).execute()

        if(result.isSuccessful)
        {
            // do not read the token if we're only verifying the login
            if(!verifyOnly) {
                result.body()?.string()?.let { json ->
                    gson.fromJson(json, AccessToken::class.java)?.let { token ->
                        return token
                    }
                }
            }
        }
        else
        {
            result.body()?.string()?.let { json ->
                var jsonObj : JSONObject? = null
                try {
                    jsonObj = JSONObject(json)
                }
                catch (t : Throwable)
                {
                    Timber.e(t)
                    throw(AuthException(result.code(), ""))
                }

                //Timber.e("Parsed json obj ${jsonObj?.toString(4)} errorDescription = ${jsonObj?.getString("error_description")}")

                throw(AuthException(result.code(), jsonObj?.getString("error_description") ?: ""))
            }

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

    @Throws(Exception::class)
    override fun decodeJWTBody(JWTEncoded: String) : JSONObject {
        val split = JWTEncoded.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        //val jwtHeaderJson = JSONObject(getJson(split[0]))
        val jwtBodyJson = JSONObject(getJson(split[1]))
        //Timber.e("Header: $jwtHeaderJson")
        Timber.e("Body: $jwtBodyJson")
        return jwtBodyJson
    }

    @Throws(UnsupportedEncodingException::class)
    private fun getJson(strEncoded: String): String {
        val decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE)
        return String(decodedBytes)
    }

}
