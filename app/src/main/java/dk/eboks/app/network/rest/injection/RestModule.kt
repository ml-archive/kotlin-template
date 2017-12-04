package dk.eboks.app.network.rest.injection

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.injection.scopes.AppScope
import dk.nodes.nstack.providers.NMetaInterceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


/**
 * Created by bison on 25/07/17.
 */
@Module
class RestModule {
    @Provides
    fun provideTypeFactory() : dk.eboks.app.network.rest.util.ItemTypeAdapterFactory
    {
        return dk.eboks.app.network.rest.util.ItemTypeAdapterFactory()
    }

    @Provides
    fun provideDateDeserializer() : dk.eboks.app.network.rest.util.DateDeserializer
    {
        return dk.eboks.app.network.rest.util.DateDeserializer()
    }

    @Provides
    @AppScope
    fun provideGson(typeFactory: dk.eboks.app.network.rest.util.ItemTypeAdapterFactory, dateDeserializer: dk.eboks.app.network.rest.util.DateDeserializer) : Gson
    {
        val gson = GsonBuilder()
                .registerTypeAdapterFactory(typeFactory)
                .registerTypeAdapter(Date::class.java, dateDeserializer)
                .setDateFormat(dk.eboks.app.network.rest.util.DateDeserializer.DATE_FORMATS[0])
                .create()
        return gson
    }

    @Provides
    @Named("NAME_BASE_URL")
    fun provideBaseUrlString(): String {
        return dk.eboks.app.BuildConfig.API_URL
    }

    @Provides
    @AppScope
    fun provideGsonConverter(gson : Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @AppScope
    fun provideHttpClient() : OkHttpClient
    {
        val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(NMetaInterceptor(dk.eboks.app.BuildConfig.FLAVOR))

        if(dk.eboks.app.BuildConfig.DEBUG)
        {
            val logging = okhttp3.logging.HttpLoggingInterceptor()
            logging.level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(logging)

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
        }

        return clientBuilder.build()
    }

    @Provides
    @AppScope
    fun provideRetrofit(client : OkHttpClient, converter: Converter.Factory, @Named("NAME_BASE_URL") baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .build()
    }

    @Provides
    @AppScope
    fun provideApi(retrofit: Retrofit): dk.eboks.app.network.Api {
        return retrofit.create<dk.eboks.app.network.Api>(dk.eboks.app.network.Api::class.java)
    }
}
