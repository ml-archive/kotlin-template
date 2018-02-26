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
import dk.eboks.app.domain.managers.ProtocolManager
import dk.eboks.app.network.Api
import dk.eboks.app.network.managers.DownloadManagerImpl
import dk.eboks.app.network.managers.protocol.EboksHeaderInterceptor
import dk.eboks.app.network.managers.protocol.MockHeaderInterceptor
import dk.eboks.app.network.managers.protocol.ProtocolManagerImpl
import dk.eboks.app.network.util.BufferedSourceConverterFactory
import dk.eboks.app.network.util.DateDeserializer
import dk.eboks.app.network.util.ItemTypeAdapterFactory
import dk.nodes.arch.domain.injection.scopes.AppScope
import dk.nodes.nstack.providers.NMetaInterceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named


/**
 * Created by bison on 25/07/17.
 */
@Module
class RestModule {
    @Provides
    fun provideTypeFactory() : ItemTypeAdapterFactory
    {
        return ItemTypeAdapterFactory()
    }

    @Provides
    fun provideDateDeserializer() : DateDeserializer
    {
        return DateDeserializer()
    }

    @Provides
    @AppScope
    fun provideGson(typeFactory: ItemTypeAdapterFactory, dateDeserializer: DateDeserializer) : Gson
    {
        val gson = GsonBuilder()
                .registerTypeAdapterFactory(typeFactory)
                .registerTypeAdapter(Date::class.java, dateDeserializer)
                .setDateFormat(DateDeserializer.DATE_FORMATS[0])
                .create()
        return gson
    }

    @Provides
    @Named("NAME_BASE_URL")
    fun provideBaseUrlString(): String {
        if(BuildConfig.MOCK_API_ENABLED)
            return BuildConfig.MOCK_API_URL
        return Config.currentMode.environment?.baseUrl + "/" + Config.currentMode.urlPrefix + "/" ?: throw(IllegalStateException("No base URL set"))
    }

    @Provides
    @AppScope
    fun provideGsonConverter(gson : Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @AppScope
    fun provideProtocolManager(): ProtocolManager {
        return ProtocolManagerImpl()
    }

    @Provides
    @AppScope
    fun provideDownloadManager(context: Context, client: OkHttpClient, cacheManager: FileCacheManager): DownloadManager {
        return DownloadManagerImpl(context, client, cacheManager)
    }

    @Provides
    @AppScope
    fun provideEboksHeaderInterceptor(eboksProtocol: ProtocolManager): EboksHeaderInterceptor {
        return EboksHeaderInterceptor(eboksProtocol)
    }

    @Provides
    @AppScope
    fun provideHttpClient(eboksHeaderInterceptor: EboksHeaderInterceptor) : OkHttpClient
    {
        val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                //.addInterceptor(eboksHeaderInterceptor)
                .addInterceptor(NMetaInterceptor(BuildConfig.FLAVOR))


        // this only work if we use retrofit enqueue() which in turn uses okhttp enqueue
        // pipelining is solved in a baseclass of the repositories instead
        /*
        if(BuildConfig.FORCE_REQUEST_PIPELINING)
            clientBuilder.dispatcher(Dispatcher(Executors.newSingleThreadExecutor()))
        */

        if(BuildConfig.MOCK_API_ENABLED)
        {
            clientBuilder.addInterceptor(MockHeaderInterceptor())
        }

        if(BuildConfig.DEBUG)
        {
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
    fun provideRetrofit(client : OkHttpClient, converter: Converter.Factory, @Named("NAME_BASE_URL") baseUrl: String): Retrofit {
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
}
