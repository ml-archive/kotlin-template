package dk.eboks.app.domain.models.protocol

import com.google.gson.annotations.SerializedName

enum class ErrorType(val type : String) {
    @SerializedName("Error") ERROR("Error"),
    @SerializedName("Warning") WARNING("Warning"),
    @SerializedName("Information") INFORMATION("Information");
    override fun toString(): String {
        when(this)
        {
            ERROR -> return "Error"
            WARNING -> return "Warning"
            INFORMATION -> return "Information"
            else -> return "Error"
        }
    }
}
