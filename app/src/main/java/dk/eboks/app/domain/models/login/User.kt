package dk.eboks.app.domain.models.login

import java.io.Serializable

data class User(
    var id : Long = -1,
    var name : String,
    var email : String? = null,
    var cpr : String? = null,
    var avatarUri : String? = null,
    var lastLoginProvider: String? = null,
    var verified : Boolean,
    var hasFingerprint : Boolean
) : Serializable