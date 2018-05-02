package dk.eboks.app.domain.models.login

import java.io.Serializable

data class User(
        var id: Long = -1,
        var name: String = "",
        var emails: ArrayList<ContactPoint> = arrayListOf(ContactPoint(), ContactPoint()),
        var mobileNumber: ContactPoint? = null,
        var cpr: String? = null,
        var avatarUri: String? = null,
        var lastLoginProvider: String? = null,
        var verified: Boolean = false,
        var hasFingerprint: Boolean = false,
        var newsletter: Boolean = false
) : Serializable {

    fun getPrimaryEmail(): String? {
        return emails.getOrNull(0)?.value
    }

    fun getSecondaryEmail(): String? {
        return emails.getOrNull(1)?.value
    }

    fun setPrimaryEmail(string: String?) {
        emails[0] = ContactPoint(string, true)
    }

    fun setSecondaryEmail(string: String?) {
        emails[1] = ContactPoint(string, true)
    }
}