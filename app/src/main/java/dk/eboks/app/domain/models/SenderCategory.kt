package dk.eboks.app.domain.models

import java.io.Serializable

/**
 * Created by Christian on 3/12/2018.
 * @author   Christian
 * @since    3/12/2018.
 */
data class SenderCategory(
        var id: Long,
        var name: String = "",
        var numberOfSenders: Int = 0
) : Serializable