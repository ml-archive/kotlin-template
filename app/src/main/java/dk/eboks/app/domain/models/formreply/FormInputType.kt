package dk.eboks.app.domain.models.formreply

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class FormInputType(val type: String) : Parcelable {
    @SerializedName("description")
    DESCRIPTION("description"),
    @SerializedName("link")
    LINK("link"),
    @SerializedName("text")
    TEXT("text"),
    @SerializedName("textarea")
    TEXTAREA("textarea"),
    @SerializedName("number")
    NUMBER("number"),
    @SerializedName("dropdown")
    DROPDOWN("dropdown"),
    @SerializedName("list")
    LIST("list"),
    @SerializedName("radiobox")
    RADIOBOX("radiobox"),
    @SerializedName("checkbox")
    CHECKBOX("checkbox"),
    @SerializedName("date")
    DATE("date"),
    @SerializedName("datetime")
    DATETIME("datetime"),
    @SerializedName("upload")
    UPLOAD("upload");

    override fun toString(): String {
        when (this) {
            DESCRIPTION -> return "description"
            LINK -> return "link"
            TEXT -> return "text"
            TEXTAREA -> return "textarea"
            NUMBER -> return "number"
            DROPDOWN -> return "dropdown"
            LIST -> return "list"
            RADIOBOX -> return "radiobox"
            CHECKBOX -> return "checkbox"
            DATE -> return "date"
            DATETIME -> return "datetime"
            UPLOAD -> return "upload"
            else -> return "description"
        }
    }
}
