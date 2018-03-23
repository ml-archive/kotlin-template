package dk.eboks.app.domain.models.local

import java.io.Serializable

data class ViewError (
    var title : String? = null,
    var message : String? = null,
    var shouldDisplay : Boolean = true,
    var shouldCloseView : Boolean = false
) : Serializable
