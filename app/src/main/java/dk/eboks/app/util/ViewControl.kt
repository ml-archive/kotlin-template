package dk.eboks.app.util

import dk.eboks.app.presentation.ui.home.components.channelcontrol.ChannelControlComponentFragment
import dk.eboks.app.presentation.ui.home.components.folderpreview.FolderPreviewComponentFragment
import dk.eboks.app.presentation.ui.home.screens.HomeActivity
import dk.eboks.app.presentation.ui.mail.components.maillist.MailListComponentFragment
import dk.eboks.app.presentation.ui.mail.screens.overview.MailOverviewActivity

object ViewControl {
    fun refreshAllOnResume() {
        HomeActivity.refreshOnResume = true
        MailOverviewActivity.refreshOnResume = true
        MailListComponentFragment.refreshOnResume = true
        ChannelControlComponentFragment.refreshOnResume = true
        FolderPreviewComponentFragment.refreshOnResume = true
    }
}