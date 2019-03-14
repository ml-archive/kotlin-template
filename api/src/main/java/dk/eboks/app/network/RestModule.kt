package dk.eboks.app.network

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.AuthClient
import dk.eboks.app.domain.managers.DownloadManager
import dk.eboks.app.network.managers.AuthClientImpl
import dk.eboks.app.network.managers.DownloadManagerImpl
import dk.eboks.app.network.managers.protocol.AcceptLanguageHeaderInterceptor
import dk.eboks.app.network.managers.protocol.ApiHostSelectionInterceptor
import dk.eboks.app.network.managers.protocol.EAuth2
import dk.eboks.app.network.managers.protocol.EboksHeaderInterceptor
import dk.eboks.app.network.managers.protocol.ServerErrorInterceptor
import dk.eboks.app.network.util.BufferedSourceConverterFactory
import dk.eboks.app.network.util.DateDeserializer
import dk.eboks.app.network.util.ItemTypeAdapterFactory
import dk.nodes.arch.domain.injection.scopes.AppScope
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.Date
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
        return GsonBuilder()
            .registerTypeAdapterFactory(typeFactory)
            .registerTypeAdapter(
                java.lang.Double::class.java,
                JsonDeserializer<Double> { json, _, _ ->
                    val string = json?.asString ?: return@JsonDeserializer null
                    try {
                        string.toDouble()
                    } catch (ne: NumberFormatException) {
                        string.replace(",", ".").toDouble()
                    }
                })
            .registerTypeAdapter(Date::class.java, dateDeserializer)
//                .registerTypeAdapter(List::class.java, ListDeserializer())
            .setDateFormat(DateDeserializer.DATE_FORMATS[0])
            .create()
    }

    @Provides
    @Named("NAME_BASE_URL")
    @AppScope
    fun provideBaseUrlString(appConfig: AppConfig): String {
        return appConfig.getApiUrl()
    }

    @Provides
    @AppScope
    fun provideGsonConverter(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @AppScope
    fun provideDownloadManager(manager: DownloadManagerImpl): DownloadManager {
        return manager
    }

    @Provides
    @AppScope
    fun provideEboksHeaderInterceptor(appStateManager: AppStateManager): EboksHeaderInterceptor {
        return EboksHeaderInterceptor(appStateManager)
    }

    @Provides
    @AppScope
    fun provideHttpClient(
        eboksHeaderInterceptor: EboksHeaderInterceptor,
        eAuth2: EAuth2,
        gson: Gson,
        context: Context,
        apiHostSelectionInterceptor: ApiHostSelectionInterceptor
    ): OkHttpClient {

        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(45, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .authenticator(eAuth2)
            .addInterceptor(AcceptLanguageHeaderInterceptor())
            .addInterceptor(ServerErrorInterceptor(gson)) // parses the server error structure and throws the ServerErrorException
            .addInterceptor(eboksHeaderInterceptor)
        // .addInterceptor(NMetaInterceptor(BuildConfig.FLAVOR))

        if (BuildConfig.BUILD_TYPE.contains("debug", ignoreCase = true)) {
            clientBuilder.addInterceptor(apiHostSelectionInterceptor)
        }

        if (BuildConfig.DEBUG) {
            val logging = okhttp3.logging.HttpLoggingInterceptor()
            logging.level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
            clientBuilder.addNetworkInterceptor(logging)
        }

        if (!BuildConfig.BUILD_TYPE.contains("debug", ignoreCase = true)) {
            clientBuilder.certificatePinner(
                CertificatePinner.Builder()
                    // .add("*.e-boks.dk", "sha256/ABF0819A9C2A025C108014F66A7382E8BC4084612D53530792FF8BF3AA5B8503")
                    // .add("*.eboks.dk", "sha256/bdf29436f609e83fcca59f1119a7e9e6eb69506a")
                    // .add("*.e-boks.com", "sha256/bdf29436f609e83fcca59f1119a7e9e6eb69506a")
                    .add("*.e-boks.com", "sha256/ai4fadHtzoMb+LlJR11Ar8YHh6wzhlSX9USbrDs8yjw=")
                    // .add("*.e-boks.com", "sha256/bdf29436f609e83fcca59f1119a7e9e6eb69506a")
                    .build()
            )
        }

        val cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))

        /*
        val cookieJar = object : CookieJar {
            private val cookieStore = HashMap<String, List<Cookie>>()

            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                Timber.e("Saving Cookies $cookies")
                cookieStore.put(url.host(), cookies)
            }

            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                val cookies = cookieStore.get(url.host())
                Timber.e("Returning cookie for $url cookies: $cookies")
                return if (cookies != null) cookies else ArrayList()
            }
        }
        */

        clientBuilder.cookieJar(cookieJar)

        return clientBuilder.build()
    }

    @Provides
    @AppScope
    fun provideRetrofit(
        client: OkHttpClient,
        converter: Converter.Factory,
        @Named("NAME_BASE_URL") baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(BufferedSourceConverterFactory())
            .addConverterFactory(converter)
            .build()
    }

    @Provides
    @AppScope
    fun provideApi(retrofit: Retrofit): Api {
        return retrofit.create<Api>(Api::class.java)
    }

    @Provides
    @AppScope
    fun provideAuthClient(client: AuthClientImpl): AuthClient {
        return client
    }
}