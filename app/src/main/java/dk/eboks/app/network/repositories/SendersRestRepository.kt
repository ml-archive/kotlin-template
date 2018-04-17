package dk.eboks.app.network.repositories

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.repositories.SendersRepository
import dk.eboks.app.network.Api
import dk.eboks.app.storage.base.CacheStore
import timber.log.Timber

typealias SenderStore = CacheStore<Int, List<Sender>>

/**
 * Created by bison on 01/02/18.
 */
class SendersRestRepository(val context: Context, val api: Api, val gson: Gson) : SendersRepository {

    val senderStore: SenderStore by lazy {
        SenderStore(context, gson, "sender_store.json", object : TypeToken<MutableMap<Int, List<Sender>>>() {}.type, { key ->
            val response = api.getSenders().execute()
            var result : List<Sender>? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }

    override fun getSenders(cached: Boolean): List<Sender> {
        val result = if (cached) senderStore.get(0) else senderStore.fetch(0)
        if (result == null) {
            throw(RuntimeException("darn"))
        }
        return result
    }

    override fun searchSenders(search: String): List<Sender> {

        val call = api.searchSenders(search)
        val result = call.execute()
        result?.let { response ->
            if (response.isSuccessful) {
                return response.body() ?: throw(RuntimeException("Unknown"))
            }
            // attempt to parse error
            response.errorBody()?.string()?.let { error_str ->
                Timber.e("Received error body $error_str")
                throw(ServerErrorException(gson.fromJson<ServerError>(error_str, ServerError::class.java)))
            }
        }
        throw(RuntimeException())
    }

    override fun getSenderDetail(id: Long): Sender {
        val call = api.getSenderDetail(id)
        val result = call.execute()
        result?.let { response ->
            if (response.isSuccessful) {
                return response.body() ?: throw(RuntimeException("Unknown"))
            }
            // attempt to parse error
            response.errorBody()?.string()?.let { error_str ->
                Timber.e("Received error body $error_str")
                throw(ServerErrorException(gson.fromJson<ServerError>(error_str, ServerError::class.java)))
            }
        }
        throw(RuntimeException())
    }
}