package dk.eboks.app.domain.models.home

import com.google.gson.annotations.SerializedName

/**
 * Created by thsk on 28/03/2018.
 */
enum class ItemType(val type: String) {
    @SerializedName("list-messages")
    MESSAGES("list-messages"),
    @SerializedName("list-receipts")
    RECEIPTS("list-receipts"),
    @SerializedName("list-news")
    NEWS("list-news"),
    @SerializedName("list-files")
    FILES("list-files"),
    @SerializedName("list-images")
    IMAGES("list-images"),
    @SerializedName("list-notifications")
    NOTIFICATIONS("list-notifications");

    override fun toString(): String {
        return when (this) {
            MESSAGES -> "messages"
            RECEIPTS -> "receipts"
            NEWS -> "news"
            FILES -> "files"
            IMAGES -> "images"
            NOTIFICATIONS -> "notifications"
            else -> super.toString()
        }
    }
}