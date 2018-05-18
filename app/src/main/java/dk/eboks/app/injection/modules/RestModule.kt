package dk.eboks.app.injection.modules

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dk.eboks.app.App
import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.managers.*
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.network.Api
import dk.eboks.app.network.managers.DownloadManagerImpl
import dk.eboks.app.network.managers.protocol.EboksHeaderInterceptor
import dk.eboks.app.network.managers.protocol.MockHeaderInterceptor
import dk.eboks.app.network.managers.protocol.ProtocolManagerImpl
import dk.eboks.app.network.util.BufferedSourceConverterFactory
import dk.eboks.app.network.util.DateDeserializer
import dk.eboks.app.network.util.ItemTypeAdapterFactory
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.injection.scopes.AppScope
import dk.nodes.nstack.kotlin.providers.NMetaInterceptor
import okhttp3.*
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named


/**
 * Created by bison on 25/07/17.
 */
@Module
class RestModule {
    @Provides
    fun provideTypeFactory(): ItemTypeAdapterFactory {
        return ItemTypeAdapterFactory()
    }

    @Provides
    fun provideDateDeserializer(): DateDeserializer {
        return DateDeserializer()
    }

    @Provides
    @AppScope
    fun provideGson(typeFactory: ItemTypeAdapterFactory, dateDeserializer: DateDeserializer): Gson {
        val gson = GsonBuilder()
                .registerTypeAdapterFactory(typeFactory)
                .registerTypeAdapter(Date::class.java, dateDeserializer)
                //.registerTypeAdapter(List::class.java, ListDeserializer())
                .setDateFormat(DateDeserializer.DATE_FORMATS[0])
                .create()
        return gson
    }

    @Provides
    @Named("NAME_BASE_URL")
    fun provideBaseUrlString(): String {
        if (BuildConfig.MOCK_API_ENABLED)
            return BuildConfig.MOCK_API_URL
        return Config.currentMode.environment?.baseUrl + "/" + Config.currentMode.urlPrefix + "/"
                ?: throw(IllegalStateException("No base URL set"))
    }

    @Provides
    @AppScope
    fun provideGsonConverter(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @AppScope
    fun provideProtocolManager(): ProtocolManager {
        return ProtocolManagerImpl()
    }

    @Provides
    @AppScope
    fun provideDownloadManager(
            context: Context,
            client: OkHttpClient,
            cacheManager: FileCacheManager
    ): DownloadManager {
        return DownloadManagerImpl(context, client, cacheManager)
    }

    @Provides
    @AppScope
    fun provideEboksHeaderInterceptor(eboksProtocol: ProtocolManager): EboksHeaderInterceptor {
        return EboksHeaderInterceptor(eboksProtocol)
    }

    @Provides
    @AppScope
    fun provideHttpClient(eboksHeaderInterceptor: EboksHeaderInterceptor, eAuth2: EAuth2, prefManager: PrefManager): OkHttpClient {

        val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .authenticator(eAuth2)
                //.addInterceptor(eboksHeaderInterceptor)
                .addInterceptor(NMetaInterceptor(BuildConfig.FLAVOR))
//                .addInterceptor { chain ->
//                    var request = chain.request()
//                    var token: AccessToken? = null
//                    try {
//                        token = Gson().fromJson<AccessToken>(prefManager.getString(Keys.keyAccesstoken, ""), AccessToken::class.java)
//                        token?.let {
//                            request = chain.request().newBuilder()
//                                    .header("Authorization", it.token_type + " " + it.access_token)
//                                    .build()
//                        }
//                    } catch (e: Throwable) {
//                        Timber.w("Couldn't load AccessToken from prefs")
//                    }
//                    chain.proceed(request)
//                }


        // this only work if we use retrofit enqueue() which in turn uses okhttp enqueue
        // pipelining is solved in a baseclass of the repositories instead
        /*
        if(BuildConfig.FORCE_REQUEST_PIPELINING)
            clientBuilder.dispatcher(Dispatcher(Executors.newSingleThreadExecutor()))
        */

        if (BuildConfig.MOCK_API_ENABLED) {
//            clientBuilder.addInterceptor(MockHeaderInterceptor())
        }

        if (BuildConfig.DEBUG) {
            val logging = okhttp3.logging.HttpLoggingInterceptor()
            logging.level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(logging)

            /*
            try {
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                    }

                    override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                        return arrayOf()
                    }
                })

                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, java.security.SecureRandom())
                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory = sslContext.socketFactory
                clientBuilder.sslSocketFactory(sslSocketFactory)
                clientBuilder.hostnameVerifier(HostnameVerifier { hostname, session -> true })
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
            */
        }

        return clientBuilder.build()
    }

    @Provides
    @AppScope
    fun provideRetrofit(
            client: OkHttpClient,
            converter: Converter.Factory, @Named("NAME_BASE_URL") baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                //.addConverterFactory(SimpleXmlConverterFactory.createNonStrict(Persister(AnnotationStrategy())))
                .addConverterFactory(BufferedSourceConverterFactory())
                .addConverterFactory(converter)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @AppScope
    fun provideApi(retrofit: Retrofit): Api {
        return retrofit.create<Api>(Api::class.java)
    }

    @Provides
    @AppScope
    fun provideAuthenticator(prefManager: PrefManager, appStateManager: AppStateManager): EAuth2 {
        return EAuth2(prefManager, appStateManager)
    }

    companion object Keys {
        const val keyAccesstoken = "eauth.accesstoken"
        const val keyKspToken = "eauth.kspToken"
    }

    /**
     * E-boks Authenticator, based on OAuth2
     */
    inner class EAuth2(val prefManager: PrefManager, val appStateManager: AppStateManager) : Authenticator {

        private var newTokenApi: Api

        @Inject
        lateinit var executer: Executor
        @Inject
        lateinit var uiManager: UIManager

        init {
            App.instance().appComponent.inject(this)

            val newTokenClient = provideHttpClient(
                    provideEboksHeaderInterceptor(
                            provideProtocolManager()
                    ), this, prefManager)
                    .newBuilder()
                    .addInterceptor { chain ->
                        val originalRequest = chain.request()
                        val newRequest = originalRequest.newBuilder()
                                .build()
                        chain.proceed(newRequest)
                    }
                    .build()

            newTokenApi = provideApi(
                    provideRetrofit(
                            newTokenClient,
                            provideGsonConverter(
                                    provideGson(
                                            provideTypeFactory(),
                                            provideDateDeserializer()
                                    )
                            ),
                            provideBaseUrlString()
                    )
            )
        }

        override fun authenticate(route: Route?, response: Response): Request? {
            Timber.w("Authenticate")
            // If we've failed 3 times, give up. Otherwise this would be an infinite loop, asking for authentication
            // because of failing authentication...
            if (responseCount(response) >= 3) {
                Timber.e("Authenticate failed several times")
                return null
            }

            // try to see if we can transform a web-token
            var token = transformToken()

            token?.guard {
                // None or invalid web-token - try for a new AccessToken
                token = newToken()
            }

            token?.let {
                // save the token
                prefManager.setString(keyAccesstoken, Gson().toJson(it))
                Timber.w("Authenticate token : $it")
                // attach it to this request.
                // the main HttpClient will handle subsequent ones
                return response.request().newBuilder()
                        .header("Authorization", it.token_type + " " + it.access_token)
                        .build()
            }.guard {
                // Todo doing login
                uiManager.showLoginScreen()
                Timber.v("Sleep - login_condition")
                executer.sleepUntilSignalled("login_condition", 0)
                Timber.v("Wake - login_condition")
                return authenticate(route, response)
            }


            return null
        }

        private fun newToken(): AccessToken? {
            try {
                val userName = appStateManager.state?.loginState?.userName
                val password = appStateManager.state?.loginState?.userPassWord
                val actiCode = appStateManager.state?.loginState?.activationCode
                if(userName.isNullOrBlank() || password.isNullOrBlank()) {
                    return null // todo much, much, much more drastic error here - this is when the authenticator was started without a user being selected
                }
                // request a new token, using the stored user info
                /*val params = mapOf(
                        Pair("grant_type", "password"),
                        Pair("username", userName!!),
                        Pair("password", password!!),
                        Pair("scope", "mobileapi offline_access"),
                        Pair("client_Id", "simplelogin"),
                        Pair("secret", "2BB80D537B1DA3E38BD30361AA855686BDE0EACD7162FEF6A25FE97BF527A25B") // TODO: probably hardcoded
                )*/
                val params = mapOf(
                        Pair("grant_type", "password"),
                        Pair("username", userName!!),
                        Pair("password", password!!),
                        Pair("scope", "mobileapi offline_access"),
                        Pair("client_id", "MobileApp-Long-id"),
                        Pair("client_secret", "MobileApp-Long-secret")
                )
                if(!actiCode.isNullOrBlank()) {
                    params.plus(Pair("acr_values", "activationcode:$actiCode"))
                }
                val tokenResponse = newTokenApi.getToken(params).execute()

                if (tokenResponse.isSuccessful) {
                    return tokenResponse.body()
                }
            } catch (e: Throwable) {
                Timber.e("Authenticate fail: $e")
            } finally {
                return null
            }
        }

        private fun transformToken(): AccessToken? {
            val kspToken = appStateManager.state?.loginState?.kspToken
            if (kspToken.isNullOrBlank()) {
                return null // no web token
            }
            appStateManager.state?.loginState?.kspToken = null // consume it
            try {
                // request a new token, using the stored user info
                val tokenResponse = newTokenApi.getToken(mapOf(
                        Pair("token", kspToken!!),
                        Pair("grant_type", "kspwebtoken"),
                        Pair("scope", "mobileapi offline_access"),
                        Pair("client_id", "MobileApp-Long-Custom-id"),
                        Pair("client_secret", "MobileApp-Long-Custom-secret")
                )).execute()

                if (tokenResponse.isSuccessful) {
                    return tokenResponse.body()
                }
            } catch (e: Throwable) {
                Timber.e("Token transform fail: $e")
            } finally {
                return null
            }
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
    }
}
