package dk.eboks.app.domain.models

import com.google.gson.annotations.SerializedName
import dk.eboks.app.R

/**
 * Created by bison on 09-02-2018.
 */
enum class FolderType(val type : String) {
    @SerializedName("inbox") INBOX("inbox"),
    @SerializedName("drafts") DRAFTS("drafts"),
    @SerializedName("archive") ARCHIVE("archive"),
    @SerializedName("sentitems") SENT("sentitems"),
    @SerializedName("deleteditems") DELETED("deleteditems"),
    @SerializedName("highlights") HIGHLIGHTS("highlights"),
    @SerializedName("unread") UNREAD("unread"),
    @SerializedName("latest") LATEST("latest"),
    @SerializedName("folder") FOLDER("folder");

    fun isSystemFolder() : Boolean
    {
        when(this)
        {
            INBOX, DRAFTS, ARCHIVE, SENT, DELETED, HIGHLIGHTS, UNREAD, LATEST -> return true
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
            HIGHLIGHTS  -> return R.drawable.ic_highlights
            UNREAD -> return R.drawable.ic_folder
            LATEST -> return R.drawable.ic_folder
            else -> return R.drawable.ic_folder
        }
    }
}
