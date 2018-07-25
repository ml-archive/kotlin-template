package dk.eboks.app.injection.components

import dagger.Subcomponent
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.pasta.activity.PastaActivity
import dk.eboks.app.pasta.activity.PastaPresenter
import dk.eboks.app.presentation.ui.channels.components.content.ekey.EkeyComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.EkeyComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.content.ekey.additem.EkeyAddItemComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.additem.EkeyAddItemComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.content.ekey.detail.EkeyDetailComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.detail.EkeyDetailComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.content.ekey.open.EkeyOpenItemComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.open.EkeyOpenItemComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.content.ekey.pin.EkeyPinComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.pin.EkeyPinComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.content.storebox.content.ChannelContentStoreboxComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.storebox.content.ChannelContentStoreboxComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.content.storebox.detail.ChannelContentStoreboxDetailComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.web.ChannelContentComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.web.ChannelContentComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.opening.ChannelOpeningComponentFragment
import dk.eboks.app.presentation.ui.channels.components.opening.ChannelOpeningComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.overview.ChannelOverviewComponentFragment
import dk.eboks.app.presentation.ui.channels.components.overview.ChannelOverviewComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.requirements.ChannelRequirementsComponentFragment
import dk.eboks.app.presentation.ui.channels.components.requirements.ChannelRequirementsComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.settings.ChannelSettingsComponentFragment
import dk.eboks.app.presentation.ui.channels.components.settings.ChannelSettingsComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.verification.ChannelVerificationComponentFragment
import dk.eboks.app.presentation.ui.channels.components.verification.ChannelVerificationComponentPresenter
import dk.eboks.app.presentation.ui.channels.screens.content.ChannelContentActivity
import dk.eboks.app.presentation.ui.channels.screens.content.ChannelContentPresenter
import dk.eboks.app.presentation.ui.channels.screens.content.ekey.EkeyContentActivity
import dk.eboks.app.presentation.ui.channels.screens.content.ekey.EkeyContentPresenter
import dk.eboks.app.presentation.ui.channels.screens.content.storebox.ConnectStoreboxActivity
import dk.eboks.app.presentation.ui.channels.screens.overview.ChannelOverviewActivity
import dk.eboks.app.presentation.ui.channels.screens.overview.ChannelOverviewPresenter
import dk.eboks.app.presentation.ui.debug.components.DebugOptionsComponentFragment
import dk.eboks.app.presentation.ui.debug.components.DebugOptionsComponentPresenter
import dk.eboks.app.presentation.ui.debug.components.DebugUsersComponentFragment
import dk.eboks.app.presentation.ui.debug.components.DebugUsersComponentPresenter
import dk.eboks.app.presentation.ui.debug.screens.hinter.HintActivity
import dk.eboks.app.presentation.ui.debug.screens.user.DebugUserActivity
import dk.eboks.app.presentation.ui.debug.screens.user.DebugUserPresenter
import dk.eboks.app.presentation.ui.folder.components.FoldersComponentFragment
import dk.eboks.app.presentation.ui.folder.components.FoldersComponentPresenter
import dk.eboks.app.presentation.ui.folder.components.newfolder.NewFolderComponentFragment
import dk.eboks.app.presentation.ui.folder.components.newfolder.NewFolderComponentPresenter
import dk.eboks.app.presentation.ui.folder.components.selectuser.FolderSelectUserComponentFragment
import dk.eboks.app.presentation.ui.folder.components.selectuser.FolderSelectUserComponentPresenter
import dk.eboks.app.presentation.ui.folder.screens.FolderActivity
import dk.eboks.app.presentation.ui.folder.screens.FolderPresenter
import dk.eboks.app.presentation.ui.home.components.channelcontrol.ChannelControlComponentFragment
import dk.eboks.app.presentation.ui.home.components.channelcontrol.ChannelControlComponentPresenter
import dk.eboks.app.presentation.ui.home.components.folderpreview.FolderPreviewComponentFragment
import dk.eboks.app.presentation.ui.home.components.folderpreview.FolderPreviewComponentPresenter
import dk.eboks.app.presentation.ui.home.screens.HomeActivity
import dk.eboks.app.presentation.ui.home.screens.HomePresenter
import dk.eboks.app.presentation.ui.login.components.*
import dk.eboks.app.presentation.ui.login.components.providers.WebLoginPresenter
import dk.eboks.app.presentation.ui.login.components.providers.bankidno.BankIdNOComponentFragment
import dk.eboks.app.presentation.ui.login.components.providers.bankidno.BankIdNOComponentPresenter
import dk.eboks.app.presentation.ui.login.components.providers.bankidse.BankIdSEComponentFragment
import dk.eboks.app.presentation.ui.login.components.providers.bankidse.BankIdSEComponentPresenter
import dk.eboks.app.presentation.ui.login.components.providers.idporten.IdPortenComponentFragment
import dk.eboks.app.presentation.ui.login.components.providers.idporten.IdPortenComponentPresenter
import dk.eboks.app.presentation.ui.login.components.providers.nemid.NemIdComponentFragment
import dk.eboks.app.presentation.ui.login.components.verification.VerificationComponentFragment
import dk.eboks.app.presentation.ui.login.components.verification.VerificationComponentPresenter
import dk.eboks.app.presentation.ui.login.screens.PopupLoginActivity
import dk.eboks.app.presentation.ui.login.screens.PopupLoginPresenter
import dk.eboks.app.presentation.ui.mail.components.foldershortcuts.FolderShortcutsComponentFragment
import dk.eboks.app.presentation.ui.mail.components.foldershortcuts.FolderShortcutsComponentPresenter
import dk.eboks.app.presentation.ui.mail.components.maillist.MailListComponentFragment
import dk.eboks.app.presentation.ui.mail.components.maillist.MailListComponentPresenter
import dk.eboks.app.presentation.ui.mail.components.sendercarousel.SenderCarouselComponentFragment
import dk.eboks.app.presentation.ui.mail.components.sendercarousel.SenderCarouselComponentPresenter
import dk.eboks.app.presentation.ui.mail.screens.list.MailListActivity
import dk.eboks.app.presentation.ui.mail.screens.list.MailListPresenter
import dk.eboks.app.presentation.ui.mail.screens.overview.MailOverviewActivity
import dk.eboks.app.presentation.ui.mail.screens.overview.MailOverviewPresenter
import dk.eboks.app.presentation.ui.message.components.detail.attachments.AttachmentsComponentFragment
import dk.eboks.app.presentation.ui.message.components.detail.attachments.AttachmentsComponentPresenter
import dk.eboks.app.presentation.ui.message.components.detail.document.DocumentComponentFragment
import dk.eboks.app.presentation.ui.message.components.detail.document.DocumentComponentPresenter
import dk.eboks.app.presentation.ui.message.components.detail.folderinfo.FolderInfoComponentFragment
import dk.eboks.app.presentation.ui.message.components.detail.folderinfo.FolderInfoComponentPresenter
import dk.eboks.app.presentation.ui.message.components.detail.header.HeaderComponentFragment
import dk.eboks.app.presentation.ui.message.components.detail.header.HeaderComponentPresenter
import dk.eboks.app.presentation.ui.message.components.detail.notes.NotesComponentFragment
import dk.eboks.app.presentation.ui.message.components.detail.notes.NotesComponentPresenter
import dk.eboks.app.presentation.ui.message.components.detail.reply.ReplyButtonComponentFragment
import dk.eboks.app.presentation.ui.message.components.detail.reply.ReplyButtonComponentPresenter
import dk.eboks.app.presentation.ui.message.components.detail.share.ShareComponentFragment
import dk.eboks.app.presentation.ui.message.components.detail.share.ShareComponentPresenter
import dk.eboks.app.presentation.ui.message.components.opening.privatesender.PrivateSenderWarningComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.privatesender.PrivateSenderWarningComponentPresenter
import dk.eboks.app.presentation.ui.message.components.opening.promulgation.PromulgationComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.promulgation.PromulgationComponentPresenter
import dk.eboks.app.presentation.ui.message.components.opening.protectedmessage.ProtectedMessageComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.protectedmessage.ProtectedMessageComponentPresenter
import dk.eboks.app.presentation.ui.message.components.opening.quarantine.QuarantineComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.quarantine.QuarantineComponentPresenter
import dk.eboks.app.presentation.ui.message.components.opening.recalled.RecalledComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.recalled.RecalledComponentPresenter
import dk.eboks.app.presentation.ui.message.components.opening.receipt.OpeningReceiptComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.receipt.OpeningReceiptComponentPresenter
import dk.eboks.app.presentation.ui.message.components.viewers.html.HtmlViewComponentFragment
import dk.eboks.app.presentation.ui.message.components.viewers.html.HtmlViewComponentPresenter
import dk.eboks.app.presentation.ui.message.components.viewers.image.ImageViewComponentFragment
import dk.eboks.app.presentation.ui.message.components.viewers.image.ImageViewComponentPresenter
import dk.eboks.app.presentation.ui.message.components.viewers.pdf.PdfViewComponentFragment
import dk.eboks.app.presentation.ui.message.components.viewers.pdf.PdfViewComponentPresenter
import dk.eboks.app.presentation.ui.message.components.viewers.text.TextViewComponentFragment
import dk.eboks.app.presentation.ui.message.components.viewers.text.TextViewComponentPresenter
import dk.eboks.app.presentation.ui.message.screens.MessageActivity
import dk.eboks.app.presentation.ui.message.screens.MessagePresenter
import dk.eboks.app.presentation.ui.message.screens.embedded.MessageEmbeddedActivity
import dk.eboks.app.presentation.ui.message.screens.embedded.MessageEmbeddedPresenter
import dk.eboks.app.presentation.ui.message.screens.opening.MessageOpeningActivity
import dk.eboks.app.presentation.ui.message.screens.opening.MessageOpeningPresenter
import dk.eboks.app.presentation.ui.message.screens.reply.ReplyFormActivity
import dk.eboks.app.presentation.ui.message.screens.reply.ReplyFormPresenter
import dk.eboks.app.presentation.ui.navigation.components.NavBarComponentFragment
import dk.eboks.app.presentation.ui.navigation.components.NavBarComponentPresenter
import dk.eboks.app.presentation.ui.overlay.screens.OverlayActivity
import dk.eboks.app.presentation.ui.overlay.screens.OverlayPresenter
import dk.eboks.app.presentation.ui.profile.components.HelpFragment
import dk.eboks.app.presentation.ui.profile.components.PrivacyFragment
import dk.eboks.app.presentation.ui.profile.components.drawer.*
import dk.eboks.app.presentation.ui.profile.components.main.ProfileInfoComponentFragment
import dk.eboks.app.presentation.ui.profile.components.main.ProfileInfoComponentPresenter
import dk.eboks.app.presentation.ui.profile.components.myinfo.MyInfoComponentFragment
import dk.eboks.app.presentation.ui.profile.components.myinfo.MyInfoComponentPresenter
import dk.eboks.app.presentation.ui.profile.screens.ProfileActivity
import dk.eboks.app.presentation.ui.profile.screens.ProfilePresenter
import dk.eboks.app.presentation.ui.profile.screens.myinfo.MyInfoActivity
import dk.eboks.app.presentation.ui.profile.screens.myinfo.MyInfoPresenter
import dk.eboks.app.presentation.ui.senders.components.SenderComponentFragment
import dk.eboks.app.presentation.ui.senders.components.SenderGroupsComponentFragment
import dk.eboks.app.presentation.ui.senders.components.SenderListComponentFragment
import dk.eboks.app.presentation.ui.senders.components.categories.CategoriesComponentFragment
import dk.eboks.app.presentation.ui.senders.components.categories.CategoriesComponentPresenter
import dk.eboks.app.presentation.ui.senders.components.list.SenderAllListComponentFragment
import dk.eboks.app.presentation.ui.senders.components.register.RegisterGroupComponentFragment
import dk.eboks.app.presentation.ui.senders.screens.browse.BrowseCategoryActivity
import dk.eboks.app.presentation.ui.senders.screens.browse.SearchSendersActivity
import dk.eboks.app.presentation.ui.senders.screens.detail.SenderDetailActivity
import dk.eboks.app.presentation.ui.senders.screens.list.SenderAllListActivity
import dk.eboks.app.presentation.ui.senders.screens.list.SenderAllListPresenter
import dk.eboks.app.presentation.ui.senders.screens.overview.SendersOverviewActivity
import dk.eboks.app.presentation.ui.senders.screens.overview.SendersOverviewPresenter
import dk.eboks.app.presentation.ui.senders.screens.registrations.PendingActivity
import dk.eboks.app.presentation.ui.senders.screens.registrations.RegistrationsActivity
import dk.eboks.app.presentation.ui.senders.screens.segment.SegmentDetailActivity
import dk.eboks.app.presentation.ui.start.components.signup.*
import dk.eboks.app.presentation.ui.start.screens.StartActivity
import dk.eboks.app.presentation.ui.start.screens.StartPresenter
import dk.eboks.app.presentation.ui.uploads.components.UploadOverviewComponentFragment
import dk.eboks.app.presentation.ui.uploads.components.UploadOverviewComponentPresenter
import dk.eboks.app.presentation.ui.uploads.components.uploadfile.UploadFileComponentFragment
import dk.eboks.app.presentation.ui.uploads.components.uploadfile.UploadFileComponentPresenter
import dk.eboks.app.presentation.ui.uploads.screens.UploadsActivity
import dk.eboks.app.presentation.ui.uploads.screens.UploadsPresenter
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
    fun inject(target : EkeyOpenItemComponentFragment)
    fun inject(target : EkeyOpenItemComponentPresenter)
    fun inject(target : EkeyPinComponentFragment)
    fun inject(target : EkeyPinComponentPresenter)

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
    fun inject(target : WebLoginPresenter)
    fun inject(target : IdPortenComponentFragment)
    fun inject(target : IdPortenComponentPresenter)
    fun inject(target : BankIdSEComponentFragment)
    fun inject(target : BankIdSEComponentPresenter)
    fun inject(target : BankIdNOComponentFragment)
    fun inject(target : BankIdNOComponentPresenter)
    fun inject(target : PopupLoginActivity)
    fun inject(target : PopupLoginPresenter)
  
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
    fun inject(target : FolderPreviewComponentFragment)
    fun inject(target : FolderPreviewComponentPresenter)
    fun inject(target : ChannelControlComponentFragment)
    fun inject(target : ChannelControlComponentPresenter)

    fun inject(target: PrivacyFragment)
    fun inject(target: HelpFragment)

    // verification

    fun inject(target : VerificationComponentFragment)
    fun inject(target : VerificationComponentPresenter)

    // brother

    fun inject(target : HintActivity)
    fun inject(target : PermissionRequestActivity)

    // upload

    fun inject(target : UploadOverviewComponentFragment)
    fun inject(target : UploadOverviewComponentPresenter)
    fun inject(target : UploadFileComponentFragment)
    fun inject(target : UploadFileComponentPresenter)

    // debug

    fun inject(target : DebugOptionsComponentFragment)
    fun inject(target : DebugOptionsComponentPresenter)
    fun inject(target : DebugUsersComponentFragment)
    fun inject(target : DebugUsersComponentPresenter)
    fun inject(target : DebugUserActivity)
    fun inject(target : DebugUserPresenter)
}
