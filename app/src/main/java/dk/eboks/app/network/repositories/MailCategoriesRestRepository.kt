package dk.eboks.app.network.repositories

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.repositories.MailCategoriesRepository
import dk.eboks.app.injection.modules.MailCategoryStore

/**
 * Created by bison on 01/02/18.
 */
class MailCategoriesRestRepository(val mailCategoryStore: MailCategoryStore) : MailCategoriesRepository {

    override fun getMailCategories(cached: Boolean): List<Folder> {
        val res = if(cached) mailCategoryStore.get(0) else mailCategoryStore.fetch(0)
        if(res != null)
            return res
        else
            return ArrayList()
    }
}