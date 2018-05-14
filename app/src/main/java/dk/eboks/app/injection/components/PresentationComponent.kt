package dk.eboks.app.injection.components

import dagger.Subcomponent
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
import dk.eboks.app.presentation.ui.components.channels.content.*
import dk.eboks.app.presentation.ui.components.channels.content.ekey.EkeyComponentFragment
import dk.eboks.app.presentation.ui.components.channels.content.ekey.EkeyComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.content.ekey.additem.EkeyAddItemComponentFragment
import dk.eboks.app.presentation.ui.components.channels.content.ekey.additem.EkeyAddItemComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.content.ekey.detail.EkeyDetailComponentFragment
import dk.eboks.app.presentation.ui.components.channels.content.ekey.detail.EkeyDetailComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.content.storebox.ChannelContentStoreboxComponentFragment
import dk.eboks.app.presentation.ui.components.channels.content.storebox.ChannelContentStoreboxComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.content.storebox.ChannelContentStoreboxDetailComponentFragment
import dk.eboks.app.presentation.ui.components.channels.requirements.*
import dk.eboks.app.presentation.ui.components.channels.opening.ChannelOpeningComponentFragment
import dk.eboks.app.presentation.ui.components.channels.opening.ChannelOpeningComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.overview.ChannelOverviewComponentFragment
import dk.eboks.app.presentation.ui.components.channels.overview.ChannelOverviewComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.settings.*
import dk.eboks.app.presentation.ui.components.channels.verification.ChannelVerificationComponentFragment
import dk.eboks.app.presentation.ui.components.channels.verification.ChannelVerificationComponentPresenter
import dk.eboks.app.presentation.ui.components.debug.DebugOptionsComponentFragment
import dk.eboks.app.presentation.ui.components.debug.DebugOptionsComponentPresenter
import dk.eboks.app.presentation.ui.components.folder.folders.newfolder.NewFolderComponentFragment
import dk.eboks.app.presentation.ui.components.folder.folders.newfolder.NewFolderComponentPresenter
import dk.eboks.app.presentation.ui.components.folder.folders.selectuser.FolderSelectUserComponentFragment
import dk.eboks.app.presentation.ui.components.folder.folders.selectuser.FolderSelectUserComponentPresenter
import dk.eboks.app.presentation.ui.components.home.HomeComponentFragment
import dk.eboks.app.presentation.ui.components.home.HomeComponentPresenter
import dk.eboks.app.presentation.ui.components.home.channelcontrol.ChannelControlComponentFragment
import dk.eboks.app.presentation.ui.components.home.channelcontrol.ChannelControlComponentPresenter
import dk.eboks.app.presentation.ui.components.home.folderpreview.FolderPreviewComponentFragment
import dk.eboks.app.presentation.ui.components.home.folderpreview.FolderPreviewComponentPresenter
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
import dk.eboks.app.presentation.ui.components.message.detail.notes.NotesComponentFragment
import dk.eboks.app.presentation.ui.components.message.detail.notes.NotesComponentPresenter
import dk.eboks.app.presentation.ui.components.message.detail.reply.ReplyButtonComponentFragment
import dk.eboks.app.presentation.ui.components.message.detail.reply.ReplyButtonComponentPresenter
import dk.eboks.app.presentation.ui.components.message.opening.privatesender.PrivateSenderWarningComponentFragment
import dk.eboks.app.presentation.ui.components.message.opening.privatesender.PrivateSenderWarningComponentPresenter
import dk.eboks.app.presentation.ui.components.message.opening.protectedmessage.ProtectedMessageComponentFragment
import dk.eboks.app.presentation.ui.components.message.opening.protectedmessage.ProtectedMessageComponentPresenter
import dk.eboks.app.presentation.ui.components.message.detail.share.ShareComponentFragment
import dk.eboks.app.presentation.ui.components.message.detail.share.ShareComponentPresenter
import dk.eboks.app.presentation.ui.components.message.opening.promulgation.PromulgationComponentFragment
import dk.eboks.app.presentation.ui.components.message.opening.promulgation.PromulgationComponentPresenter
import dk.eboks.app.presentation.ui.components.message.opening.quarantine.QuarantineComponentFragment
import dk.eboks.app.presentation.ui.components.message.opening.quarantine.QuarantineComponentPresenter
import dk.eboks.app.presentation.ui.components.message.opening.recalled.RecalledComponentFragment
import dk.eboks.app.presentation.ui.components.message.opening.recalled.RecalledComponentPresenter
import dk.eboks.app.presentation.ui.components.message.opening.receipt.OpeningReceiptComponentFragment
import dk.eboks.app.presentation.ui.components.message.opening.receipt.OpeningReceiptComponentPresenter
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
import dk.eboks.app.presentation.ui.components.profile.drawer.*
import dk.eboks.app.presentation.ui.components.profile.main.ProfileInfoComponentFragment
import dk.eboks.app.presentation.ui.components.profile.main.ProfileInfoComponentPresenter
import dk.eboks.app.presentation.ui.components.profile.myinfo.MyInfoComponentFragment
import dk.eboks.app.presentation.ui.components.profile.myinfo.MyInfoComponentPresenter
import dk.eboks.app.presentation.ui.components.senders.SenderComponentFragment
import dk.eboks.app.presentation.ui.components.senders.SenderGroupsComponentFragment
import dk.eboks.app.presentation.ui.components.senders.SenderListComponentFragment
import dk.eboks.app.presentation.ui.components.senders.categories.CategoriesComponentFragment
import dk.eboks.app.presentation.ui.components.senders.categories.CategoriesComponentPresenter
import dk.eboks.app.presentation.ui.components.senders.list.SenderAllListComponentFragment
import dk.eboks.app.presentation.ui.components.senders.register.RegisterGroupComponentFragment
import dk.eboks.app.presentation.ui.components.start.login.*
import dk.eboks.app.presentation.ui.components.start.login.providers.bankidno.BankIdNOComponentFragment
import dk.eboks.app.presentation.ui.components.start.login.providers.bankidno.BankIdNOComponentPresenter
import dk.eboks.app.presentation.ui.components.start.login.providers.bankidse.BankIdSEComponentFragment
import dk.eboks.app.presentation.ui.components.start.login.providers.bankidse.BankIdSEComponentPresenter
import dk.eboks.app.presentation.ui.components.start.login.providers.idporten.IdPortenComponentFragment
import dk.eboks.app.presentation.ui.components.start.login.providers.idporten.IdPortenComponentPresenter
import dk.eboks.app.presentation.ui.components.start.login.providers.nemid.NemIdComponentFragment
import dk.eboks.app.presentation.ui.components.start.login.providers.nemid.NemIdComponentPresenter
import dk.eboks.app.presentation.ui.components.start.signup.*
import dk.eboks.app.presentation.ui.components.uploads.UploadOverviewComponentFragment
import dk.eboks.app.presentation.ui.components.uploads.UploadOverviewComponentPresenter
import dk.eboks.app.presentation.ui.components.uploads.myuploads.MyUploadsComponentFragment
import dk.eboks.app.presentation.ui.components.uploads.myuploads.MyUploadsComponentPresenter
import dk.eboks.app.presentation.ui.components.uploads.uploadfile.UploadFileComponentFragment
import dk.eboks.app.presentation.ui.components.uploads.uploadfile.UploadFileComponentPresenter
import dk.eboks.app.presentation.ui.components.verification.VerificationComponentFragment
import dk.eboks.app.presentation.ui.components.verification.VerificationComponentPresenter
import dk.eboks.app.presentation.ui.screens.overlay.OverlayActivity
import dk.eboks.app.presentation.ui.screens.overlay.OverlayPresenter
import dk.eboks.app.presentation.ui.screens.channels.content.ChannelContentActivity
import dk.eboks.app.presentation.ui.screens.channels.content.ChannelContentPresenter
import dk.eboks.app.presentation.ui.screens.channels.content.ekey.EkeyContentActivity
import dk.eboks.app.presentation.ui.screens.channels.content.ekey.EkeyContentPresenter
import dk.eboks.app.presentation.ui.screens.channels.content.storebox.ConnectStoreboxActivity
import dk.eboks.app.presentation.ui.screens.channels.content.storebox.StoreboxContentActivity
import dk.eboks.app.presentation.ui.screens.channels.content.storebox.StoreboxContentPresenter
import dk.eboks.app.presentation.ui.screens.channels.content.storebox.StoreboxSignInFragment
import dk.eboks.app.presentation.ui.screens.channels.overview.ChannelOverviewActivity
import dk.eboks.app.presentation.ui.screens.channels.overview.ChannelOverviewPresenter
import dk.eboks.app.presentation.ui.screens.debug.user.DebugUserActivity
import dk.eboks.app.presentation.ui.screens.debug.user.DebugUserPresenter
import dk.eboks.app.presentation.ui.screens.home.HomeActivity
import dk.eboks.app.presentation.ui.screens.home.HomePresenter
import dk.eboks.app.presentation.ui.screens.start.StartActivity
import dk.eboks.app.presentation.ui.screens.start.StartPresenter
import dk.eboks.app.presentation.ui.screens.message.opening.MessageOpeningActivity
import dk.eboks.app.presentation.ui.screens.message.opening.MessageOpeningPresenter
import dk.eboks.app.presentation.ui.screens.message.reply.ReplyFormActivity
import dk.eboks.app.presentation.ui.screens.message.reply.ReplyFormPresenter
import dk.eboks.app.presentation.ui.screens.profile.ProfileActivity
import dk.eboks.app.presentation.ui.screens.profile.ProfilePresenter
import dk.eboks.app.presentation.ui.screens.profile.myinfo.MyInfoActivity
import dk.eboks.app.presentation.ui.screens.profile.myinfo.MyInfoPresenter
import dk.eboks.app.presentation.ui.screens.senders.browse.BrowseCategoryActivity
import dk.eboks.app.presentation.ui.screens.senders.browse.SearchSendersActivity
import dk.eboks.app.presentation.ui.screens.senders.detail.SenderDetailActivity
import dk.eboks.app.presentation.ui.screens.senders.list.SenderAllListActivity
import dk.eboks.app.presentation.ui.screens.senders.list.SenderAllListPresenter
import dk.eboks.app.presentation.ui.screens.senders.overview.SendersOverviewActivity
import dk.eboks.app.presentation.ui.screens.senders.overview.SendersOverviewPresenter
import dk.eboks.app.presentation.ui.screens.senders.registrations.PendingActivity
import dk.eboks.app.presentation.ui.screens.senders.registrations.RegistrationsActivity
import dk.eboks.app.presentation.ui.screens.senders.segment.SegmentDetailActivity
import dk.eboks.app.presentation.ui.screens.uploads.UploadsActivity
import dk.eboks.app.presentation.ui.screens.uploads.UploadsPresenter
import dk.eboks.app.system.managers.permission.PermissionRequestActivity
import dk.nodes.arch.domain.injection.scopes.ActivityScope

@Subcomponent(modules = [PresentationModule::class])
@ActivityScope
interface PresentationComponent {

    // Screens

    fun inject(target: PastaActivity)
    fun inject(target: PastaPresenter)
    fun inject(target: MailOverviewActivity)
    fun inject(target: MailOverviewPresenter)
    fun inject(target: MailListActivity)
    fun inject(target: MailListPresenter)
    fun inject(target: FolderActivity)
    fun inject(target: FolderPresenter)
    fun inject(target: MessageActivity)
    fun inject(target: MessagePresenter)
    fun inject(target: MessageEmbeddedActivity)
    fun inject(target: MessageEmbeddedPresenter)
    fun inject(target: MessageOpeningActivity)
    fun inject(target: MessageOpeningPresenter)
    fun inject(target: ChannelOverviewActivity)
    fun inject(target: ChannelOverviewPresenter)
    fun inject(target: ChannelContentActivity)
    fun inject(target: ChannelContentPresenter)
    fun inject(target: SendersOverviewActivity)
    fun inject(target: SendersOverviewPresenter)
    fun inject(target: StartActivity)
    fun inject(target: StartPresenter)
    fun inject(target: ProfileActivity)
    fun inject(target: ProfilePresenter)
    fun inject(target: UploadsActivity)
    fun inject(target: UploadsPresenter)
    fun inject(target: OverlayActivity)
    fun inject(target: OverlayPresenter)
    fun inject(target: ConnectStoreboxActivity)
    fun inject(target: StoreboxSignInFragment)
    fun inject(target: StoreboxContentActivity)
    fun inject(target: StoreboxContentPresenter)
    fun inject(target: MyInfoActivity)
    fun inject(target: MyInfoPresenter)
    fun inject(target: SenderAllListActivity)
    fun inject(target: SenderAllListPresenter)
    fun inject(target: EkeyContentActivity)
    fun inject(target: EkeyContentPresenter)

    // Components

    //folder

    fun inject(target : NewFolderComponentFragment)
    fun inject(target : NewFolderComponentPresenter)
    fun inject(target : FolderSelectUserComponentFragment)
    fun inject(target : FolderSelectUserComponentPresenter)


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
    fun inject(target : ProtectedMessageComponentFragment)
    fun inject(target : ProtectedMessageComponentPresenter)
    fun inject(target : PrivateSenderWarningComponentFragment)
    fun inject(target : PrivateSenderWarningComponentPresenter)
    fun inject(target : OpeningReceiptComponentFragment)
    fun inject(target : OpeningReceiptComponentPresenter)
    fun inject(target : QuarantineComponentFragment)
    fun inject(target : QuarantineComponentPresenter)
    fun inject(target : RecalledComponentFragment)
    fun inject(target : RecalledComponentPresenter)
    fun inject(target : PromulgationComponentFragment)
    fun inject(target : PromulgationComponentPresenter)
    fun inject(target : ReplyButtonComponentFragment)
    fun inject(target : ReplyButtonComponentPresenter)
    fun inject(target : ReplyFormActivity)
    fun inject(target : ReplyFormPresenter)

    // mail

    fun inject(target : FoldersComponentFragment)
    fun inject(target : FoldersComponentPresenter)
    fun inject(target : FolderShortcutsComponentFragment)
    fun inject(target : FolderShortcutsComponentPresenter)
    fun inject(target : SenderCarouselComponentFragment)
    fun inject(target : SenderCarouselComponentPresenter)
    fun inject(target : MailListComponentFragment)
    fun inject(target : MailListComponentPresenter)
    fun inject(target : SearchSendersActivity)

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
    fun inject(target : ChannelContentStoreboxDetailComponentFragment)
    fun inject(target : ChannelContentStoreboxComponentPresenter)
    fun inject(target : ChannelSettingsComponentFragment)
    fun inject(target : ChannelSettingsComponentPresenter)
    fun inject(target : EkeyComponentFragment)
    fun inject(target : EkeyComponentPresenter)
    fun inject(target : EkeyAddItemComponentFragment)
    fun inject(target : EkeyAddItemComponentPresenter)
    fun inject(target : EkeyDetailComponentFragment)
    fun inject(target : EkeyDetailComponentPresenter)

    // senders

    fun inject(target : CategoriesComponentFragment)
    fun inject(target : CategoriesComponentPresenter)
    fun inject(target : BrowseCategoryActivity)
    fun inject(target : SenderGroupsComponentFragment)
    fun inject(target : SenderDetailActivity)
    fun inject(target : RegisterGroupComponentFragment)
    fun inject(target : SenderListComponentFragment)
    fun inject(target : SenderComponentFragment)
    fun inject(target : SegmentDetailActivity)
    fun inject(target : RegistrationsActivity)
    fun inject(target : PendingActivity)
    fun inject(target : SenderAllListComponentFragment)

    // sign up

    fun inject(target : SignupComponentPresenter)
    fun inject(target : NameMailComponentFragment)
    fun inject(target : PasswordComponentFragment)
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
    fun inject(target : ForgotPasswordDoneComponentFragment)
    fun inject(target : ForgotPasswordDoneComponentPresenter)
    fun inject(target : ActivationCodeComponentFragment)
    fun inject(target : ActivationCodeComponentPresenter)
    fun inject(target : NemIdComponentFragment)
    fun inject(target : NemIdComponentPresenter)
    fun inject(target : IdPortenComponentFragment)
    fun inject(target : IdPortenComponentPresenter)
    fun inject(target : BankIdSEComponentFragment)
    fun inject(target : BankIdSEComponentPresenter)
    fun inject(target : BankIdNOComponentFragment)
    fun inject(target : BankIdNOComponentPresenter)
  
    // profile

    fun inject(target: ProfileInfoComponentFragment)
    fun inject(target: ProfileInfoComponentPresenter)
    fun inject(target: MyInfoComponentFragment)
    fun inject(target: MyInfoComponentPresenter)
    fun inject(target: EmailVerificationComponentPresenter)
    fun inject(target: EmailVerificationComponentFragment)
    fun inject(target: PhoneVerificationComponentPresenter)
    fun inject(target: PhoneVerificationComponentFragment)
    fun inject(target: MergeAccountComponentPresenter)
    fun inject(target: MergeAccountComponentFragment)
    fun inject(target: FingerHintComponentPresenter)
    fun inject(target: FingerHintComponentFragment)
    fun inject(target: FingerPrintComponentPresenter)
    fun inject(target: FingerPrintComponentFragment)

    // home
    fun inject(target: HomeActivity)
    fun inject(target: HomePresenter)
    fun inject(target : HomeComponentFragment)
    fun inject(target : HomeComponentPresenter)
    fun inject(target : FolderPreviewComponentFragment)
    fun inject(target : FolderPreviewComponentPresenter)
    fun inject(target : ChannelControlComponentFragment)
    fun inject(target : ChannelControlComponentPresenter)


    // verification

    fun inject(target : VerificationComponentFragment)
    fun inject(target : VerificationComponentPresenter)

    // brother

    fun inject(target : HintActivity)
    fun inject(target : PermissionRequestActivity)

    // upload

    fun inject(target : UploadOverviewComponentFragment)
    fun inject(target : UploadOverviewComponentPresenter)
    fun inject(target : MyUploadsComponentFragment)
    fun inject(target : MyUploadsComponentPresenter)
    fun inject(target : UploadFileComponentFragment)
    fun inject(target : UploadFileComponentPresenter)

    // debug

    fun inject(target : DebugOptionsComponentFragment)
    fun inject(target : DebugOptionsComponentPresenter)
    fun inject(target : DebugUserActivity)
    fun inject(target : DebugUserPresenter)
}
