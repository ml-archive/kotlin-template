package dk.nodes.template.network.rest.util

import java.io.IOException
import java.lang.reflect.Type

import okhttp3.ResponseBody
import okio.BufferedSource
import retrofit2.Converter
import retrofit2.Retrofit

/*
    This is necessary to get general purpose disk caching from Store library to work
    with retrofit. (Store expects okio BufferedSource)
 */
class BufferedSourceConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?,
                                       retrofit: Retrofit?): Converter<ResponseBody, *>? {
        return if (BufferedSource::class.java != type) {
            null
        } else Converter<ResponseBody, BufferedSource> { value -> value.source() }
    }
}