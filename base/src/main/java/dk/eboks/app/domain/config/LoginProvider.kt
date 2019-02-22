package dk.eboks.app.domain.config

import androidx.fragment.app.Fragment

/**
 * Created by bison on 11-03-2018.
 */
data class LoginProvider(
    val id: String,
    val name: String,
    val onlyVerified: Boolean,
    val icon: Int,
    val description: String? = null,
    val fragmentClass: Class<out Fragment>?,
    val fallbackProvider: String
) {
    override fun toString(): String = id
}
