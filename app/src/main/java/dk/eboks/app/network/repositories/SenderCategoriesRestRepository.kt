package dk.eboks.app.network.repositories

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.domain.repositories.SenderCategoriesRepository
import dk.eboks.app.network.Api
import dk.eboks.app.storage.base.CacheStore
import timber.log.Timber

typealias SenderCategoryStore = CacheStore<String, List<SenderCategory>>

/**
 * Created by bison on 01/02/18.
 */
class SenderCategoriesRestRepository(val context: Context, val api: Api, val gson: Gson) : SenderCategoriesRepository {

    val senderCategoryStore: SenderCategoryStore by lazy {
        SenderCategoryStore(context, gson, "sender_category_store.json", object : TypeToken<MutableMap<String, List<SenderCategory>>>() {}.type, { key ->
            val response = api.getSenderCategories(key).execute()
            var result : List<SenderCategory>? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }

    override fun getSenderCategories(cached: Boolean): List<SenderCategory> {
            val result = if(cached) {
                senderCategoryStore.get("private")
            } else {
                senderCategoryStore.fetch("private")
            }
            if(result == null) {
                throw(RuntimeException("dang"))
            }
            return result
    }

    override fun getSendersByCategory(catId: Long): SenderCategory {
        val call = api.getSenders(catId)
        val result = call.execute()
        result?.let { response ->
            if (response.isSuccessful) {
                return response.body() ?: throw(RuntimeException("Unknown"))
            }
        }
        throw RuntimeException()
    }
}