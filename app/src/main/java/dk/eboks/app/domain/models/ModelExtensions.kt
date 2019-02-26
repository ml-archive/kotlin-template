package dk.eboks.app.domain.models

import dk.eboks.app.R
import dk.eboks.app.domain.models.folder.FolderType

fun FolderType.getIconResId(): Int {
    return when (this) {
        FolderType.INBOX -> R.drawable.ic_inbox
        FolderType.DRAFTS -> R.drawable.ic_draft
        FolderType.ARCHIVE -> R.drawable.ic_archive
        FolderType.SENT -> R.drawable.ic_sent
        FolderType.DELETED -> R.drawable.ic_deleted
        FolderType.HIGHLIGHTS -> R.drawable.ic_highlights
        FolderType.UNREAD -> R.drawable.ic_folder
        FolderType.LATEST -> R.drawable.ic_folder
        FolderType.UPLOADS -> R.drawable.ic_folder
        else -> R.drawable.ic_folder
    }
}