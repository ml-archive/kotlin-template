package dk.eboks.app.domain.models

import dk.eboks.app.R
import dk.eboks.app.domain.models.folder.FolderType

fun FolderType.getIconResId(): Int {
    when (this) {
        FolderType.INBOX -> return R.drawable.ic_inbox
        FolderType.DRAFTS -> return R.drawable.ic_draft
        FolderType.ARCHIVE -> return R.drawable.ic_archive
        FolderType.SENT -> return R.drawable.ic_sent
        FolderType.DELETED -> return R.drawable.ic_deleted
        FolderType.HIGHLIGHTS -> return R.drawable.ic_highlights
        FolderType.UNREAD -> return R.drawable.ic_folder
        FolderType.LATEST -> return R.drawable.ic_folder
        FolderType.UPLOADS -> return R.drawable.ic_folder
        else -> return R.drawable.ic_folder
    }
}