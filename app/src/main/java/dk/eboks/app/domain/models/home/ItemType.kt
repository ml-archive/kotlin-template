package dk.eboks.app.domain.models.home

import com.google.gson.annotations.SerializedName

/**
 * Created by thsk on 28/03/2018.
 */
enum class ItemType(val type : String) {
    @SerializedName("list-messages") MESSAGES("list-messages"),
    @SerializedName("list-receipts") RECEIPTS("list-receipts"),
    @SerializedName("list-news") NEWS("list-news"),
    @SerializedName("list-files") FILES("list-files"),
    @SerializedName("list-images") IMAGES("list-images"),
    @SerializedName("list-notifications") NOTIFICATIONS("list-notifications");

    override fun toString(): String {
        when(this)
        {
            MESSAGES -> return "messages"
            RECEIPTS -> return "receipts"
            NEWS -> return "news"
            FILES -> return "files"
            IMAGES -> return "images"
            NOTIFICATIONS -> return "notifications"
            else -> return super.toString()
        }
    }
}