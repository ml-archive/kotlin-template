package dk.eboks.app.domain.models.internal

import java.io.Serializable
import java.util.*

/**
 * Created by bison on 24-06-2017.
 */
data class User(
    var id : Long = -1,
    var name : String,
    var email : String,
    var avatarUri : String? = null
) : Serializable