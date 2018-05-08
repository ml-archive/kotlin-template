package dk.eboks.app.presentation.ui.components.channels.content.ekey.detail

enum class EkeyDetailMode(val type: String) {
    PIN("pin"),
    NOTE("note"),
    LOGIN("login");


    override fun toString(): String {
        when (this) {
            PIN -> return "pin"
            NOTE -> return "note"
            LOGIN -> return "login"
            else -> return "unknown"
        }
    }
}