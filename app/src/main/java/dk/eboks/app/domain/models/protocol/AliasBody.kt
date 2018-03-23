package dk.eboks.app.domain.models.protocol

import dk.eboks.app.domain.models.sender.Alias
import java.io.Serializable

/**
 * Created by Christian on 3/23/2018.
 * @author   Christian
 * @since    3/23/2018.
 */
data class AliasBody(val aliasRegistrations: List<Alias>?) : Serializable