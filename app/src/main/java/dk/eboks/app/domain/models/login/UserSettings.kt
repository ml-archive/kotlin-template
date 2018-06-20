package dk.eboks.app.domain.models.login

import java.io.Serializable

/**
 * Stores app-side settings and properties of a User
 * @author   Christian
 * @since    6/19/2018.
 */
data class UserSettings(
        var id: Int,
        var activationCode: String? = null,
        var lastLoginProviderId: String? = null,
        var hasFingerprint: Boolean = false,
        var stayLoggedIn: Boolean = false
) : Serializable {
    override fun toString(): String {
        return "UserSettings($id, $lastLoginProviderId, $activationCode, $hasFingerprint, $stayLoggedIn)"
    }
}