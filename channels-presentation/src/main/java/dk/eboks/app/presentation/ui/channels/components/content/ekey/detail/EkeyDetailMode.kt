package dk.eboks.app.presentation.ui.channels.components.content.ekey.detail

import dk.eboks.app.domain.models.Translation

enum class EkeyDetailMode(val type: String) {
    PIN("pin"),
    NOTE("note"),
    LOGIN("login");

    override fun toString(): String {
        return when (this) {
            PIN -> Translation.ekey.addItemPinCode
            NOTE -> Translation.ekey.addItemNote
            LOGIN -> Translation.ekey.addItemLogin
            else -> "unknown"
        }
    }
}