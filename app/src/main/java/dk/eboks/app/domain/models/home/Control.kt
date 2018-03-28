package dk.eboks.app.domain.models.home

import dk.eboks.app.domain.models.Image
import dk.eboks.app.domain.models.shared.Status
import dk.eboks.app.domain.models.shared.Description
import java.io.Serializable

/**
 * Created by thsk on 16/02/2018.
 */
data class Control(
        var id : String?,
        var type : ItemType,
        var items : ArrayList<Item>
) : Serializable