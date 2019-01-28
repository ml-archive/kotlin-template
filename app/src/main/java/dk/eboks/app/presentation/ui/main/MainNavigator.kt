package dk.eboks.app.presentation.ui.main

import androidx.annotation.IdRes
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.folder.Folder

interface MainNavigator {
    fun showMainSection(section: Section)
}

enum class Section(@IdRes val id: Int, val title: String) {
    Home(R.id.actionHome, Translation.mainnav.homeButton),
    Mail(R.id.actionMail, Translation.mainnav.mailButton),
    Channels(R.id.actionChannels, Translation.mainnav.channelsButton),
    Senders(R.id.actionSenders, Translation.mainnav.sendersButton),
    Uploads(R.id.actionUploads, Translation.mainnav.uploadsButton),
    Inbox(R.id.actionInbox, Translation.mainnav.mailButton);

    companion object {
        fun fromId(@IdRes id: Int): Section {
            return when (id) {
                Home.id -> Home
                Mail.id -> Mail
                Channels.id -> Channels
                Senders.id -> Senders
                Uploads.id -> Uploads
                else -> throw IllegalArgumentException()
            }
        }
    }
}