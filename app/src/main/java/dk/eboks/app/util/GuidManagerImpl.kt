package dk.eboks.app.util

import dk.eboks.app.domain.managers.GuidManager
import java.util.*

/**
 * Created by bison on 15/12/17.
 */
class GuidManagerImpl : GuidManager {
    override fun generateGuid(): String {
        return UUID.randomUUID().toString()
    }
}