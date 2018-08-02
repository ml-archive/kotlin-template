package dk.eboks.app.injection.modules

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.managers.*
import dk.eboks.app.network.Api
import dk.eboks.app.network.managers.AuthClientImpl
import dk.eboks.app.network.managers.DownloadManagerImpl
import dk.eboks.app.network.managers.protocol.*
import dk.eboks.app.network.util.BufferedSourceConverterFactory
import dk.eboks.app.network.util.DateDeserializer
import dk.eboks.app.network.util.ItemTypeAdapterFactory
import dk.nodes.arch.domain.injection.scopes.AppScope
import okhttp3.CertificatePinner
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
    @AppScope
    fun provideBaseUrlString(): String {
        return Config.getApiUrl()
    }

    @Provides
    @AppScope
    fun provideGsonConverter(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
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
    fun provideEboksHeaderInterceptor(appStateManager: AppStateManager): EboksHeaderInterceptor {
        return EboksHeaderInterceptor(appStateManager)
    }

    @Provides
    @AppScope
    fun provideHttpClient(eboksHeaderInterceptor: EboksHeaderInterceptor, eAuth2: EAuth2, prefManager: PrefManager): OkHttpClient {

        val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .authenticator(eAuth2)
                .addInterceptor(AcceptLanguageHeaderInterceptor())
                .addInterceptor(ServerErrorInterceptor()) // parses the server error structure and throws the ServerErrorException
                .addInterceptor(eboksHeaderInterceptor)
                //.addInterceptor(NMetaInterceptor(BuildConfig.FLAVOR))

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(ApiHostSelectionInterceptor())
        }

        if (BuildConfig.DEBUG) {
            val logging = okhttp3.logging.HttpLoggingInterceptor()
            logging.level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(logging)
        }

        if (!BuildConfig.DEBUG) {
            // TODO: reenable cert pinning at some point
            /*
            clientBuilder.certificatePinner(
                    CertificatePinner.Builder()
                            //.add("*.e-boks.dk", "sha256/ABF0819A9C2A025C108014F66A7382E8BC4084612D53530792FF8BF3AA5B8503")
                            //.add("*.eboks.dk", "sha256/bdf29436f609e83fcca59f1119a7e9e6eb69506a")
                            .add("*.e-boks.dk", "sha256/bdf29436f609e83fcca59f1119a7e9e6eb69506a")
                            //.add("*.e-boks.com", "sha256/bdf29436f609e83fcca59f1119a7e9e6eb69506a")
                            .build()
            )
            */
        }

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
    fun provideAuthenticator(prefManager: PrefManager, appStateManager: AppStateManager, userSettingsManager: UserSettingsManager): EAuth2 {
        return EAuth2(prefManager, appStateManager, userSettingsManager)
    }

    @Provides
    @AppScope
    fun provideAuthClient(): AuthClient {
        return AuthClientImpl()
    }


}
