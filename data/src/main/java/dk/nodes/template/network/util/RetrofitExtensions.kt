package dk.nodes.template.network.util

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

fun <T> Call<T>.toDeferred(): Deferred<T> {
    val deferred = CompletableDeferred<T>()
    deferred.invokeOnCompletion {
        if (deferred.isCancelled) {
            cancel()
        }
    }

    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            deferred.completeExceptionally(t)
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                deferred.complete(response.body()!!)
            } else {
                deferred.completeExceptionally(HttpException(response))
            }
        }
    })
    return deferred
}

fun <T> Call<T>.toDeferredResponse(): Deferred<Response<T>> {
    val deferred = CompletableDeferred<Response<T>>()

    deferred.invokeOnCompletion {
        if (deferred.isCancelled) {
            cancel()
        }
    }

    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            deferred.completeExceptionally(t)
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            deferred.complete(response)
        }
    })

    return deferred
}