package dk.eboks.app.network.repositories

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dk.eboks.app.domain.managers.CacheManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.repositories.MailCategoriesRepository
import dk.eboks.app.network.Api
import dk.eboks.app.storage.base.CacheStore
import javax.inject.Inject

typealias MailCategoryStore = CacheStore<Long, List<Folder>>

/**
 * Created by bison on 01/02/18.
 */
class MailCategoriesRestRepository @Inject constructor(
    private val context: Context,
    private val api: Api,
    private val gson: Gson,
    private val cacheManager: CacheManager
) : MailCategoriesRepository {

    private var userId: Int? = null

    private val mailCategoryStore: MailCategoryStore by lazy {
        MailCategoryStore(
            cacheManager,
            context,
            gson,
            "mail_category_store.json",
            object : TypeToken<MutableMap<Long, List<Folder>>>() {}.type
        ) { key ->
            val response = api.getMailCategories(userId).execute()
            var result: List<Folder>? = null
            response.let {
                if (it.isSuccessful)
                    result = it.body()
            }
            result
        }
    }

    override fun getMailCategories(cached: Boolean, userId: Int?): List<Folder> {
        this.userId = userId
        return (if (cached) mailCategoryStore.get(0) else mailCategoryStore.fetch(0)) ?: listOf()
    }
}