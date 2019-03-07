package dk.eboks.app.domain.models.protocol

import com.google.gson.annotations.SerializedName

enum class ErrorType(val type: String) {
    @SerializedName("error")
    ERROR("error"),
    @SerializedName("warning")
    WARNING("warning"),
    @SerializedName("information")
    INFORMATION("information");

    override fun toString(): String {
        return when (this) {
            ERROR -> "error"
            WARNING -> "warning"
            INFORMATION -> "information"
            else -> "error"
        }
    }
}
