package dk.eboks.app.domain.models.sender

import java.io.Serializable

data class Alias(val name: String, val key: String? = "", var value: String? = "") : Serializable {

    override fun toString(): String {
        return key ?: ""
    }
}