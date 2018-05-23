package dk.eboks.app.domain.models.message

import com.google.gson.annotations.SerializedName

enum class MessageType(val type : String) {
    @SerializedName("Upload") UPLOAD("Upload"),
    @SerializedName("Draft") DRAFT("Draft"),
    @SerializedName("Sent") SENT("Sent"),
    @SerializedName("Received") RECEIVED("Received");
    override fun toString(): String {
        when(this)
        {
            UPLOAD -> return "Upload"
            DRAFT -> return "Draft"
            SENT -> return "Sent"
            RECEIVED -> return "Received"
            else -> return "Received"
        }
    }
}
