package dk.nodes.template.injection.modules

import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapterFactory
import dk.nodes.nstack.kotlin.providers.NMetaInterceptor
import dk.nodes.template.BuildConfig
import dk.nodes.template.network.Api
import dk.nodes.template.network.util.BufferedSourceConverterFactory
import dk.nodes.template.network.util.DateDeserializer
import dk.nodes.template.network.util.ItemTypeAdapterFactory
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import java.util.concurrent.TimeUnit

val restModule = module {

    factory<TypeAdapterFactory> { ItemTypeAdapterFactory() }
    factory { DateDeserializer() }
    single {
        GsonBuilder()
            .registerTypeAdapterFactory(get())
            .registerTypeAdapter(Date::class.java, get<DateDeserializer>())
            .setDateFormat(DateDeserializer.DATE_FORMATS[0])
            .create()

    }
    single(named("NAME_BASE_URL")) { BuildConfig.API_URL }
    single<Converter.Factory> { GsonConverterFactory.create(get()) }
    single {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(45, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(NMetaInterceptor(BuildConfig.BUILD_TYPE))

        if (BuildConfig.DEBUG) {
            val logging = okhttp3.logging.HttpLoggingInterceptor()
            logging.level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(logging)
        }

        clientBuilder.build()
    }
    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(get<String>(qualifier = named("NAME_BASE_URL")))
            .addConverterFactory(BufferedSourceConverterFactory())
            .addConverterFactory(get())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    single { get<Retrofit>().create<Api>(Api::class.java) }

}
