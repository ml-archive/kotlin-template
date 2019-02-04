package dk.eboks.app.network.repositories

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dk.eboks.app.domain.managers.CacheManager
import dk.eboks.app.domain.models.sender.Segment
import dk.eboks.app.network.Api
import dk.eboks.app.storage.base.CacheStore

import javax.inject.Inject

typealias SenderSegmentsStore = CacheStore<String, List<Segment>>

class SenderSegmentsRepository @Inject constructor(
    private val context: Context,
    private val api: Api,
    private val gson: Gson,
    private val cacheManager: CacheManager
) {
    private val senderSegmentsStore: SenderSegmentsStore by lazy {
        SenderSegmentsStore(
            cacheManager,
            context,
            gson,
            "sender_segments_store.json",
            object : TypeToken<MutableMap<String, List<Segment>>>() {}.type
        ) {
            val response = api.getSegments().execute()
            if (response.isSuccessful)
                response.body()
            null
        }
    }

    fun getSenderSegments(cached: Boolean = false): List<Segment> {
        return if (cached) {
            senderSegmentsStore.get("")
        } else {
            senderSegmentsStore.fetch("")
        } ?: throw(RuntimeException("dang"))
    }
}