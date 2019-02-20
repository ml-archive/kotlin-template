package dk.eboks.app.domain.models.formreply

import com.google.gson.annotations.SerializedName

enum class FormInputType {
    @SerializedName(value = "Description", alternate = ["description"])
    DESCRIPTION,
    @SerializedName(value = "Link", alternate = ["link"])
    LINK,
    @SerializedName(value = "Text", alternate = ["text"])
    TEXT,
    @SerializedName(value = "Textarea", alternate = ["textarea"])
    TEXTAREA,
    @SerializedName(value = "Number", alternate = ["number"])
    NUMBER,
    @SerializedName(value = "Dropdown", alternate = ["dropdown"])
    DROPDOWN,
    @SerializedName(value = "List", alternate = ["list"])
    LIST,
    @SerializedName(value = "Radiobox", alternate = ["radiobox"])
    RADIOBOX,
    @SerializedName(value = "Checkbox", alternate = ["checkbox"])
    CHECKBOX,
    @SerializedName(value = "Date", alternate = ["date"])
    DATE,
    @SerializedName(value = "Datetime", alternate = ["datetime"])
    DATETIME,
    @SerializedName(value = "Upload", alternate = ["upload"])
    UPLOAD;

    override fun toString(): String {
        return when (this) {
            DESCRIPTION -> "Description"
            LINK -> "Link"
            TEXT -> "Text"
            TEXTAREA -> "Textarea"
            NUMBER -> "Number"
            DROPDOWN -> "Dropdown"
            LIST -> "List"
            RADIOBOX -> "Radiobox"
            CHECKBOX -> "Checkbox"
            DATE -> "Date"
            DATETIME -> "Datetime"
            UPLOAD -> "Upload"
        }
    }
}
