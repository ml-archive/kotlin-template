package dk.eboks.app.domain.config

import dk.eboks.app.presentation.base.BaseFragment

/**
 * Created by bison on 11-03-2018.
 */
data class LoginProvider (
        val id : String,
        val name : String,
        val onlyVerified : Boolean,
        val icon : Int,
        val description: String? = null,
        val fragmentClass: Class<out BaseFragment>?
)
{
    override fun toString(): String = id
}
