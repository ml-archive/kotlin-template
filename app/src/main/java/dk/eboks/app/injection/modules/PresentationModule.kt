package dk.eboks.app.injection.modules

import dagger.Binds
import dagger.Module
<<<<<<< HEAD
import dagger.Provides
import dk.eboks.app.domain.interactors.BootstrapInteractor
import dk.eboks.app.domain.interactors.GetCategoriesInteractor
import dk.eboks.app.domain.interactors.authentication.CheckRSAKeyPresenceInteractor
import dk.eboks.app.domain.interactors.authentication.LoginInteractor
import dk.eboks.app.domain.interactors.authentication.MergeAndImpersonateInteractor
import dk.eboks.app.domain.interactors.authentication.ResetPasswordInteractor
import dk.eboks.app.domain.interactors.authentication.SetCurrentUserInteractor
import dk.eboks.app.domain.interactors.authentication.TestLoginInteractor
import dk.eboks.app.domain.interactors.authentication.TransformTokenInteractor
import dk.eboks.app.domain.interactors.authentication.VerifyProfileInteractor
import dk.eboks.app.domain.interactors.channel.GetChannelContentLinkInteractor
import dk.eboks.app.domain.interactors.channel.GetChannelInteractor
import dk.eboks.app.domain.interactors.channel.GetChannelsInteractor
import dk.eboks.app.domain.interactors.channel.InstallChannelInteractor
import dk.eboks.app.domain.interactors.channel.UninstallChannelInteractor
import dk.eboks.app.domain.interactors.encryption.DecryptUserLoginInfoInteractor
import dk.eboks.app.domain.interactors.encryption.EncryptUserLoginInfoInteractor
import dk.eboks.app.domain.interactors.folder.CreateFolderInteractor
import dk.eboks.app.domain.interactors.folder.DeleteFolderInteractor
import dk.eboks.app.domain.interactors.folder.EditFolderInteractor
import dk.eboks.app.domain.interactors.folder.GetFoldersInteractor
import dk.eboks.app.domain.interactors.folder.OpenFolderInteractor
import dk.eboks.app.domain.interactors.message.GetLatestUploadsInteractor
import dk.eboks.app.domain.interactors.message.GetMessagesInteractor
import dk.eboks.app.domain.interactors.message.GetReplyFormInteractor
import dk.eboks.app.domain.interactors.message.GetSignLinkInteractor
import dk.eboks.app.domain.interactors.message.GetStorageInteractor
import dk.eboks.app.domain.interactors.message.OpenAttachmentInteractor
import dk.eboks.app.domain.interactors.message.OpenMessageInteractor
import dk.eboks.app.domain.interactors.message.SaveAttachmentInteractor
import dk.eboks.app.domain.interactors.message.SubmitReplyFormInteractor
import dk.eboks.app.domain.interactors.message.UploadFileInteractor
import dk.eboks.app.domain.interactors.message.messageoperations.DeleteMessagesInteractor
import dk.eboks.app.domain.interactors.message.messageoperations.MoveMessagesInteractor
import dk.eboks.app.domain.interactors.message.messageoperations.UpdateMessageInteractor
import dk.eboks.app.domain.interactors.message.payment.GetPaymentDetailsInteractor
import dk.eboks.app.domain.interactors.message.payment.GetPaymentLinkInteractor
import dk.eboks.app.domain.interactors.message.payment.TogglePaymentNotificationInteractor
import dk.eboks.app.domain.interactors.sender.GetSegmentInteractor
import dk.eboks.app.domain.interactors.sender.GetSenderCategoriesInteractor
import dk.eboks.app.domain.interactors.sender.GetSenderDetailInteractor
import dk.eboks.app.domain.interactors.sender.GetSendersInteractor
import dk.eboks.app.domain.interactors.sender.register.GetPendingInteractor
import dk.eboks.app.domain.interactors.sender.register.GetRegistrationsInteractor
import dk.eboks.app.domain.interactors.sender.register.RegisterInteractor
import dk.eboks.app.domain.interactors.sender.register.UnRegisterInteractor
import dk.eboks.app.domain.interactors.signup.CheckSignupMailInteractor
import dk.eboks.app.domain.interactors.storebox.ConfirmStoreboxInteractor
import dk.eboks.app.domain.interactors.storebox.CreateStoreboxInteractor
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxAccountLinkInteractor
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxCreditCardInteractor
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxReceiptInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxCardLinkInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxCreditCardsInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxProfileInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxReceiptInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxReceiptsInteractor
import dk.eboks.app.domain.interactors.storebox.LinkStoreboxInteractor
import dk.eboks.app.domain.interactors.storebox.PutStoreboxProfileInteractor
import dk.eboks.app.domain.interactors.storebox.SaveReceiptInteractor
import dk.eboks.app.domain.interactors.storebox.ShareReceiptInteractor
import dk.eboks.app.domain.interactors.storebox.UpdateStoreboxFlagsInteractor
import dk.eboks.app.domain.interactors.user.CheckSsnExistsInteractor
import dk.eboks.app.domain.interactors.user.ConfirmPhoneInteractor
import dk.eboks.app.domain.interactors.user.CreateDebugUserInteractorImpl
import dk.eboks.app.domain.interactors.user.CreateUserInteractor
import dk.eboks.app.domain.interactors.user.DeleteUserInteractor
import dk.eboks.app.domain.interactors.user.GetUserProfileInteractor
import dk.eboks.app.domain.interactors.user.GetUsersInteractor
import dk.eboks.app.domain.interactors.user.SaveUserInteractor
import dk.eboks.app.domain.interactors.user.SaveUserSettingsInteractor
import dk.eboks.app.domain.interactors.user.UpdateUserInteractor
import dk.eboks.app.domain.interactors.user.VerifyEmailInteractor
import dk.eboks.app.domain.interactors.user.VerifyPhoneInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.PrefManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.login.LoginState
=======
>>>>>>> development
import dk.eboks.app.pasta.activity.PastaContract
import dk.eboks.app.pasta.activity.PastaPresenter
import dk.eboks.app.presentation.ui.debug.components.DebugOptionsComponentContract
import dk.eboks.app.presentation.ui.debug.components.DebugOptionsComponentPresenter
import dk.eboks.app.presentation.ui.debug.components.DebugUsersComponentContract
import dk.eboks.app.presentation.ui.debug.components.DebugUsersComponentPresenter
import dk.eboks.app.presentation.ui.debug.screens.user.DebugUserContract
import dk.eboks.app.presentation.ui.debug.screens.user.DebugUserPresenter
import dk.eboks.app.presentation.ui.home.components.channelcontrol.ChannelControlComponentContract
import dk.eboks.app.presentation.ui.home.components.channelcontrol.ChannelControlComponentPresenter
import dk.eboks.app.presentation.ui.home.components.folderpreview.FolderPreviewComponentContract
import dk.eboks.app.presentation.ui.home.components.folderpreview.FolderPreviewComponentPresenter
<<<<<<< HEAD
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
import dk.eboks.app.presentation.ui.login.components.providers.WebLoginContract
import dk.eboks.app.presentation.ui.login.components.providers.WebLoginPresenter
import dk.eboks.app.presentation.ui.login.components.providers.bankidno.BankIdNOComponentPresenter
import dk.eboks.app.presentation.ui.login.components.providers.bankidse.BankIdSEComponentPresenter
import dk.eboks.app.presentation.ui.login.components.providers.idporten.IdPortenComponentPresenter
import dk.eboks.app.presentation.ui.login.components.providers.nemid.NemIdComponentPresenter
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
import dk.eboks.app.presentation.ui.message.components.detail.payment.PaymentButtonComponentContract
import dk.eboks.app.presentation.ui.message.components.detail.payment.PaymentButtonComponentPresenter
import dk.eboks.app.presentation.ui.message.components.detail.payment.PaymentComponentContract
import dk.eboks.app.presentation.ui.message.components.detail.payment.PaymentComponentPresenter
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
=======
import dk.eboks.app.presentation.ui.home.screens.HomeContract
import dk.eboks.app.presentation.ui.home.screens.HomePresenter
>>>>>>> development
import dk.eboks.app.presentation.ui.message.screens.opening.MessageOpeningContract
import dk.eboks.app.presentation.ui.message.screens.opening.MessageOpeningPresenter
import dk.eboks.app.presentation.ui.navigation.components.NavBarComponentContract
import dk.eboks.app.presentation.ui.navigation.components.NavBarComponentPresenter
import dk.eboks.app.presentation.ui.overlay.screens.OverlayContract
import dk.eboks.app.presentation.ui.overlay.screens.OverlayPresenter
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
abstract class PresentationModule {
    @ActivityScope
    @Binds
    abstract fun bindPastaPresenter(presenter: PastaPresenter): PastaContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindFileUploadPresenter(presenter: FileUploadPresenter): FileUploadContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindMessageOpeningPresenter(presenter: MessageOpeningPresenter): MessageOpeningContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindNavBarComponentPresenter(presenter: NavBarComponentPresenter): NavBarComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindStartPresenter(presenter: StartPresenter): StartContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindUploadOverviewPresenter(presenter: UploadOverviewComponentPresenter): UploadOverviewComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindDebugOptionsComponentPresenter(presenter: DebugOptionsComponentPresenter): DebugOptionsComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindDebugUsersComponentPresenter(presenter: DebugUsersComponentPresenter): DebugUsersComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindDebugUserPresenter(presenter: DebugUserPresenter): DebugUserContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindUploadsPresenter(presenter: UploadsPresenter): UploadsContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindOverlayPresenter(presenter: OverlayPresenter): OverlayContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindFolderPreviewComponentPresenter(presenter: FolderPreviewComponentPresenter): FolderPreviewComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindChannelControlComponentPresenter(presenter: ChannelControlComponentPresenter): ChannelControlComponentContract.Presenter

    @ActivityScope
<<<<<<< HEAD
    @Provides
    fun provideEkeyOpenItemComponentPresenter(presenter: EkeyOpenItemComponentPresenter): EkeyOpenItemComponentContract.Presenter =
        presenter

    @ActivityScope
    @Provides
    fun provideEkeyPinComponentPresenter(stateManager: AppStateManager): EkeyPinComponentContract.Presenter {
        return EkeyPinComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun providePrivacyPresenter(@Named("NAME_BASE_URL") baseUrlString: String): PrivacyContract.Presenter {
        return PrivacyPresenter(baseUrlString)
    }

    @ActivityScope
    @Provides
    fun provideHelpPresenter(@Named("NAME_BASE_URL") baseUrlString: String): HelpContract.Presenter {
        return HelpPresenter(baseUrlString)
    }

    @ActivityScope
    @Provides
    fun providePopupLoginPresenter(stateManager: AppStateManager): PopupLoginContract.Presenter {
        return PopupLoginPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideDeviceActivationComponentPresenter(
        presenter: DeviceActivationComponentPresenter
    ): DeviceActivationComponentContract.Presenter {
        return presenter
    }
  @ActivityScope
    @Provides
    fun providePaymentPresenter(stateManager: AppStateManager, getPaymentLinkInteractor: GetPaymentLinkInteractor, togglePaymentNotificationInteractor: TogglePaymentNotificationInteractor) : PaymentComponentContract.Presenter {
        return PaymentComponentPresenter(stateManager, getPaymentLinkInteractor, togglePaymentNotificationInteractor)
    }

    @ActivityScope
    @Provides
    fun providePaymentButtonComponentFragment(
            stateManager: AppStateManager,
            getPaymentDetailsInteractor: GetPaymentDetailsInteractor) : PaymentButtonComponentContract.Presenter {
        return PaymentButtonComponentPresenter(stateManager, getPaymentDetailsInteractor)
    }

=======
    @Binds
    abstract fun bindHomePresenter(presenter: HomePresenter): HomeContract.Presenter
>>>>>>> development
    /* Pasta
    @ActivityScope
    @Binds
    abstract fun bindComponentPresenter(stateManager: AppStateManager) : ComponentContract.Presenter {
        return ComponentPresenter(stateManager)
    }
    */
}