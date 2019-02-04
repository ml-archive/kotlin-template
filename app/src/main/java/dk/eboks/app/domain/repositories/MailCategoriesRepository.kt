package dk.eboks.app.domain.repositories

import dk.eboks.app.domain.models.folder.Folder

/**
 * Created by bison on 01/02/18.
 */
interface MailCategoriesRepository {
    fun getMailCategories(cached: Boolean = false, userId: Int?): List<Folder>
}