package dk.eboks.app.domain.models.message

import com.google.gson.annotations.SerializedName

enum class MessageType(val type: String) {
    @SerializedName("upload")
    UPLOAD("upload"),
    @SerializedName("draft")
    DRAFT("draft"),
    @SerializedName("sent")
    SENT("sent"),
    @SerializedName("received")
    RECEIVED("received");

    override fun toString(): String {
        return when (this) {
            UPLOAD -> "upload"
            DRAFT -> "draft"
            SENT -> "sent"
            RECEIVED -> "received"
            else -> "received"
        }
    }
}
