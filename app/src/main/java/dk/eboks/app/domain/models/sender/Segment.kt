package dk.eboks.app.domain.models.sender

import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.domain.models.shared.Status
import java.io.Serializable

/**
 * Created by Christian on 3/19/2018.
 * @author   Christian
 * @since    3/19/2018.
 */
data class Segment(
        val id: Long,
        val name : String,
        val type: String,
        val numberOfCategories: Int = 0,
        val categories : List<SenderCategory>? = ArrayList(),
        val registered: Int? = 0, // (0: No, 1: Yes, 2: Partial)
        val status: Status? = null
) : Serializable