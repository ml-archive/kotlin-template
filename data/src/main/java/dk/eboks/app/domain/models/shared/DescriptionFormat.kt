package dk.eboks.app.domain.models.shared

import com.google.gson.annotations.SerializedName

/**
 * Created by bison on 09-02-2018.
 */
enum class DescriptionFormat(val type: String) {
    @SerializedName("text")
    TEXT("text"),
    @SerializedName("html")
    HTML("html");

    override fun toString(): String {
        return when (this) {
            TEXT -> "text"
            HTML -> "html"
            else -> super.toString()
        }
    }
}
