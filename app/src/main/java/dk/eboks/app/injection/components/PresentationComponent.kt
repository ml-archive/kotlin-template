package dk.eboks.app.injection.components

import dagger.Component
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.presentation.ui.components.folder.folders.FoldersComponentFragment
import dk.eboks.app.presentation.ui.components.folder.folders.FoldersComponentPresenter
import dk.eboks.app.presentation.ui.components.mail.foldershortcuts.FolderShortcutsComponentFragment
import dk.eboks.app.presentation.ui.components.mail.foldershortcuts.FolderShortcutsComponentPresenter
import dk.eboks.app.presentation.ui.components.mail.maillist.MailListComponentFragment
import dk.eboks.app.presentation.ui.components.mail.maillist.MailListComponentPresenter
import dk.eboks.app.presentation.ui.components.mail.sendercarousel.SenderCarouselComponentFragment
import dk.eboks.app.presentation.ui.components.mail.sendercarousel.SenderCarouselComponentPresenter
import dk.eboks.app.presentation.ui.screens.debug.hinter.HintActivity
import dk.eboks.app.presentation.ui.screens.mail.folder.FolderActivity
import dk.eboks.app.presentation.ui.screens.mail.folder.FolderPresenter
import dk.eboks.app.presentation.ui.screens.mail.list.MailListActivity
import dk.eboks.app.presentation.ui.screens.mail.list.MailListPresenter
import dk.eboks.app.presentation.ui.screens.mail.overview.MailOverviewActivity
import dk.eboks.app.presentation.ui.screens.mail.overview.MailOverviewPresenter
import dk.eboks.app.pasta.activity.PastaActivity
import dk.eboks.app.pasta.activity.PastaPresenter
import dk.eboks.app.presentation.ui.components.channels.list.*
import dk.eboks.app.presentation.ui.screens.message.MessageActivity
import dk.eboks.app.presentation.ui.screens.message.MessagePresenter
import dk.eboks.app.presentation.ui.screens.message.embedded.MessageEmbeddedActivity
import dk.eboks.app.presentation.ui.screens.message.embedded.MessageEmbeddedPresenter
import dk.eboks.app.presentation.ui.components.message.attachments.AttachmentsComponentFragment
import dk.eboks.app.presentation.ui.components.message.attachments.AttachmentsComponentPresenter
import dk.eboks.app.presentation.ui.components.message.document.DocumentComponentFragment
import dk.eboks.app.presentation.ui.components.message.document.DocumentComponentPresenter
import dk.eboks.app.presentation.ui.components.message.folderinfo.FolderInfoComponentFragment
import dk.eboks.app.presentation.ui.components.message.folderinfo.FolderInfoComponentPresenter
import dk.eboks.app.presentation.ui.components.message.header.HeaderComponentFragment
import dk.eboks.app.presentation.ui.components.message.header.HeaderComponentPresenter
import dk.eboks.app.presentation.ui.components.message.locked.LockedMessageComponentFragment
import dk.eboks.app.presentation.ui.components.message.locked.LockedMessageComponentPresenter
import dk.eboks.app.presentation.ui.components.message.notes.NotesComponentFragment
import dk.eboks.app.presentation.ui.components.message.notes.NotesComponentPresenter
import dk.eboks.app.presentation.ui.components.message.privatesender.PrivateSenderWarningComponentFragment
import dk.eboks.app.presentation.ui.components.message.privatesender.PrivateSenderWarningComponentPresenter
import dk.eboks.app.presentation.ui.components.message.protectedmessage.ProtectedMessageComponentFragment
import dk.eboks.app.presentation.ui.components.message.protectedmessage.ProtectedMessageComponentPresenter
import dk.eboks.app.presentation.ui.components.message.share.ShareComponentFragment
import dk.eboks.app.presentation.ui.components.message.share.ShareComponentPresenter
import dk.eboks.app.presentation.ui.components.message.viewers.html.HtmlViewComponentFragment
import dk.eboks.app.presentation.ui.components.message.viewers.html.HtmlViewComponentPresenter
import dk.eboks.app.presentation.ui.components.message.viewers.image.ImageViewComponentFragment
import dk.eboks.app.presentation.ui.components.message.viewers.image.ImageViewComponentPresenter
import dk.eboks.app.presentation.ui.components.message.viewers.pdf.PdfViewComponentFragment
import dk.eboks.app.presentation.ui.components.message.viewers.pdf.PdfViewComponentPresenter
import dk.eboks.app.presentation.ui.components.message.viewers.text.TextViewComponentFragment
import dk.eboks.app.presentation.ui.components.message.viewers.text.TextViewComponentPresenter
import dk.eboks.app.presentation.ui.components.navigation.NavBarComponentFragment
import dk.eboks.app.presentation.ui.components.navigation.NavBarComponentPresenter
import dk.eboks.app.presentation.ui.components.senders.SenderListComponentFragment
import dk.eboks.app.presentation.ui.components.senders.SenderListComponentPresenter
import dk.eboks.app.presentation.ui.screens.channels.ChannelsActivity
import dk.eboks.app.presentation.ui.screens.channels.ChannelsPresenter
import dk.eboks.app.presentation.ui.screens.message.opening.MessageOpeningActivity
import dk.eboks.app.presentation.ui.screens.message.opening.MessageOpeningPresenter
import dk.eboks.app.presentation.ui.screens.splash.SplashActivity
import dk.eboks.app.presentation.ui.screens.splash.SplashPresenter
import dk.eboks.app.system.managers.permission.PermissionRequestActivity
import dk.nodes.arch.domain.injection.scopes.ActivityScope

/**
 * Created by bison on 26-07-2017.
 */

@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(PresentationModule::class))
@ActivityScope
interface PresentationComponent {
    fun inject(target : PastaActivity)
    fun inject(target : PastaPresenter)
    fun inject(target : SplashActivity)
    fun inject(target : SplashPresenter)
    fun inject(target : MailOverviewActivity)
    fun inject(target : MailOverviewPresenter)
    fun inject(target : MailListActivity)
    fun inject(target : MailListPresenter)
    fun inject(target : FolderActivity)
    fun inject(target : FolderPresenter)
    fun inject(target : MessageActivity)
    fun inject(target : MessagePresenter)
    fun inject(target : MessageEmbeddedActivity)
    fun inject(target : MessageEmbeddedPresenter)
    fun inject(target : MessageOpeningActivity)
    fun inject(target : MessageOpeningPresenter)
    fun inject(target : ChannelsActivity)
    fun inject(target : ChannelsPresenter)

    // components
    fun inject(target : HeaderComponentFragment)
    fun inject(target : HeaderComponentPresenter)
    fun inject(target : NotesComponentFragment)
    fun inject(target : NotesComponentPresenter)
    fun inject(target : AttachmentsComponentFragment)
    fun inject(target : AttachmentsComponentPresenter)
    fun inject(target : FolderInfoComponentFragment)
    fun inject(target : FolderInfoComponentPresenter)
    fun inject(target : DocumentComponentFragment)
    fun inject(target : DocumentComponentPresenter)
    fun inject(target : PdfViewComponentFragment)
    fun inject(target : PdfViewComponentPresenter)
    fun inject(target : FoldersComponentFragment)
    fun inject(target : FoldersComponentPresenter)
    fun inject(target : FolderShortcutsComponentFragment)
    fun inject(target : FolderShortcutsComponentPresenter)
    fun inject(target : SenderCarouselComponentFragment)
    fun inject(target : SenderCarouselComponentPresenter)
    fun inject(target : MailListComponentFragment)
    fun inject(target : MailListComponentPresenter)
    fun inject(target : NavBarComponentFragment)
    fun inject(target : NavBarComponentPresenter)
    fun inject(target : HtmlViewComponentFragment)
    fun inject(target : HtmlViewComponentPresenter)
    fun inject(target : ImageViewComponentFragment)
    fun inject(target : ImageViewComponentPresenter)
    fun inject(target : TextViewComponentFragment)
    fun inject(target : TextViewComponentPresenter)
    fun inject(target : LockedMessageComponentFragment)
    fun inject(target : LockedMessageComponentPresenter)
    fun inject(target : ProtectedMessageComponentFragment)
    fun inject(target : ProtectedMessageComponentPresenter)
    fun inject(target : PrivateSenderWarningComponentFragment)
    fun inject(target : PrivateSenderWarningComponentPresenter)
    fun inject(target : ChannelListComponentFragment)
    fun inject(target : ChannelListComponentPresenter)
    fun inject(target : ChannelDetailComponentFragment)
    fun inject(target : ChannelDetailComponentPresenter)
    fun inject(target : ChannelSettingsPopUpComponentFragment)
    fun inject(target : ChannelSettingsPopUpComponentPresenter)
    fun inject(target : ShareComponentFragment)
    fun inject(target : ShareComponentPresenter)
    fun inject(target : HintActivity)
    fun inject(target : PermissionRequestActivity)
    fun inject(target : SenderListComponentFragment)
    fun inject(target : SenderListComponentPresenter)
}
