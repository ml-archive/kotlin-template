package dk.eboks.app.domain.models.folder

import com.google.gson.annotations.SerializedName
import dk.eboks.app.R

/**
 * Created by bison on 09-02-2018.
 */
enum class FolderType(val type : String) {
    @SerializedName("Inbox") INBOX("inbox"),
    @SerializedName("drafts") DRAFTS("drafts"),
    @SerializedName("archive") ARCHIVE("archive"),
    @SerializedName("sentitems") SENT("sentitems"),
    @SerializedName("deleteditems") DELETED("deleteditems"),
    @SerializedName("highlights") HIGHLIGHTS("highlights"),
    @SerializedName("unread") UNREAD("unread"),
    @SerializedName("latest") LATEST("latest"),
    @SerializedName("folder") FOLDER("folder"),
    @SerializedName("uploads") UPLOADS("uploads");

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
