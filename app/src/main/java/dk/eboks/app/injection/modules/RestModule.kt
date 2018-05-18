package dk.eboks.app.injection.modules

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.managers.DownloadManager
import dk.eboks.app.domain.managers.FileCacheManager
import dk.eboks.app.domain.managers.PrefManager
import dk.eboks.app.domain.managers.ProtocolManager
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.network.Api
import dk.eboks.app.network.managers.DownloadManagerImpl
import dk.eboks.app.network.managers.protocol.EboksHeaderInterceptor
import dk.eboks.app.network.managers.protocol.MockHeaderInterceptor
import dk.eboks.app.network.managers.protocol.ProtocolManagerImpl
import dk.eboks.app.network.util.BufferedSourceConverterFactory
import dk.eboks.app.network.util.DateDeserializer
import dk.eboks.app.network.util.ItemTypeAdapterFactory
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
                .addInterceptor { chain ->
                    var request = chain.request()
                    var token: AccessToken? = null
                    try {
                        token = Gson().fromJson<AccessToken>(prefManager.getString(eAuth2.keyAccesstoken, ""), AccessToken::class.java)
                        token?.let {
                            request = chain.request().newBuilder()
                                    .header("Authorization", it.token_type + " " + it.access_token)
                                    .build()
                        }
                    } catch (e: Throwable) {
                        Timber.w("Couldn't load AccessToken from prefs")
                    }
                    chain.proceed(request)
                }


        // this only work if we use retrofit enqueue() which in turn uses okhttp enqueue
        // pipelining is solved in a baseclass of the repositories instead
        /*
        if(BuildConfig.FORCE_REQUEST_PIPELINING)
            clientBuilder.dispatcher(Dispatcher(Executors.newSingleThreadExecutor()))
        */

        if (BuildConfig.MOCK_API_ENABLED) {
            //clientBuilder.addInterceptor(MockHeaderInterceptor())
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
    fun provideAuthenticator(prefManager: PrefManager): EAuth2 {
        return EAuth2(prefManager)
    }

    /**
     * E-boks Authenticator, based on OAuth2
     */
    inner class EAuth2(val prefManager: PrefManager) : Authenticator {
        val keyAccesstoken = "eauth.accesstoken"

        private var newTokenApi: Api
        private var refreshTokenApi: Api // TODO maybe we need separate api-clients for other auth-calls

        init {
            val newTokenClient = provideHttpClient(
                    provideEboksHeaderInterceptor(
                            provideProtocolManager()
                    ), this, prefManager)
                    .newBuilder()
                    .addInterceptor { chain ->
                        val originalRequest = chain.request()
                        val newRequest = originalRequest.newBuilder()
                                //.header("Authorization", "Basic c2ltcGxlbG9naW46c2VjcmV0")  // NOTE: The header token is different!
                                .build()
                        chain.proceed(newRequest)
                    }
                    .build()
            val refreshTokenClient = provideHttpClient(
                    provideEboksHeaderInterceptor(
                            provideProtocolManager()
                    ), this, prefManager)
                    .newBuilder()
                    .addInterceptor { chain ->
                        val originalRequest = chain.request()
                        val newRequest = originalRequest.newBuilder()
                                //.header("Authorization", "Basic dG9rZW50cmFuc2Zvcm06c2VjcmV0")  // NOTE: The header token is different!
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
            refreshTokenApi = provideApi(
                    provideRetrofit(
                            refreshTokenClient,
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

            var token = Gson().fromJson<AccessToken>(prefManager.getString(keyAccesstoken, ""), AccessToken::class.java)
            Timber.w("Token doesn't work! ${token.access_token} \nTrying for a new one...")
            token = newToken()
            // todo?
//            if(token is kspWebGrantToken) {
//                token = transformToken()
//            }

            token?.let {
                // save the token
                prefManager.setString(keyAccesstoken, Gson().toJson(it))
                Timber.w("Authenticate kæmpik token : $it")
                // attach it to this request.
                // the main HttpClient will handle subsequent ones
                return response.request().newBuilder()
                        .header("Authorization", it.token_type + " " + it.access_token)
                        .build()
            }
            return null
        }

        private fun newToken(): AccessToken? {
            try {
                // request a new token, using the stored user info
                val tokenResponse = newTokenApi.getToken(mapOf(
                        Pair("grant_type", "password"),
                        Pair("username", "3110276111"), // TODO: username + password from where? Fingerprint? nCrypt?
                        Pair("password", "147258369"),
                        Pair("acr_values", "activationcode:Cr4x3N6Q"), // <--- optional
                        Pair("scope", "mobileapi offline_access"),
                        Pair("client_id", "MobileApp-Long-id"),
                        Pair("client_secret", "Mobile-Long-secret") // TODO: probably hardcoded
                )).execute()

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
            try {
                // request a new token, using the stored user info
                val tokenResponse = newTokenApi.getToken(mapOf(
                        Pair("grant_type", "KspWebGrantValidator"),
                        Pair("kspwebtoken", "sdf9jklsjdlf9suwljfsdpofusdpfoudsf"), // TODO: kspwebtoken from where? nemID?
                        Pair("scope", "mobileapi offline_access"),
                        Pair("client_Id", "tokentransform"),
                        Pair("secret", "2BB80D537B1DA3E38BD30361AA855686BDE0EACD7162FEF6A25FE97BF527A25B")  // TODO: probably hardcoded
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
