package dk.eboks.app.domain.models.sender

import dk.eboks.app.domain.models.shared.Status
import java.io.Serializable

/**
 * Created by Christian on 3/28/2018.
 * @author   Christian
 * @since    3/28/2018.
 */
data class Registrations(
        val senders : List<Sender>,
        val private : Status,
        val public : Status
) : Serializable