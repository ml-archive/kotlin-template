package dk.eboks.app.domain.models.login

import java.io.Serializable

data class User(
        var id: Int = -1,
        var name: String = "",
        var emails: ArrayList<ContactPoint> = arrayListOf(ContactPoint()),
        var mobilenumber: ContactPoint? = null,
        var nationality: String? = "",
        var identity: String? = null,
        var identityType: String? = null,
        var verified: Boolean = false,
        var newsletter: Boolean = false,
        var avatarUri: String? = null
) : Serializable {

    fun getPrimaryEmail(): String? {
        return emails.getOrNull(0)?.value
    }

    fun getPrimaryEmailIsVerified(): Boolean {
        return emails.getOrNull(0)?.verified ?: false
    }

    fun getSecondaryEmailIsVerified(): Boolean {
        return emails.getOrNull(1)?.verified ?: false
    }

    fun setPrimaryEmail(string: String?) {
        emails[0] = ContactPoint(string, true)
    }

    fun getSecondaryEmail(): String? {
        return emails.getOrNull(1)?.value
    }

    fun setSecondaryEmail(string: String?) {
        if(emails.size > 1) {
            emails[1] = ContactPoint(string, true)
        } else {
            emails.add(1, ContactPoint(string, true))
        }
    }

    override fun equals(other: Any?): Boolean {
        if(other is User && id == other.id) {
            return true
        }
        return false
    }
}