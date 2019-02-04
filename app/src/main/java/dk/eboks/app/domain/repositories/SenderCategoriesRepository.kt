package dk.eboks.app.domain.repositories

import dk.eboks.app.domain.models.SenderCategory

/**
 * Created by bison on 01/02/18.
 */
interface SenderCategoriesRepository {
    fun getSenderCategories(cached: Boolean = false): List<SenderCategory>
    fun getSendersByCategory(catId: Long): SenderCategory
}