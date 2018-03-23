package dk.eboks.app.network.repositories

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.repositories.MailCategoriesRepository
import dk.eboks.app.injection.modules.MailCategoryStore
import java.io.IOException
import java.net.UnknownHostException

/**
 * Created by bison on 01/02/18.
 */
class MailCategoriesRestRepository(val mailCategoryStore: MailCategoryStore) : MailCategoriesRepository {

    override fun getMailCategories(cached: Boolean): List<Folder> {
        val result = if(cached) mailCategoryStore.get(0).blockingGet() else mailCategoryStore.fetch(0).blockingGet()
        if(result == null) {
            throw(RuntimeException("darn"))
        }
        return result
    }
}