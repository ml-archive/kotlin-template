package dk.eboks.app.domain.models.internal

import dk.eboks.app.domain.config.LoginProvider
import java.io.Serializable
import java.util.*

/**
 * Created by bison on 24-06-2017.
 */
data class User(
    var id : Long = -1,
    var name : String,
    var email : String? = null,
    var cpr : String? = null,
    var avatarUri : String? = null,
    var lastLoginProvider: LoginProvider? = null,
    var verified : Boolean,
    var hasFingerprint : Boolean
) : Serializable