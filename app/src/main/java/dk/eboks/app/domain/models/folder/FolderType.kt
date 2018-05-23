package dk.eboks.app.domain.models.folder

import com.google.gson.annotations.SerializedName
import dk.eboks.app.R

/**
 * Created by bison on 09-02-2018.
 */
enum class FolderType(val type : String) {
    @SerializedName("Inbox") INBOX("Inbox"),
    @SerializedName("Drafts") DRAFTS("Drafts"),
    @SerializedName("Archive") ARCHIVE("Archive"),
    @SerializedName("SentItems") SENT("SentItems"),
    @SerializedName("Trash") TRASH("Trash"),
    @SerializedName("DeletedItems") DELETED("DeletedItems"),
    @SerializedName("Highlights") HIGHLIGHTS("Highlights"),
    @SerializedName("Unread") UNREAD("Unread"),
    @SerializedName("Latest") LATEST("Latest"),
    @SerializedName("Folder") FOLDER("Folder"),
    @SerializedName("Receipts") RECEIPTS("Receipts"),
    @SerializedName("Uploads") UPLOADS("Uploads");

    fun isSystemFolder() : Boolean
    {
        when(this)
        {
            INBOX, DRAFTS, ARCHIVE, SENT, DELETED, HIGHLIGHTS, UNREAD, LATEST, UPLOADS -> return true
            else -> return false
        }
    }

    fun getIconResId() : Int
    {
        when(this)
        {
            INBOX -> return R.drawable.ic_inbox
            DRAFTS -> return R.drawable.ic_draft
            ARCHIVE -> return R.drawable.ic_archive
            SENT -> return R.drawable.ic_sent
            DELETED -> return R.drawable.ic_deleted
            HIGHLIGHTS -> return R.drawable.ic_highlights
            UNREAD -> return R.drawable.ic_folder
            LATEST -> return R.drawable.ic_folder
            UPLOADS -> return R.drawable.ic_folder
            else -> return R.drawable.ic_folder
        }
    }

    override fun toString(): String {
        when(this)
        {
            INBOX -> return "Inbox"
            DRAFTS -> return "Drafts"
            ARCHIVE -> return "Archive"
            SENT -> return "Sentitems"
            DELETED -> return "Deleteditems"
            HIGHLIGHTS -> return "Highlights"
            UNREAD -> return "Unread"
            LATEST -> return "Latest"
            FOLDER -> return "Folder"
            UPLOADS -> return "Uploads"
            else -> return super.toString()
        }
    }
}
