package dk.eboks.app.injection.modules

import dagger.Module
import dagger.Provides
import dk.eboks.app.pasta.activity.PastaContract
import dk.eboks.app.pasta.activity.PastaPresenter
import dk.eboks.app.presentation.ui.channels.components.content.ekey.EkeyComponentContract
import dk.eboks.app.presentation.ui.channels.components.content.ekey.EkeyComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.content.ekey.additem.EkeyAddItemComponentContract
import dk.eboks.app.presentation.ui.channels.components.content.ekey.additem.EkeyAddItemComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.content.ekey.detail.EkeyDetailComponentContract
import dk.eboks.app.presentation.ui.channels.components.content.ekey.detail.EkeyDetailComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.content.ekey.open.EkeyOpenItemComponentContract
import dk.eboks.app.presentation.ui.channels.components.content.ekey.open.EkeyOpenItemComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.content.ekey.pin.EkeyPinComponentContract
import dk.eboks.app.presentation.ui.channels.components.content.ekey.pin.EkeyPinComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.content.storebox.content.ChannelContentStoreboxComponentContract
import dk.eboks.app.presentation.ui.channels.components.content.storebox.content.ChannelContentStoreboxComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.content.storebox.detail.ChannelContentStoreboxDetailComponentContract
import dk.eboks.app.presentation.ui.channels.components.content.storebox.detail.ChannelContentStoreboxDetailComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.content.web.ChannelContentComponentContract
import dk.eboks.app.presentation.ui.channels.components.content.web.ChannelContentComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.opening.ChannelOpeningComponentContract
import dk.eboks.app.presentation.ui.channels.components.opening.ChannelOpeningComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.overview.ChannelOverviewComponentContract
import dk.eboks.app.presentation.ui.channels.components.overview.ChannelOverviewComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.requirements.ChannelRequirementsComponentContract
import dk.eboks.app.presentation.ui.channels.components.requirements.ChannelRequirementsComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.settings.ChannelSettingsComponentContract
import dk.eboks.app.presentation.ui.channels.components.settings.ChannelSettingsComponentPresenter
import dk.eboks.app.presentation.ui.channels.components.verification.ChannelVerificationComponentContract
import dk.eboks.app.presentation.ui.channels.components.verification.ChannelVerificationComponentPresenter
import dk.eboks.app.presentation.ui.channels.screens.content.ChannelContentContract
import dk.eboks.app.presentation.ui.channels.screens.content.ChannelContentPresenter
import dk.eboks.app.presentation.ui.channels.screens.content.ekey.EkeyContentContract
import dk.eboks.app.presentation.ui.channels.screens.content.ekey.EkeyContentPresenter
import dk.eboks.app.presentation.ui.channels.screens.content.storebox.ConnectStoreboxContract
import dk.eboks.app.presentation.ui.channels.screens.content.storebox.ConnectStoreboxPresenter
import dk.eboks.app.presentation.ui.channels.screens.overview.ChannelOverviewContract
import dk.eboks.app.presentation.ui.channels.screens.overview.ChannelOverviewPresenter
import dk.eboks.app.presentation.ui.debug.components.DebugOptionsComponentContract
import dk.eboks.app.presentation.ui.debug.components.DebugOptionsComponentPresenter
import dk.eboks.app.presentation.ui.debug.components.DebugUsersComponentContract
import dk.eboks.app.presentation.ui.debug.components.DebugUsersComponentPresenter
import dk.eboks.app.presentation.ui.debug.screens.user.DebugUserContract
import dk.eboks.app.presentation.ui.debug.screens.user.DebugUserPresenter
import dk.eboks.app.presentation.ui.folder.components.FoldersComponentContract
import dk.eboks.app.presentation.ui.folder.components.FoldersComponentPresenter
import dk.eboks.app.presentation.ui.folder.components.newfolder.NewFolderComponentContract
import dk.eboks.app.presentation.ui.folder.components.newfolder.NewFolderComponentPresenter
import dk.eboks.app.presentation.ui.folder.components.selectuser.FolderSelectUserComponentContract
import dk.eboks.app.presentation.ui.folder.components.selectuser.FolderSelectUserComponentPresenter
import dk.eboks.app.presentation.ui.folder.screens.FolderContract
import dk.eboks.app.presentation.ui.folder.screens.FolderPresenter
import dk.eboks.app.presentation.ui.home.components.channelcontrol.ChannelControlComponentContract
import dk.eboks.app.presentation.ui.home.components.channelcontrol.ChannelControlComponentPresenter
import dk.eboks.app.presentation.ui.home.components.folderpreview.FolderPreviewComponentContract
import dk.eboks.app.presentation.ui.home.components.folderpreview.FolderPreviewComponentPresenter
import dk.eboks.app.presentation.ui.login.components.ActivationCodeComponentContract
import dk.eboks.app.presentation.ui.login.components.ActivationCodeComponentPresenter
import dk.eboks.app.presentation.ui.login.components.DeviceActivationComponentContract
import dk.eboks.app.presentation.ui.login.components.DeviceActivationComponentPresenter
import dk.eboks.app.presentation.ui.login.components.ForgotPasswordComponentContract
import dk.eboks.app.presentation.ui.login.components.ForgotPasswordComponentPresenter
import dk.eboks.app.presentation.ui.login.components.ForgotPasswordDoneComponentContract
import dk.eboks.app.presentation.ui.login.components.ForgotPasswordDoneComponentPresenter
import dk.eboks.app.presentation.ui.login.components.LoginComponentContract
import dk.eboks.app.presentation.ui.login.components.LoginComponentPresenter
import dk.eboks.app.presentation.ui.login.components.UserCarouselComponentContract
import dk.eboks.app.presentation.ui.login.components.UserCarouselComponentPresenter
import dk.eboks.app.presentation.ui.login.components.verification.VerificationComponentContract
import dk.eboks.app.presentation.ui.login.components.verification.VerificationComponentPresenter
import dk.eboks.app.presentation.ui.login.screens.PopupLoginContract
import dk.eboks.app.presentation.ui.login.screens.PopupLoginPresenter
import dk.eboks.app.presentation.ui.mail.components.foldershortcuts.FolderShortcutsComponentContract
import dk.eboks.app.presentation.ui.mail.components.foldershortcuts.FolderShortcutsComponentPresenter
import dk.eboks.app.presentation.ui.mail.components.maillist.MailListComponentContract
import dk.eboks.app.presentation.ui.mail.components.maillist.MailListComponentPresenter
import dk.eboks.app.presentation.ui.mail.components.sendercarousel.SenderCarouselComponentContract
import dk.eboks.app.presentation.ui.mail.components.sendercarousel.SenderCarouselComponentPresenter
import dk.eboks.app.presentation.ui.mail.screens.list.MailListContract
import dk.eboks.app.presentation.ui.mail.screens.list.MailListPresenter
import dk.eboks.app.presentation.ui.mail.screens.overview.MailOverviewContract
import dk.eboks.app.presentation.ui.mail.screens.overview.MailOverviewPresenter
import dk.eboks.app.presentation.ui.message.components.detail.attachments.AttachmentsComponentContract
import dk.eboks.app.presentation.ui.message.components.detail.attachments.AttachmentsComponentPresenter
import dk.eboks.app.presentation.ui.message.components.detail.document.DocumentComponentContract
import dk.eboks.app.presentation.ui.message.components.detail.document.DocumentComponentPresenter
import dk.eboks.app.presentation.ui.message.components.detail.folderinfo.FolderInfoComponentContract
import dk.eboks.app.presentation.ui.message.components.detail.folderinfo.FolderInfoComponentPresenter
import dk.eboks.app.presentation.ui.message.components.detail.header.HeaderComponentContract
import dk.eboks.app.presentation.ui.message.components.detail.header.HeaderComponentPresenter
import dk.eboks.app.presentation.ui.message.components.detail.notes.NotesComponentContract
import dk.eboks.app.presentation.ui.message.components.detail.notes.NotesComponentPresenter
import dk.eboks.app.presentation.ui.message.components.detail.reply.ReplyButtonComponentContract
import dk.eboks.app.presentation.ui.message.components.detail.reply.ReplyButtonComponentPresenter
import dk.eboks.app.presentation.ui.message.components.detail.share.ShareComponentContract
import dk.eboks.app.presentation.ui.message.components.detail.share.ShareComponentPresenter
import dk.eboks.app.presentation.ui.message.components.detail.sign.SignButtonComponentContract
import dk.eboks.app.presentation.ui.message.components.detail.sign.SignButtonComponentPresenter
import dk.eboks.app.presentation.ui.message.components.opening.privatesender.PrivateSenderWarningComponentContract
import dk.eboks.app.presentation.ui.message.components.opening.privatesender.PrivateSenderWarningComponentPresenter
import dk.eboks.app.presentation.ui.message.components.opening.promulgation.PromulgationComponentContract
import dk.eboks.app.presentation.ui.message.components.opening.promulgation.PromulgationComponentPresenter
import dk.eboks.app.presentation.ui.message.components.opening.protectedmessage.ProtectedMessageComponentContract
import dk.eboks.app.presentation.ui.message.components.opening.protectedmessage.ProtectedMessageComponentPresenter
import dk.eboks.app.presentation.ui.message.components.opening.quarantine.QuarantineComponentContract
import dk.eboks.app.presentation.ui.message.components.opening.quarantine.QuarantineComponentPresenter
import dk.eboks.app.presentation.ui.message.components.opening.recalled.RecalledComponentContract
import dk.eboks.app.presentation.ui.message.components.opening.recalled.RecalledComponentPresenter
import dk.eboks.app.presentation.ui.message.components.opening.receipt.OpeningReceiptComponentContract
import dk.eboks.app.presentation.ui.message.components.opening.receipt.OpeningReceiptComponentPresenter
import dk.eboks.app.presentation.ui.message.components.viewers.html.HtmlViewComponentContract
import dk.eboks.app.presentation.ui.message.components.viewers.html.HtmlViewComponentPresenter
import dk.eboks.app.presentation.ui.message.components.viewers.image.ImageViewComponentContract
import dk.eboks.app.presentation.ui.message.components.viewers.image.ImageViewComponentPresenter
import dk.eboks.app.presentation.ui.message.components.viewers.pdf.PdfViewComponentContract
import dk.eboks.app.presentation.ui.message.components.viewers.pdf.PdfViewComponentPresenter
import dk.eboks.app.presentation.ui.message.components.viewers.text.TextViewComponentContract
import dk.eboks.app.presentation.ui.message.components.viewers.text.TextViewComponentPresenter
import dk.eboks.app.presentation.ui.message.screens.MessageContract
import dk.eboks.app.presentation.ui.message.screens.MessagePresenter
import dk.eboks.app.presentation.ui.message.screens.embedded.MessageEmbeddedContract
import dk.eboks.app.presentation.ui.message.screens.embedded.MessageEmbeddedPresenter
import dk.eboks.app.presentation.ui.message.screens.opening.MessageOpeningContract
import dk.eboks.app.presentation.ui.message.screens.opening.MessageOpeningPresenter
import dk.eboks.app.presentation.ui.message.screens.reply.ReplyFormContract
import dk.eboks.app.presentation.ui.message.screens.reply.ReplyFormPresenter
import dk.eboks.app.presentation.ui.message.screens.sign.SignContract
import dk.eboks.app.presentation.ui.message.screens.sign.SignPresenter
import dk.eboks.app.presentation.ui.navigation.components.NavBarComponentContract
import dk.eboks.app.presentation.ui.navigation.components.NavBarComponentPresenter
import dk.eboks.app.presentation.ui.overlay.screens.OverlayContract
import dk.eboks.app.presentation.ui.overlay.screens.OverlayPresenter
import dk.eboks.app.presentation.ui.profile.components.HelpContract
import dk.eboks.app.presentation.ui.profile.components.HelpPresenter
import dk.eboks.app.presentation.ui.profile.components.PrivacyContract
import dk.eboks.app.presentation.ui.profile.components.PrivacyPresenter
import dk.eboks.app.presentation.ui.profile.components.drawer.EmailVerificationComponentContract
import dk.eboks.app.presentation.ui.profile.components.drawer.EmailVerificationComponentPresenter
import dk.eboks.app.presentation.ui.profile.components.drawer.FingerHintComponentContract
import dk.eboks.app.presentation.ui.profile.components.drawer.FingerHintComponentPresenter
import dk.eboks.app.presentation.ui.profile.components.drawer.FingerPrintComponentContract
import dk.eboks.app.presentation.ui.profile.components.drawer.FingerPrintComponentPresenter
import dk.eboks.app.presentation.ui.profile.components.drawer.MergeAccountComponentContract
import dk.eboks.app.presentation.ui.profile.components.drawer.MergeAccountComponentPresenter
import dk.eboks.app.presentation.ui.profile.components.drawer.PhoneVerificationComponentContract
import dk.eboks.app.presentation.ui.profile.components.drawer.PhoneVerificationComponentPresenter
import dk.eboks.app.presentation.ui.profile.components.main.ProfileInfoComponentContract
import dk.eboks.app.presentation.ui.profile.components.main.ProfileInfoComponentPresenter
import dk.eboks.app.presentation.ui.profile.components.myinfo.MyInfoComponentContract
import dk.eboks.app.presentation.ui.profile.components.myinfo.MyInfoComponentPresenter
import dk.eboks.app.presentation.ui.profile.screens.ProfileContract
import dk.eboks.app.presentation.ui.profile.screens.ProfilePresenter
import dk.eboks.app.presentation.ui.profile.screens.myinfo.MyInfoContract
import dk.eboks.app.presentation.ui.profile.screens.myinfo.MyInfoPresenter
import dk.eboks.app.presentation.ui.senders.components.SenderGroupsComponentContract
import dk.eboks.app.presentation.ui.senders.components.SenderGroupsComponentPresenter
import dk.eboks.app.presentation.ui.senders.components.categories.CategoriesComponentContract
import dk.eboks.app.presentation.ui.senders.components.categories.CategoriesComponentPresenter
import dk.eboks.app.presentation.ui.senders.components.list.SenderAllListComponentContract
import dk.eboks.app.presentation.ui.senders.components.list.SenderAllListComponentPresenter
import dk.eboks.app.presentation.ui.senders.components.register.RegisterPresenter
import dk.eboks.app.presentation.ui.senders.components.register.RegistrationContract
import dk.eboks.app.presentation.ui.senders.screens.browse.BrowseCategoryContract
import dk.eboks.app.presentation.ui.senders.screens.browse.BrowseCategoryPresenter
import dk.eboks.app.presentation.ui.senders.screens.detail.SenderDetailContract
import dk.eboks.app.presentation.ui.senders.screens.detail.SenderDetailPresenter
import dk.eboks.app.presentation.ui.senders.screens.list.SenderAllListContract
import dk.eboks.app.presentation.ui.senders.screens.list.SenderAllListPresenter
import dk.eboks.app.presentation.ui.senders.screens.overview.SendersOverviewContract
import dk.eboks.app.presentation.ui.senders.screens.overview.SendersOverviewPresenter
import dk.eboks.app.presentation.ui.senders.screens.registrations.PendingContract
import dk.eboks.app.presentation.ui.senders.screens.registrations.PendingPresenter
import dk.eboks.app.presentation.ui.senders.screens.registrations.RegistrationsContract
import dk.eboks.app.presentation.ui.senders.screens.registrations.RegistrationsPresenter
import dk.eboks.app.presentation.ui.senders.screens.segment.SegmentDetailContract
import dk.eboks.app.presentation.ui.senders.screens.segment.SegmentDetailPresenter
import dk.eboks.app.presentation.ui.start.components.signup.SignupComponentContract
import dk.eboks.app.presentation.ui.start.components.signup.SignupComponentPresenter
import dk.eboks.app.presentation.ui.start.screens.StartContract
import dk.eboks.app.presentation.ui.start.screens.StartPresenter
import dk.eboks.app.presentation.ui.uploads.components.UploadOverviewComponentContract
import dk.eboks.app.presentation.ui.uploads.components.UploadOverviewComponentPresenter
import dk.eboks.app.presentation.ui.uploads.screens.UploadsContract
import dk.eboks.app.presentation.ui.uploads.screens.UploadsPresenter
import dk.eboks.app.presentation.ui.uploads.screens.fileupload.FileUploadContract
import dk.eboks.app.presentation.ui.uploads.screens.fileupload.FileUploadPresenter
import dk.nodes.arch.domain.injection.scopes.ActivityScope

@Module
class PresentationModule {
    @ActivityScope
    @Provides
    fun providePastaPresenter(presenter: PastaPresenter): PastaContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideMailOverviewPresenter(presenter: MailOverviewPresenter): MailOverviewContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideMailListPresenter(presenter: MailListPresenter): MailListContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideFolderPresenter(presenter: FolderPresenter): FolderContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideMessagePresenter(
        presenter: MessagePresenter
    ): MessageContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideChannelsPresenter(presenter: ChannelOverviewPresenter): ChannelOverviewContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideFileUploadPresenter(presenter: FileUploadPresenter): FileUploadContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideMessageSheetPresenter(presenter: MessageEmbeddedPresenter): MessageEmbeddedContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideMessageOpeningPresenter(presenter: MessageOpeningPresenter): MessageOpeningContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideHeaderComponentPresenter(presenter: HeaderComponentPresenter): HeaderComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideNotesComponentPresenter(presenter: NotesComponentPresenter): NotesComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideReplyButtonComponentPresenter(presenter: ReplyButtonComponentPresenter): ReplyButtonComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideSignButtonComponentPresenter(presenter: SignButtonComponentPresenter): SignButtonComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideSignPresenter(presenter: SignPresenter): SignContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideAttachmentsComponentPresenter(presenter: AttachmentsComponentPresenter): AttachmentsComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideFolderInfoComponentPresenter(presenter: FolderInfoComponentPresenter): FolderInfoComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideDocumentComponentPresenter(presenter: DocumentComponentPresenter): DocumentComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun providePdfPreviewComponentPresenter(presenter: PdfViewComponentPresenter): PdfViewComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideFoldersComponentPresenter(presenter: FoldersComponentPresenter): FoldersComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideFolderShortcutsComponentPresenter(presenter: FolderShortcutsComponentPresenter): FolderShortcutsComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideSenderCarouselComponentPresenter(presenter: SenderCarouselComponentPresenter): SenderCarouselComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideMailListComponentPresenter(presenter: MailListComponentPresenter): MailListComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideNavBarComponentPresenter(presenter: NavBarComponentPresenter): NavBarComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideHtmlViewComponentPresenter(presenter: HtmlViewComponentPresenter): HtmlViewComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideImageViewComponentPresenter(presenter: ImageViewComponentPresenter): ImageViewComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideTextViewComponentPresenter(presenter: TextViewComponentPresenter): TextViewComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideQuarantineComponentPresenter(presenter: QuarantineComponentPresenter): QuarantineComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideRecalledMessageComponentPresenter(presenter: RecalledComponentPresenter): RecalledComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun providePromulgationComponentPresenter(presenter: PromulgationComponentPresenter): PromulgationComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideProtectedMessageComponentPresenter(presenter: ProtectedMessageComponentPresenter): ProtectedMessageComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideOpeningReceiptComponentPresenter(presenter: OpeningReceiptComponentPresenter): OpeningReceiptComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun providePrivateSenderWarningComponentPresenter(presenter: PrivateSenderWarningComponentPresenter): PrivateSenderWarningComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideChannelListComponentPresenter(presenter: ChannelOverviewComponentPresenter): ChannelOverviewComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideChannelSettingsPopUpComponentPresenter(presenter: ChannelRequirementsComponentPresenter): ChannelRequirementsComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideShareComponentPresenter(presenter: ShareComponentPresenter): ShareComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideSendersOverviewPresenter(presenter: SendersOverviewPresenter): SendersOverviewContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideStartPresenter(presenter: StartPresenter): StartContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideSignupComponentPresenter(presenter: SignupComponentPresenter): SignupComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideVerificationComponentPresenter(presenter: VerificationComponentPresenter): VerificationComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideUserCarouselComponentPresenter(presenter: UserCarouselComponentPresenter): UserCarouselComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideLoginComponentPresenter(presenter: LoginComponentPresenter): LoginComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideForgotPasswordComponentPresenter(presenter: ForgotPasswordComponentPresenter): ForgotPasswordComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideActivationCodeComponentPresenter(presenter: ActivationCodeComponentPresenter): ActivationCodeComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideChannelOpeningComponentPresenter(presenter: ChannelOpeningComponentPresenter): ChannelOpeningComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideChannelVerificationComponentPresenter(presenter: ChannelVerificationComponentPresenter): ChannelVerificationComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideChannelContentComponentPresenter(presenter: ChannelContentComponentPresenter): ChannelContentComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideChannelContentStoreboxComponentPresenter(presenter: ChannelContentStoreboxComponentPresenter): ChannelContentStoreboxComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideChannelContentStoreboxDetailComponentPresenter(presenter: ChannelContentStoreboxDetailComponentPresenter): ChannelContentStoreboxDetailComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideChannelSettingsComponentPresenter(presenter: ChannelSettingsComponentPresenter): ChannelSettingsComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideChannelContentPresenter(presenter: ChannelContentPresenter): ChannelContentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideCategoriesComponentPresenter(presenter: CategoriesComponentPresenter): CategoriesComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideBrowseCategoryPresenter(
        presenter: BrowseCategoryPresenter
    ): BrowseCategoryContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideRegistrationsPresenter(presenter: RegistrationsPresenter): RegistrationsContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideSenderGroupsComponentPresenter(presenter: SenderGroupsComponentPresenter): SenderGroupsComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideUploadOverviewPresenter(presenter: UploadOverviewComponentPresenter): UploadOverviewComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideRegisterPresenter(presenter: RegisterPresenter): RegistrationContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun providePendingPresenter(presenter: PendingPresenter): PendingContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideSenderDetailPresenter(presenter: SenderDetailPresenter): SenderDetailContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideSegmentDetailPresenter(presenter: SegmentDetailPresenter): SegmentDetailContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideDebugOptionsComponentPresenter(presenter: DebugOptionsComponentPresenter): DebugOptionsComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideDebugUsersComponentPresenter(presenter: DebugUsersComponentPresenter): DebugUsersComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideProfilePresenter(presenter: ProfilePresenter): ProfileContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideProfileInfoComponentPresenter(presenter: ProfileInfoComponentPresenter): ProfileInfoComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideMyInfoComponentPresenter(presenter: MyInfoComponentPresenter): MyInfoComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideDebugUserPresenter(presenter: DebugUserPresenter): DebugUserContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideEmailVerificationComponentPresenter(presenter: EmailVerificationComponentPresenter): EmailVerificationComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun providePhoneVerificationComponentPresenter(presenter: PhoneVerificationComponentPresenter): PhoneVerificationComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideFingerHintComponentPresenter(presenter: FingerHintComponentPresenter): FingerHintComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideFingerPrintComponentPresenter(presenter: FingerPrintComponentPresenter): FingerPrintComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideMergeAccountComponentPresenter(presenter: MergeAccountComponentPresenter): MergeAccountComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideUploadsPresenter(presenter: UploadsPresenter): UploadsContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideForgotPasswordDoneComponentPresenter(presenter: ForgotPasswordDoneComponentPresenter): ForgotPasswordDoneComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideOverlayPresenter(presenter: OverlayPresenter): OverlayContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideConnectStoreboxPresenter(
        presenter: ConnectStoreboxPresenter
    ): ConnectStoreboxContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideMyInfoPresenter(presenter: MyInfoPresenter): MyInfoContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideReplyFormPresenter(
        presenter: ReplyFormPresenter
    ): ReplyFormContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideSenderAllListPresenter(presenter: SenderAllListPresenter): SenderAllListContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideSenderAllListComponentPresenter(
        presenter: SenderAllListComponentPresenter
    ): SenderAllListComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideNewFolderComponentPresenter(
        presenter: NewFolderComponentPresenter
    ): NewFolderComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideFolderSelectUserComponentPresenter(
        presenter: FolderSelectUserComponentPresenter
    ): FolderSelectUserComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideEkeyContentPresenter(
        presenter: EkeyContentPresenter
    ): EkeyContentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideEkeyComponentPresenter(presenter: EkeyComponentPresenter): EkeyComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideEkeyAddItemComponentPresenter(presenter: EkeyAddItemComponentPresenter): EkeyAddItemComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideEkeyDetailComponentPresenter(presenter: EkeyDetailComponentPresenter): EkeyDetailComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideFolderPreviewComponentPresenter(presenter: FolderPreviewComponentPresenter): FolderPreviewComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideChannelControlComponentPresenter(presenter: ChannelControlComponentPresenter): ChannelControlComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideEkeyOpenItemComponentPresenter(presenter: EkeyOpenItemComponentPresenter): EkeyOpenItemComponentContract.Presenter =
        presenter

    @ActivityScope
    @Provides
    fun provideEkeyPinComponentPresenter(presenter: EkeyPinComponentPresenter): EkeyPinComponentContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun providePrivacyPresenter(presenter: PrivacyPresenter): PrivacyContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideHelpPresenter(presenter: HelpPresenter): HelpContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun providePopupLoginPresenter(presenter: PopupLoginPresenter): PopupLoginContract.Presenter {
        return presenter
    }

    @ActivityScope
    @Provides
    fun provideDeviceActivationComponentPresenter(presenter: DeviceActivationComponentPresenter): DeviceActivationComponentContract.Presenter {
        return presenter
    }

    /* Pasta
    @ActivityScope
    @Provides
    fun provideComponentPresenter(stateManager: AppStateManager) : ComponentContract.Presenter {
        return ComponentPresenter(stateManager)
    }
    */
}