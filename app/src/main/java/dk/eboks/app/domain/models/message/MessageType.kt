package dk.eboks.app.domain.models.message

import com.google.gson.annotations.SerializedName

enum class MessageType(val type : String) {
    @SerializedName("upload") UPLOAD("upload"),
    @SerializedName("draft") DRAFT("draft"),
    @SerializedName("sent") SENT("sent"),
    @SerializedName("received") RECEIVED("received");
    override fun toString(): String {
        when(this)
        {
            UPLOAD -> return "upload"
            DRAFT -> return "draft"
            SENT -> return "sent"
            RECEIVED -> return "received"
            else -> return "received"
        }
    }
}
