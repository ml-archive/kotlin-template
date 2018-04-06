package dk.eboks.app.domain.models.home

import java.io.Serializable

/**
 * Created by thsk on 16/02/2018.
 */
data class Control(
        var id : String?,
        var type : ItemType,
        var items : List<Item>
) : Serializable