package dk.eboks.app.network.rest

import dk.eboks.app.domain.models.Folder
import dk.eboks.app.domain.repositories.CategoriesRepository
import dk.eboks.app.domain.repositories.RepositoryException
import dk.eboks.app.injection.modules.CategoryStore
import java.io.IOException
import java.net.UnknownHostException

/**
 * Created by bison on 01/02/18.
 */
class CategoriesRestRepository(val categoryStore: CategoryStore) : CategoriesRepository {

    override fun getCategories(cached: Boolean): List<Folder> {
        try {
            val result = if(cached) categoryStore.get(0).blockingGet() else categoryStore.fetch(0).blockingGet()
            if(result == null) {
                throw(RepositoryException(-1, "darn"))
            }
            return result
        }
        catch (e : Throwable)
        {
            e.printStackTrace()
            if(e.cause != null) {
                when(e.cause) {
                    is UnknownHostException -> throw(RepositoryException(-1, "UnknownHostException"))
                    is IOException -> throw(RepositoryException(-1, "IOException"))
                    else -> throw(RepositoryException(-1, "UnknownException"))
                }
            }
            else
                throw(RepositoryException(-1, "Unknown"))
        }
    }
}