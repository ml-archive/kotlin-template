package dk.eboks.app.domain.models.channel.ekey

import java.io.Serializable



sealed class Ekey(
        open val name : String,
        open val note: String?
)

data class Login(var username: String, var password: String, override val name: String, override val note: String?) : Ekey(name, note)
data class Note(override val name: String, override val note: String?) : Ekey(name, note)
data class Pin(var pin: String, override val name: String, override val note: String?) : Ekey(name, note)

