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
import dk.eboks.app.presentation.ui.components.channels.content.ChannelContentComponentFragment
import dk.eboks.app.presentation.ui.components.channels.content.ChannelContentComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.content.ChannelContentStoreboxComponentFragment
import dk.eboks.app.presentation.ui.components.channels.content.ChannelContentStoreboxComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.requirements.*
import dk.eboks.app.presentation.ui.components.channels.opening.ChannelOpeningComponentFragment
import dk.eboks.app.presentation.ui.components.channels.opening.ChannelOpeningComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.overview.ChannelOverviewComponentFragment
import dk.eboks.app.presentation.ui.components.channels.overview.ChannelOverviewComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.settings.*
import dk.eboks.app.presentation.ui.components.channels.verification.ChannelVerificationComponentFragment
import dk.eboks.app.presentation.ui.components.channels.verification.ChannelVerificationComponentPresenter
import dk.eboks.app.presentation.ui.screens.message.MessageActivity
import dk.eboks.app.presentation.ui.screens.message.MessagePresenter
import dk.eboks.app.presentation.ui.screens.message.embedded.MessageEmbeddedActivity
import dk.eboks.app.presentation.ui.screens.message.embedded.MessageEmbeddedPresenter
import dk.eboks.app.presentation.ui.components.message.detail.attachments.AttachmentsComponentFragment
import dk.eboks.app.presentation.ui.components.message.detail.attachments.AttachmentsComponentPresenter
import dk.eboks.app.presentation.ui.components.message.detail.document.DocumentComponentFragment
import dk.eboks.app.presentation.ui.components.message.detail.document.DocumentComponentPresenter
import dk.eboks.app.presentation.ui.components.message.detail.folderinfo.FolderInfoComponentFragment
import dk.eboks.app.presentation.ui.components.message.detail.folderinfo.FolderInfoComponentPresenter
import dk.eboks.app.presentation.ui.components.message.detail.header.HeaderComponentFragment
import dk.eboks.app.presentation.ui.components.message.detail.header.HeaderComponentPresenter
import dk.eboks.app.presentation.ui.components.message.opening.locked.LockedMessageComponentFragment
import dk.eboks.app.presentation.ui.components.message.opening.locked.LockedMessageComponentPresenter
import dk.eboks.app.presentation.ui.components.message.detail.notes.NotesComponentFragment
import dk.eboks.app.presentation.ui.components.message.detail.notes.NotesComponentPresenter
import dk.eboks.app.presentation.ui.components.message.opening.privatesender.PrivateSenderWarningComponentFragment
import dk.eboks.app.presentation.ui.components.message.opening.privatesender.PrivateSenderWarningComponentPresenter
import dk.eboks.app.presentation.ui.components.message.opening.protectedmessage.ProtectedMessageComponentFragment
import dk.eboks.app.presentation.ui.components.message.opening.protectedmessage.ProtectedMessageComponentPresenter
import dk.eboks.app.presentation.ui.components.message.detail.share.ShareComponentFragment
import dk.eboks.app.presentation.ui.components.message.detail.share.ShareComponentPresenter
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
import dk.eboks.app.presentation.ui.components.profile.MyInformationComponentFragment
import dk.eboks.app.presentation.ui.components.profile.MyInformationComponentPresenter
import dk.eboks.app.presentation.ui.components.senders.SenderListComponentFragment
import dk.eboks.app.presentation.ui.components.senders.SenderListComponentPresenter
import dk.eboks.app.presentation.ui.components.start.login.*
import dk.eboks.app.presentation.ui.components.start.signup.*
import dk.eboks.app.presentation.ui.components.verification.VerificationComponentFragment
import dk.eboks.app.presentation.ui.components.verification.VerificationComponentPresenter
import dk.eboks.app.presentation.ui.screens.channels.content.ChannelContentActivity
import dk.eboks.app.presentation.ui.screens.channels.content.ChannelContentPresenter
import dk.eboks.app.presentation.ui.screens.channels.overview.ChannelOverviewActivity
import dk.eboks.app.presentation.ui.screens.channels.overview.ChannelOverviewPresenter
import dk.eboks.app.presentation.ui.screens.start.StartActivity
import dk.eboks.app.presentation.ui.screens.start.StartPresenter
import dk.eboks.app.presentation.ui.screens.message.opening.MessageOpeningActivity
import dk.eboks.app.presentation.ui.screens.message.opening.MessageOpeningPresenter
import dk.eboks.app.presentation.ui.screens.senders.overview.SendersOverviewActivity
import dk.eboks.app.presentation.ui.screens.senders.overview.SendersOverviewPresenter
import dk.eboks.app.system.managers.permission.PermissionRequestActivity
import dk.nodes.arch.domain.injection.scopes.ActivityScope

/**
 * Created by bison on 26-07-2017.
 */

@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(PresentationModule::class))
@ActivityScope
interface PresentationComponent {
    // Screens
    fun inject(target : PastaActivity)
    fun inject(target : PastaPresenter)
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
    fun inject(target : ChannelOverviewActivity)
    fun inject(target : ChannelOverviewPresenter)
    fun inject(target : ChannelContentActivity)
    fun inject(target : ChannelContentPresenter)
    fun inject(target : SendersOverviewActivity)
    fun inject(target : SendersOverviewPresenter)
    fun inject(target : StartActivity)
    fun inject(target : StartPresenter)

    // Components

    // message
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
    fun inject(target : ShareComponentFragment)
    fun inject(target : ShareComponentPresenter)
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

    // mail
    fun inject(target : FoldersComponentFragment)
    fun inject(target : FoldersComponentPresenter)
    fun inject(target : FolderShortcutsComponentFragment)
    fun inject(target : FolderShortcutsComponentPresenter)
    fun inject(target : SenderCarouselComponentFragment)
    fun inject(target : SenderCarouselComponentPresenter)
    fun inject(target : MailListComponentFragment)
    fun inject(target : MailListComponentPresenter)
    fun inject(target : SenderListComponentFragment)
    fun inject(target : SenderListComponentPresenter)

    // generic
    fun inject(target : NavBarComponentFragment)
    fun inject(target : NavBarComponentPresenter)

    // channels
    fun inject(target : ChannelOverviewComponentFragment)
    fun inject(target : ChannelOverviewComponentPresenter)
    fun inject(target : ChannelRequirementsComponentFragment)
    fun inject(target : ChannelRequirementsComponentPresenter)
    fun inject(target : ChannelOpeningComponentFragment)
    fun inject(target : ChannelOpeningComponentPresenter)
    fun inject(target : ChannelVerificationComponentFragment)
    fun inject(target : ChannelVerificationComponentPresenter)
    fun inject(target : ChannelContentComponentFragment)
    fun inject(target : ChannelContentComponentPresenter)
    fun inject(target : ChannelContentStoreboxComponentFragment)
    fun inject(target : ChannelContentStoreboxComponentPresenter)
    fun inject(target : ChannelSettingsComponentFragment)
    fun inject(target : ChannelSettingsComponentPresenter)
    fun inject(target : ChannelSettingsStoreboxComponentFragment)
    fun inject(target : ChannelSettingsStoreboxComponentPresenter)
    fun inject(target : ChannelSettingsOptionsComponentFragment)
    fun inject(target : ChannelSettingsOptionsComponentPresenter)

    // signup
    fun inject(target : SignupComponentPresenter)
    fun inject(target : NameMailComponentFragment)
    fun inject(target : PasswordComponentFragment)
    fun inject(target : TermsComponentFragment)
    fun inject(target : SignupVerificationComponentFragment)
    fun inject(target : MMComponentFragment)
    fun inject(target : CompletedComponentFragment)
    fun inject(target : AcceptTermsComponentFragment)

    // login
    fun inject(target : UserCarouselComponentFragment)
    fun inject(target : UserCarouselComponentPresenter)
    fun inject(target : LoginComponentFragment)
    fun inject(target : LoginComponentPresenter)
    fun inject(target : ForgotPasswordComponentFragment)
    fun inject(target : ForgotPasswordComponentPresenter)
    fun inject(target : ActivationCodeComponentFragment)
    fun inject(target : ActivationCodeComponentPresenter)
  
    // profile
    fun inject(target : MyInformationComponentFragment)
    fun inject(target : MyInformationComponentPresenter)

    // verification
    fun inject(target : VerificationComponentFragment)
    fun inject(target : VerificationComponentPresenter)

    // brother
    fun inject(target : HintActivity)
    fun inject(target : PermissionRequestActivity)
}
