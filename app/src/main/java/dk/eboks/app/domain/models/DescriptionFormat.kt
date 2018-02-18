package dk.eboks.app.domain.models

import com.google.gson.annotations.SerializedName
import dk.eboks.app.R

/**
 * Created by bison on 09-02-2018.
 */
enum class DescriptionFormat(val type : String) {
    @SerializedName("text") TEXT("text"),
    @SerializedName("html") HTML("html");

    override fun toString(): String {
        when(this)
        {
            TEXT -> return "text"
            HTML -> return "html"
            else -> return super.toString()
        }
    }
}
