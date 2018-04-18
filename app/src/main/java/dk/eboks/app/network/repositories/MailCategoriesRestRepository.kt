package dk.eboks.app.network.repositories

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.repositories.MailCategoriesRepository
import dk.eboks.app.network.Api
import dk.eboks.app.storage.base.CacheStore

typealias MailCategoryStore = CacheStore<Long, List<Folder>>

/**
 * Created by bison on 01/02/18.
 */
class MailCategoriesRestRepository(val context: Context, val api: Api, val gson: Gson) : MailCategoriesRepository {

    val mailCategoryStore: MailCategoryStore by lazy {
        MailCategoryStore(context, gson, "mail_category_store.json", object : TypeToken<MutableMap<Long, List<Folder>>>() {}.type, { key ->
            val response = api.getMailCategories().execute()
            var result : List<Folder>? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }

    override fun getMailCategories(cached: Boolean): List<Folder> {
        val res = if(cached) mailCategoryStore.get(0) else mailCategoryStore.fetch(0)
        if(res != null)
            return res
        else
            return ArrayList()
    }
}