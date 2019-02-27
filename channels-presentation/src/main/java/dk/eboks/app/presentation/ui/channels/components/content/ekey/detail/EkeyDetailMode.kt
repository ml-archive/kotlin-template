package dk.eboks.app.presentation.ui.channels.components.content.ekey.detail

import dk.eboks.app.domain.models.Translation

enum class EkeyDetailMode(val type: String) {
    PIN("pin"),
    NOTE("note"),
    LOGIN("login");

    override fun toString(): String {
        when (this) {
            PIN -> return Translation.ekey.addItemPinCode
            NOTE -> return Translation.ekey.addItemNote
            LOGIN -> return Translation.ekey.addItemLogin
            else -> return "unknown"
        }
    }
}