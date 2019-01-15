package dk.eboks.app.presentation.ui.main

import androidx.annotation.IdRes
import dk.eboks.app.R

interface MainNavigator {
    fun showMainSection(section: Section)
}

enum class Section(@IdRes val id: Int) {
    Home(R.id.actionHome),
    Mail(R.id.actionMail),
    Channels(R.id.actionChannels),
    Senders(R.id.actionSenders),
    Uploads(R.id.actionUploads);

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