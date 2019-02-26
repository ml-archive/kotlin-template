package dk.eboks.app.injection.components

import dagger.Subcomponent
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.pasta.activity.PastaActivity
import dk.eboks.app.presentation.ui.channels.components.content.ekey.EkeyComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.additem.EkeyAddItemComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.detail.EkeyDetailComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.open.EkeyOpenItemComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.pin.EkeyPinComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.storebox.content.ChannelContentStoreboxComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.storebox.detail.ChannelContentStoreboxDetailComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.web.ChannelContentComponentFragment
import dk.eboks.app.presentation.ui.channels.components.opening.ChannelOpeningComponentFragment
import dk.eboks.app.presentation.ui.channels.components.overview.ChannelOverviewComponentFragment
import dk.eboks.app.presentation.ui.channels.components.requirements.ChannelRequirementsComponentFragment
import dk.eboks.app.presentation.ui.channels.components.settings.ChannelSettingsComponentFragment
import dk.eboks.app.presentation.ui.channels.components.verification.ChannelVerificationComponentFragment
import dk.eboks.app.presentation.ui.channels.screens.content.ChannelContentActivity
import dk.eboks.app.presentation.ui.channels.screens.content.ekey.EkeyContentActivity
import dk.eboks.app.presentation.ui.channels.screens.content.storebox.ConnectStoreboxActivity
import dk.eboks.app.presentation.ui.channels.screens.overview.ChannelOverviewActivity
import dk.eboks.app.presentation.ui.debug.components.DebugOptionsComponentFragment
import dk.eboks.app.presentation.ui.debug.components.DebugUsersComponentFragment
import dk.eboks.app.presentation.ui.debug.screens.hinter.HintActivity
import dk.eboks.app.presentation.ui.debug.screens.user.DebugUserActivity
import dk.eboks.app.presentation.ui.folder.components.FoldersComponentFragment
import dk.eboks.app.presentation.ui.folder.components.newfolder.NewFolderComponentFragment
import dk.eboks.app.presentation.ui.folder.components.selectuser.FolderSelectUserComponentFragment
import dk.eboks.app.presentation.ui.folder.screens.FolderActivity
import dk.eboks.app.presentation.ui.home.components.channelcontrol.ChannelControlComponentFragment
import dk.eboks.app.presentation.ui.home.components.folderpreview.FolderPreviewComponentFragment
import dk.eboks.app.presentation.ui.home.screens.HomeActivity
import dk.eboks.app.presentation.ui.home.screens.HomeFragment
import dk.eboks.app.presentation.ui.login.components.ActivationCodeComponentFragment
import dk.eboks.app.presentation.ui.login.components.DeviceActivationComponentFragment
import dk.eboks.app.presentation.ui.login.components.ForgotPasswordComponentFragment
import dk.eboks.app.presentation.ui.login.components.ForgotPasswordDoneComponentFragment
import dk.eboks.app.presentation.ui.login.components.LoginComponentFragment
import dk.eboks.app.presentation.ui.login.components.UserCarouselComponentFragment
import dk.eboks.app.presentation.ui.login.components.providers.bankidno.BankIdNOComponentFragment
import dk.eboks.app.presentation.ui.login.components.providers.bankidse.BankIdSEComponentFragment
import dk.eboks.app.presentation.ui.login.components.providers.idporten.IdPortenComponentFragment
import dk.eboks.app.presentation.ui.login.components.providers.nemid.NemIdComponentFragment
import dk.eboks.app.presentation.ui.login.components.verification.VerificationComponentFragment
import dk.eboks.app.presentation.ui.login.screens.PopupLoginActivity
import dk.eboks.app.presentation.ui.mail.components.foldershortcuts.FolderShortcutsComponentFragment
import dk.eboks.app.presentation.ui.mail.components.maillist.MailListComponentFragment
import dk.eboks.app.presentation.ui.mail.components.sendercarousel.SenderCarouselComponentFragment
import dk.eboks.app.presentation.ui.mail.screens.list.MailListActivity
import dk.eboks.app.presentation.ui.mail.screens.overview.MailOverviewActivity
import dk.eboks.app.presentation.ui.mail.screens.overview.MailOverviewFragment
import dk.eboks.app.presentation.ui.message.components.detail.attachments.AttachmentsComponentFragment
import dk.eboks.app.presentation.ui.message.components.detail.document.DocumentComponentFragment
import dk.eboks.app.presentation.ui.message.components.detail.folderinfo.FolderInfoComponentFragment
import dk.eboks.app.presentation.ui.message.components.detail.header.HeaderComponentFragment
import dk.eboks.app.presentation.ui.message.components.detail.notes.NotesComponentFragment
import dk.eboks.app.presentation.ui.message.components.detail.reply.ReplyButtonComponentFragment
import dk.eboks.app.presentation.ui.message.components.detail.share.ShareComponentFragment
import dk.eboks.app.presentation.ui.message.components.detail.sign.SignButtonComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.privatesender.PrivateSenderWarningComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.promulgation.PromulgationComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.protectedmessage.ProtectedMessageComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.quarantine.QuarantineComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.recalled.RecalledComponentFragment
import dk.eboks.app.presentation.ui.message.components.opening.receipt.OpeningReceiptComponentFragment
import dk.eboks.app.presentation.ui.message.components.viewers.html.HtmlViewComponentFragment
import dk.eboks.app.presentation.ui.message.components.viewers.image.ImageViewComponentFragment
import dk.eboks.app.presentation.ui.message.components.viewers.pdf.PdfViewComponentFragment
import dk.eboks.app.presentation.ui.message.components.viewers.text.TextViewComponentFragment
import dk.eboks.app.presentation.ui.message.screens.MessageActivity
import dk.eboks.app.presentation.ui.message.screens.embedded.MessageEmbeddedActivity
import dk.eboks.app.presentation.ui.message.screens.opening.MessageOpeningActivity
import dk.eboks.app.presentation.ui.message.screens.reply.ReplyFormActivity
import dk.eboks.app.presentation.ui.message.screens.sign.SignActivity
import dk.eboks.app.presentation.ui.navigation.components.NavBarComponentFragment
import dk.eboks.app.presentation.ui.overlay.screens.OverlayActivity
import dk.eboks.app.presentation.ui.profile.components.HelpFragment
import dk.eboks.app.presentation.ui.profile.components.PrivacyFragment
import dk.eboks.app.presentation.ui.profile.components.drawer.EmailVerificationComponentFragment
import dk.eboks.app.presentation.ui.profile.components.drawer.FingerHintComponentFragment
import dk.eboks.app.presentation.ui.profile.components.drawer.FingerPrintComponentFragment
import dk.eboks.app.presentation.ui.profile.components.drawer.MergeAccountComponentFragment
import dk.eboks.app.presentation.ui.profile.components.drawer.PhoneVerificationComponentFragment
import dk.eboks.app.presentation.ui.profile.components.main.ProfileInfoComponentFragment
import dk.eboks.app.presentation.ui.profile.components.myinfo.MyInfoComponentFragment
import dk.eboks.app.presentation.ui.profile.screens.ProfileActivity
import dk.eboks.app.presentation.ui.profile.screens.myinfo.MyInfoActivity
import dk.eboks.app.presentation.ui.senders.components.SenderComponentFragment
import dk.eboks.app.presentation.ui.senders.components.SenderGroupsComponentFragment
import dk.eboks.app.presentation.ui.senders.components.SenderListComponentFragment
import dk.eboks.app.presentation.ui.senders.components.categories.CategoriesComponentFragment
import dk.eboks.app.presentation.ui.senders.components.list.SenderAllListComponentFragment
import dk.eboks.app.presentation.ui.senders.components.register.RegisterGroupComponentFragment
import dk.eboks.app.presentation.ui.senders.screens.browse.BrowseCategoryActivity
import dk.eboks.app.presentation.ui.senders.screens.browse.SearchSendersActivity
import dk.eboks.app.presentation.ui.senders.screens.detail.SenderDetailActivity
import dk.eboks.app.presentation.ui.senders.screens.list.SenderAllListActivity
import dk.eboks.app.presentation.ui.senders.screens.overview.SendersOverviewActivity
import dk.eboks.app.presentation.ui.senders.screens.overview.SerdersOverviewFragment
import dk.eboks.app.presentation.ui.senders.screens.registrations.PendingActivity
import dk.eboks.app.presentation.ui.senders.screens.registrations.RegistrationsActivity
import dk.eboks.app.presentation.ui.senders.screens.segment.SegmentDetailActivity
import dk.eboks.app.presentation.ui.start.components.signup.AcceptTermsComponentFragment
import dk.eboks.app.presentation.ui.start.components.signup.CompletedComponentFragment
import dk.eboks.app.presentation.ui.start.components.signup.MMComponentFragment
import dk.eboks.app.presentation.ui.start.components.signup.NameMailComponentFragment
import dk.eboks.app.presentation.ui.start.components.signup.PasswordComponentFragment
import dk.eboks.app.presentation.ui.start.components.signup.SignupVerificationComponentFragment
import dk.eboks.app.presentation.ui.start.screens.StartActivity
import dk.eboks.app.presentation.ui.uploads.components.UploadOverviewComponentFragment
import dk.eboks.app.presentation.ui.uploads.screens.UploadsActivity
import dk.eboks.app.presentation.ui.uploads.screens.fileupload.FileUploadActivity
import dk.eboks.app.system.managers.permission.PermissionRequestActivity
import dk.nodes.arch.domain.injection.scopes.ActivityScope

@Subcomponent(modules = [PresentationModule::class])
@ActivityScope
interface PresentationComponent {

    // Screens

    fun inject(target: PastaActivity)
    fun inject(target: MailOverviewActivity)
    fun inject(target: MailListActivity)
    fun inject(target: FolderActivity)
    fun inject(target: MessageActivity)
    fun inject(target: MessageEmbeddedActivity)
    fun inject(target: MessageOpeningActivity)
    fun inject(target: ChannelOverviewActivity)
    fun inject(target: ChannelContentActivity)
    fun inject(target: SendersOverviewActivity)
    fun inject(target: SerdersOverviewFragment)
    fun inject(target: StartActivity)
    fun inject(target: ProfileActivity)
    fun inject(target: UploadsActivity)
    fun inject(target: OverlayActivity)
    fun inject(target: ConnectStoreboxActivity)
    fun inject(target: MyInfoActivity)
    fun inject(target: SenderAllListActivity)
    fun inject(target: EkeyContentActivity)

    // Components

    // folder

    fun inject(target: NewFolderComponentFragment)
    fun inject(target: FolderSelectUserComponentFragment)

    // message

    fun inject(target: HeaderComponentFragment)
    fun inject(target: NotesComponentFragment)
    fun inject(target: AttachmentsComponentFragment)
    fun inject(target: FolderInfoComponentFragment)
    fun inject(target: DocumentComponentFragment)
    fun inject(target: PdfViewComponentFragment)
    fun inject(target: ShareComponentFragment)
    fun inject(target: HtmlViewComponentFragment)
    fun inject(target: ImageViewComponentFragment)
    fun inject(target: TextViewComponentFragment)
    fun inject(target: ProtectedMessageComponentFragment)
    fun inject(target: PrivateSenderWarningComponentFragment)
    fun inject(target: OpeningReceiptComponentFragment)
    fun inject(target: QuarantineComponentFragment)
    fun inject(target: RecalledComponentFragment)
    fun inject(target: PromulgationComponentFragment)
    fun inject(target: ReplyButtonComponentFragment)
    fun inject(target: SignButtonComponentFragment)
    fun inject(target: ReplyFormActivity)
    fun inject(target: SignActivity)

    // mail

    fun inject(target: FoldersComponentFragment)
    fun inject(target: FolderShortcutsComponentFragment)
    fun inject(target: SenderCarouselComponentFragment)
    fun inject(target: MailListComponentFragment)
    fun inject(target: SearchSendersActivity)
    fun inject(target: MailOverviewFragment)

    // generic

    fun inject(target: NavBarComponentFragment)

    // channels

    fun inject(target: ChannelOverviewComponentFragment)
    fun inject(target: ChannelRequirementsComponentFragment)
    fun inject(target: ChannelOpeningComponentFragment)
    fun inject(target: ChannelVerificationComponentFragment)
    fun inject(target: ChannelContentComponentFragment)
    fun inject(target: ChannelContentStoreboxComponentFragment)
    fun inject(target: ChannelContentStoreboxDetailComponentFragment)
    fun inject(target: ChannelSettingsComponentFragment)
    fun inject(target: EkeyComponentFragment)
    fun inject(target: EkeyAddItemComponentFragment)
    fun inject(target: EkeyDetailComponentFragment)
    fun inject(target: EkeyOpenItemComponentFragment)
    fun inject(target: EkeyPinComponentFragment)

    // senders

    fun inject(target: CategoriesComponentFragment)
    fun inject(target: BrowseCategoryActivity)
    fun inject(target: SenderGroupsComponentFragment)
    fun inject(target: SenderDetailActivity)
    fun inject(target: RegisterGroupComponentFragment)
    fun inject(target: SenderListComponentFragment)
    fun inject(target: SenderComponentFragment)
    fun inject(target: SegmentDetailActivity)
    fun inject(target: RegistrationsActivity)
    fun inject(target: PendingActivity)
    fun inject(target: SenderAllListComponentFragment)

    // sign up
    fun inject(target: NameMailComponentFragment)

    fun inject(target: PasswordComponentFragment)
    fun inject(target: SignupVerificationComponentFragment)
    fun inject(target: MMComponentFragment)
    fun inject(target: CompletedComponentFragment)
    fun inject(target: AcceptTermsComponentFragment)

    // login

    fun inject(target: UserCarouselComponentFragment)
    fun inject(target: LoginComponentFragment)
    fun inject(target: ForgotPasswordComponentFragment)
    fun inject(target: ForgotPasswordDoneComponentFragment)
    fun inject(target: ActivationCodeComponentFragment)
    fun inject(target: NemIdComponentFragment)
    fun inject(target: IdPortenComponentFragment)
    fun inject(target: BankIdSEComponentFragment)
    fun inject(target: BankIdNOComponentFragment)
    fun inject(target: PopupLoginActivity)
    fun inject(target: DeviceActivationComponentFragment)

    // profile

    fun inject(target: ProfileInfoComponentFragment)
    fun inject(target: MyInfoComponentFragment)
    fun inject(target: EmailVerificationComponentFragment)
    fun inject(target: PhoneVerificationComponentFragment)
    fun inject(target: MergeAccountComponentFragment)
    fun inject(target: FingerHintComponentFragment)
    fun inject(target: FingerPrintComponentFragment)

    // home
    fun inject(target: HomeActivity)

    fun inject(target: HomeFragment)
    fun inject(target: FolderPreviewComponentFragment)
    fun inject(target: ChannelControlComponentFragment)

    fun inject(target: PrivacyFragment)
    fun inject(target: HelpFragment)

    // verification

    fun inject(target: VerificationComponentFragment)

    // brother

    fun inject(target: HintActivity)
    fun inject(target: PermissionRequestActivity)

    // upload

    fun inject(target: UploadOverviewComponentFragment)
    fun inject(target: FileUploadActivity)

    // debug

    fun inject(target: DebugOptionsComponentFragment)
    fun inject(target: DebugUsersComponentFragment)
    fun inject(target: DebugUserActivity)
}
