package dk.eboks.app.domain.repositories

import dk.eboks.app.domain.models.Folder

/**
 * Created by bison on 01/02/18.
 */
interface CategoriesRepository {
    fun getCategories(cached : Boolean = false) : List<Folder>
}