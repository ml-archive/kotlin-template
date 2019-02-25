package dk.eboks.app.domain.models.folder

import com.google.gson.annotations.SerializedName

/**
 * Created by bison on 09-02-2018.
 */
enum class FolderType(val type: String) {
    @SerializedName("inbox")
    INBOX("inbox"),
    @SerializedName("drafts")
    DRAFTS("drafts"),
    @SerializedName("archive")
    ARCHIVE("archive"),
    @SerializedName("sentitems")
    SENT("sentitems"),
    @SerializedName("trash")
    TRASH("trash"),
    @SerializedName("deleteditems")
    DELETED("deleteditems"),
    @SerializedName("highlights")
    HIGHLIGHTS("highlights"),
    @SerializedName("unread")
    UNREAD("unread"),
    @SerializedName("latest")
    LATEST("latest"),
    @SerializedName("folder")
    FOLDER("folder"),
    @SerializedName("receipts")
    RECEIPTS("receipts"),
    @SerializedName("uploads")
    UPLOADS("uploads");

    override fun toString(): String {
        when (this) {
            INBOX -> return "inbox"
            DRAFTS -> return "drafts"
            ARCHIVE -> return "archive"
            SENT -> return "sentitems"
            DELETED -> return "deleteditems"
            HIGHLIGHTS -> return "highlights"
            UNREAD -> return "unread"
            LATEST -> return "latest"
            FOLDER -> return "folder"
            UPLOADS -> return "uploads"
            else -> return super.toString()
        }
    }
}

fun FolderType.isSystemFolder(): Boolean {
    return when (this) {
        FolderType.INBOX, FolderType.DRAFTS, FolderType.ARCHIVE, FolderType.SENT, FolderType.TRASH, FolderType.DELETED, FolderType.HIGHLIGHTS, FolderType.UNREAD, FolderType.LATEST, FolderType.RECEIPTS, FolderType.UPLOADS -> true
        else -> false
    }
}

