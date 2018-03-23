package dk.eboks.app.network.repositories

import com.google.gson.Gson
import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.domain.models.protocol.ServerError
import dk.eboks.app.domain.repositories.SenderCategoriesRepository
import dk.eboks.app.injection.modules.SenderCategoryStore
import dk.eboks.app.network.Api
import timber.log.Timber
import java.io.IOException
import java.net.UnknownHostException

/**
 * Created by bison on 01/02/18.
 */
class SenderCategoriesRestRepository(val api: Api, val gson: Gson, val senderCategoryStore: SenderCategoryStore) : SenderCategoriesRepository {

    override fun getSenderCategories(cached: Boolean): List<SenderCategory> {
            val result = if(cached) {
                senderCategoryStore.get(0).blockingGet()
            } else {
                senderCategoryStore.fetch(0).blockingGet()
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
            // attempt to parse error
            response.errorBody()?.string()?.let { error_str ->
                Timber.e("Received error body $error_str")
                throw(ServerErrorException(gson.fromJson<ServerError>(error_str, ServerError::class.java)))
            }
        }
        throw RuntimeException()
    }
}