package dk.eboks.app.domain.models.formreply

import com.google.gson.annotations.SerializedName

enum class FormInputType {
    @SerializedName("Description")
    DESCRIPTION,
    @SerializedName("Link")
    LINK,
    @SerializedName("Text")
    TEXT,
    @SerializedName("Textarea")
    TEXTAREA,
    @SerializedName("Number")
    NUMBER,
    @SerializedName("Dropdown")
    DROPDOWN,
    @SerializedName("List")
    LIST,
    @SerializedName("Radiobox")
    RADIOBOX,
    @SerializedName("Checkbox")
    CHECKBOX,
    @SerializedName("Date")
    DATE,
    @SerializedName("Datetime")
    DATETIME,
    @SerializedName("Upload")
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
