package dk.eboks.app.domain.models.login

import java.io.Serializable

data class User(
        var id: Int = -1,
        var name: String = "",
        var emails: ArrayList<ContactPoint> = arrayListOf(ContactPoint()),
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

    fun setPrimaryEmail(string: String?) {
        emails[0] = ContactPoint(string, true)
    }

    fun getSecondaryEmail(): String? {
        return try {
            emails.getOrNull(1)?.value
        } catch ( e: Throwable) {
            ""
        }
    }

    fun setSecondaryEmail(string: String?) {
        if(emails.size > 1) {
            emails[1] = ContactPoint(string, true)
        } else {
            emails.add(1, ContactPoint(string, true))
        }
    }
}