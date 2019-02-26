package dk.eboks.app.system.managers

import android.content.Context
import dk.eboks.app.domain.managers.ResourceManager
import javax.inject.Inject

/**
 * Created by bison on 07/02/18.
 */
class ResourceManagerImpl @Inject constructor(val context: Context) : ResourceManager {
    override fun getStringArray(id: Int): Array<String> {
        return context.resources.getStringArray(id)
    }
}