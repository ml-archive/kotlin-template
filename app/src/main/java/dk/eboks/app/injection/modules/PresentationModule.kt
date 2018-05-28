package dk.eboks.app.injection.modules

import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.interactors.BootstrapInteractor
import dk.eboks.app.domain.interactors.GetCategoriesInteractor
import dk.eboks.app.domain.interactors.authentication.LoginInteractor
import dk.eboks.app.domain.interactors.authentication.TransformTokenInteractor
import dk.eboks.app.domain.interactors.channel.GetChannelHomeContentInteractor
import dk.eboks.app.domain.interactors.channel.GetChannelInteractor
import dk.eboks.app.domain.interactors.channel.GetChannelsInteractor
import dk.eboks.app.domain.interactors.encryption.EncryptUserLoginInfoInteractor
import dk.eboks.app.domain.interactors.folder.GetFoldersInteractor
import dk.eboks.app.domain.interactors.folder.OpenFolderInteractor
import dk.eboks.app.domain.interactors.message.*
import dk.eboks.app.domain.interactors.sender.*
import dk.eboks.app.domain.interactors.sender.register.GetPendingInteractor
import dk.eboks.app.domain.interactors.sender.register.GetRegistrationsInteractor
import dk.eboks.app.domain.interactors.sender.register.RegisterInteractor
import dk.eboks.app.domain.interactors.sender.register.UnRegisterInteractor
import dk.eboks.app.domain.interactors.storebox.*
import dk.eboks.app.domain.interactors.user.*
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.pasta.activity.PastaContract
import dk.eboks.app.pasta.activity.PastaPresenter
import dk.eboks.app.presentation.ui.components.channels.content.ChannelContentComponentContract
import dk.eboks.app.presentation.ui.components.channels.content.ChannelContentComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.content.ekey.EkeyComponentContract
import dk.eboks.app.presentation.ui.components.channels.content.ekey.EkeyComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.content.ekey.additem.EkeyAddItemComponentContract
import dk.eboks.app.presentation.ui.components.channels.content.ekey.additem.EkeyAddItemComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.content.ekey.detail.EkeyDetailComponentContract
import dk.eboks.app.presentation.ui.components.channels.content.ekey.detail.EkeyDetailComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.content.ekey.open.EkeyOpenItemComponentContract
import dk.eboks.app.presentation.ui.components.channels.content.ekey.open.EkeyOpenItemComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.content.ekey.pin.EkeyPinComponentContract
import dk.eboks.app.presentation.ui.components.channels.content.ekey.pin.EkeyPinComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.content.storebox.content.ChannelContentStoreboxComponentContract
import dk.eboks.app.presentation.ui.components.channels.content.storebox.content.ChannelContentStoreboxComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.content.storebox.detail.ChannelContentStoreboxDetailComponentContract
import dk.eboks.app.presentation.ui.components.channels.content.storebox.detail.ChannelContentStoreboxDetailComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.opening.ChannelOpeningComponentContract
import dk.eboks.app.presentation.ui.components.channels.opening.ChannelOpeningComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.overview.ChannelOverviewComponentContract
import dk.eboks.app.presentation.ui.components.channels.overview.ChannelOverviewComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.requirements.ChannelRequirementsComponentContract
import dk.eboks.app.presentation.ui.components.channels.requirements.ChannelRequirementsComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.settings.ChannelSettingsComponentContract
import dk.eboks.app.presentation.ui.components.channels.settings.ChannelSettingsComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.verification.ChannelVerificationComponentContract
import dk.eboks.app.presentation.ui.components.channels.verification.ChannelVerificationComponentPresenter
import dk.eboks.app.presentation.ui.components.debug.DebugOptionsComponentContract
import dk.eboks.app.presentation.ui.components.debug.DebugOptionsComponentPresenter
import dk.eboks.app.presentation.ui.components.folder.folders.FoldersComponentContract
import dk.eboks.app.presentation.ui.components.folder.folders.FoldersComponentPresenter
import dk.eboks.app.presentation.ui.components.folder.folders.newfolder.NewFolderComponentContract
import dk.eboks.app.presentation.ui.components.folder.folders.newfolder.NewFolderComponentPresenter
import dk.eboks.app.presentation.ui.components.folder.folders.selectuser.FolderSelectUserComponentContract
import dk.eboks.app.presentation.ui.components.folder.folders.selectuser.FolderSelectUserComponentPresenter
import dk.eboks.app.presentation.ui.components.home.channelcontrol.ChannelControlComponentContract
import dk.eboks.app.presentation.ui.components.home.channelcontrol.ChannelControlComponentPresenter
import dk.eboks.app.presentation.ui.components.home.folderpreview.FolderPreviewComponentContract
import dk.eboks.app.presentation.ui.components.home.folderpreview.FolderPreviewComponentPresenter
import dk.eboks.app.presentation.ui.components.mail.foldershortcuts.FolderShortcutsComponentContract
import dk.eboks.app.presentation.ui.components.mail.foldershortcuts.FolderShortcutsComponentPresenter
import dk.eboks.app.presentation.ui.components.mail.maillist.MailListComponentContract
import dk.eboks.app.presentation.ui.components.mail.maillist.MailListComponentPresenter
import dk.eboks.app.presentation.ui.components.mail.sendercarousel.SenderCarouselComponentContract
import dk.eboks.app.presentation.ui.components.mail.sendercarousel.SenderCarouselComponentPresenter
import dk.eboks.app.presentation.ui.components.message.detail.attachments.AttachmentsComponentContract
import dk.eboks.app.presentation.ui.components.message.detail.attachments.AttachmentsComponentPresenter
import dk.eboks.app.presentation.ui.components.message.detail.document.DocumentComponentContract
import dk.eboks.app.presentation.ui.components.message.detail.document.DocumentComponentPresenter
import dk.eboks.app.presentation.ui.components.message.detail.folderinfo.FolderInfoComponentContract
import dk.eboks.app.presentation.ui.components.message.detail.folderinfo.FolderInfoComponentPresenter
import dk.eboks.app.presentation.ui.components.message.detail.header.HeaderComponentContract
import dk.eboks.app.presentation.ui.components.message.detail.header.HeaderComponentPresenter
import dk.eboks.app.presentation.ui.components.message.detail.notes.NotesComponentContract
import dk.eboks.app.presentation.ui.components.message.detail.notes.NotesComponentPresenter
import dk.eboks.app.presentation.ui.components.message.detail.reply.ReplyButtonComponentContract
import dk.eboks.app.presentation.ui.components.message.detail.reply.ReplyButtonComponentPresenter
import dk.eboks.app.presentation.ui.components.message.detail.share.ShareComponentContract
import dk.eboks.app.presentation.ui.components.message.detail.share.ShareComponentPresenter
import dk.eboks.app.presentation.ui.components.message.opening.privatesender.PrivateSenderWarningComponentContract
import dk.eboks.app.presentation.ui.components.message.opening.privatesender.PrivateSenderWarningComponentPresenter
import dk.eboks.app.presentation.ui.components.message.opening.promulgation.PromulgationComponentContract
import dk.eboks.app.presentation.ui.components.message.opening.promulgation.PromulgationComponentPresenter
import dk.eboks.app.presentation.ui.components.message.opening.protectedmessage.ProtectedMessageComponentContract
import dk.eboks.app.presentation.ui.components.message.opening.protectedmessage.ProtectedMessageComponentPresenter
import dk.eboks.app.presentation.ui.components.message.opening.quarantine.QuarantineComponentContract
import dk.eboks.app.presentation.ui.components.message.opening.quarantine.QuarantineComponentPresenter
import dk.eboks.app.presentation.ui.components.message.opening.recalled.RecalledComponentContract
import dk.eboks.app.presentation.ui.components.message.opening.recalled.RecalledComponentPresenter
import dk.eboks.app.presentation.ui.components.message.opening.receipt.OpeningReceiptComponentContract
import dk.eboks.app.presentation.ui.components.message.opening.receipt.OpeningReceiptComponentPresenter
import dk.eboks.app.presentation.ui.components.message.viewers.html.HtmlViewComponentContract
import dk.eboks.app.presentation.ui.components.message.viewers.html.HtmlViewComponentPresenter
import dk.eboks.app.presentation.ui.components.message.viewers.image.ImageViewComponentContract
import dk.eboks.app.presentation.ui.components.message.viewers.image.ImageViewComponentPresenter
import dk.eboks.app.presentation.ui.components.message.viewers.pdf.PdfViewComponentContract
import dk.eboks.app.presentation.ui.components.message.viewers.pdf.PdfViewComponentPresenter
import dk.eboks.app.presentation.ui.components.message.viewers.text.TextViewComponentContract
import dk.eboks.app.presentation.ui.components.message.viewers.text.TextViewComponentPresenter
import dk.eboks.app.presentation.ui.components.navigation.NavBarComponentContract
import dk.eboks.app.presentation.ui.components.navigation.NavBarComponentPresenter
import dk.eboks.app.presentation.ui.components.profile.HelpContract
import dk.eboks.app.presentation.ui.components.profile.HelpPresenter
import dk.eboks.app.presentation.ui.components.profile.PrivacyContract
import dk.eboks.app.presentation.ui.components.profile.PrivacyPresenter
import dk.eboks.app.presentation.ui.components.profile.drawer.*
import dk.eboks.app.presentation.ui.components.profile.main.ProfileInfoComponentContract
import dk.eboks.app.presentation.ui.components.profile.main.ProfileInfoComponentPresenter
import dk.eboks.app.presentation.ui.components.profile.myinfo.MyInfoComponentContract
import dk.eboks.app.presentation.ui.components.profile.myinfo.MyInfoComponentPresenter
import dk.eboks.app.presentation.ui.components.senders.SenderGroupsComponentContract
import dk.eboks.app.presentation.ui.components.senders.SenderGroupsComponentPresenter
import dk.eboks.app.presentation.ui.components.senders.categories.CategoriesComponentContract
import dk.eboks.app.presentation.ui.components.senders.categories.CategoriesComponentPresenter
import dk.eboks.app.presentation.ui.components.senders.list.SenderAllListComponentContract
import dk.eboks.app.presentation.ui.components.senders.list.SenderAllListComponentPresenter
import dk.eboks.app.presentation.ui.components.senders.register.RegisterPresenter
import dk.eboks.app.presentation.ui.components.senders.register.RegistrationContract
import dk.eboks.app.presentation.ui.components.start.login.*
import dk.eboks.app.presentation.ui.components.start.login.providers.bankidno.BankIdNOComponentPresenter
import dk.eboks.app.presentation.ui.components.start.login.providers.bankidse.BankIdSEComponentPresenter
import dk.eboks.app.presentation.ui.components.start.login.providers.idporten.IdPortenComponentPresenter
import dk.eboks.app.presentation.ui.components.start.login.providers.WebLoginContract
import dk.eboks.app.presentation.ui.components.start.login.providers.WebLoginPresenter
import dk.eboks.app.presentation.ui.components.start.signup.AcceptTermsComponentContract
import dk.eboks.app.presentation.ui.components.start.signup.AcceptTermsComponentPresenter
import dk.eboks.app.presentation.ui.components.start.signup.SignupComponentContract
import dk.eboks.app.presentation.ui.components.start.signup.SignupComponentPresenter
import dk.eboks.app.presentation.ui.components.uploads.UploadOverviewComponentContract
import dk.eboks.app.presentation.ui.components.uploads.UploadOverviewComponentPresenter
import dk.eboks.app.presentation.ui.components.uploads.myuploads.MyUploadsComponentContract
import dk.eboks.app.presentation.ui.components.uploads.myuploads.MyUploadsComponentPresenter
import dk.eboks.app.presentation.ui.components.uploads.uploadfile.UploadFileComponentContract
import dk.eboks.app.presentation.ui.components.uploads.uploadfile.UploadFileComponentPresenter
import dk.eboks.app.presentation.ui.components.verification.VerificationComponentContract
import dk.eboks.app.presentation.ui.components.verification.VerificationComponentPresenter
import dk.eboks.app.presentation.ui.screens.channels.content.ChannelContentContract
import dk.eboks.app.presentation.ui.screens.channels.content.ChannelContentPresenter
import dk.eboks.app.presentation.ui.screens.channels.content.ekey.EkeyContentContract
import dk.eboks.app.presentation.ui.screens.channels.content.ekey.EkeyContentPresenter
import dk.eboks.app.presentation.ui.screens.channels.content.storebox.ConnectStoreboxContract
import dk.eboks.app.presentation.ui.screens.channels.content.storebox.ConnectStoreboxPresenter
import dk.eboks.app.presentation.ui.screens.channels.content.storebox.StoreboxContentContract
import dk.eboks.app.presentation.ui.screens.channels.content.storebox.StoreboxContentPresenter
import dk.eboks.app.presentation.ui.screens.channels.overview.ChannelOverviewContract
import dk.eboks.app.presentation.ui.screens.channels.overview.ChannelOverviewPresenter
import dk.eboks.app.presentation.ui.screens.debug.user.DebugUserContract
import dk.eboks.app.presentation.ui.screens.debug.user.DebugUserPresenter
import dk.eboks.app.presentation.ui.screens.home.HomeContract
import dk.eboks.app.presentation.ui.screens.home.HomePresenter
import dk.eboks.app.presentation.ui.screens.mail.folder.FolderContract
import dk.eboks.app.presentation.ui.screens.mail.folder.FolderPresenter
import dk.eboks.app.presentation.ui.screens.mail.list.MailListContract
import dk.eboks.app.presentation.ui.screens.mail.list.MailListPresenter
import dk.eboks.app.presentation.ui.screens.mail.overview.MailOverviewContract
import dk.eboks.app.presentation.ui.screens.mail.overview.MailOverviewPresenter
import dk.eboks.app.presentation.ui.screens.message.MessageContract
import dk.eboks.app.presentation.ui.screens.message.MessagePresenter
import dk.eboks.app.presentation.ui.screens.message.embedded.MessageEmbeddedContract
import dk.eboks.app.presentation.ui.screens.message.embedded.MessageEmbeddedPresenter
import dk.eboks.app.presentation.ui.screens.message.opening.MessageOpeningContract
import dk.eboks.app.presentation.ui.screens.message.opening.MessageOpeningPresenter
import dk.eboks.app.presentation.ui.screens.message.reply.ReplyFormContract
import dk.eboks.app.presentation.ui.screens.message.reply.ReplyFormPresenter
import dk.eboks.app.presentation.ui.screens.overlay.OverlayContract
import dk.eboks.app.presentation.ui.screens.overlay.OverlayPresenter
import dk.eboks.app.presentation.ui.screens.profile.ProfileContract
import dk.eboks.app.presentation.ui.screens.profile.ProfilePresenter
import dk.eboks.app.presentation.ui.screens.profile.myinfo.MyInfoContract
import dk.eboks.app.presentation.ui.screens.profile.myinfo.MyInfoPresenter
import dk.eboks.app.presentation.ui.screens.senders.browse.BrowseCategoryContract
import dk.eboks.app.presentation.ui.screens.senders.browse.BrowseCategoryPresenter
import dk.eboks.app.presentation.ui.screens.senders.detail.SenderDetailContract
import dk.eboks.app.presentation.ui.screens.senders.detail.SenderDetailPresenter
import dk.eboks.app.presentation.ui.screens.senders.list.SenderAllListContract
import dk.eboks.app.presentation.ui.screens.senders.list.SenderAllListPresenter
import dk.eboks.app.presentation.ui.screens.senders.overview.SendersOverviewContract
import dk.eboks.app.presentation.ui.screens.senders.overview.SendersOverviewPresenter
import dk.eboks.app.presentation.ui.screens.senders.registrations.PendingContract
import dk.eboks.app.presentation.ui.screens.senders.registrations.PendingPresenter
import dk.eboks.app.presentation.ui.screens.senders.registrations.RegistrationsContract
import dk.eboks.app.presentation.ui.screens.senders.registrations.RegistrationsPresenter
import dk.eboks.app.presentation.ui.screens.senders.segment.SegmentDetailContract
import dk.eboks.app.presentation.ui.screens.senders.segment.SegmentDetailPresenter
import dk.eboks.app.presentation.ui.screens.start.StartContract
import dk.eboks.app.presentation.ui.screens.start.StartPresenter
import dk.eboks.app.presentation.ui.screens.uploads.UploadsContract
import dk.eboks.app.presentation.ui.screens.uploads.UploadsPresenter
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.injection.scopes.ActivityScope
import javax.inject.Named

@Module
class PresentationModule {
    @ActivityScope
    @Provides
    fun providePastaPresenter(appState: AppStateManager): PastaContract.Presenter {
        return PastaPresenter(appState)
    }

    @ActivityScope
    @Provides
    fun provideMailOverviewPresenter(stateManager: AppStateManager): MailOverviewContract.Presenter {
        return MailOverviewPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideMailListPresenter(appState: AppStateManager): MailListContract.Presenter {
        return MailListPresenter(appState)
    }

    @ActivityScope
    @Provides
    fun provideFolderPresenter(appState: AppStateManager): FolderContract.Presenter {
        return FolderPresenter(appState)
    }

    @ActivityScope
    @Provides
    fun provideMessagePresenter(appState: AppStateManager): MessageContract.Presenter {
        return MessagePresenter(appState)
    }


    @ActivityScope
    @Provides
    fun provideChannelsPresenter(stateManager: AppStateManager): ChannelOverviewContract.Presenter {
        return ChannelOverviewPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideMessageSheetPresenter(stateManager: AppStateManager): MessageEmbeddedContract.Presenter {
        return MessageEmbeddedPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideMessageOpeningPresenter(
            stateManager: AppStateManager,
            executor: Executor,
            openMessageInteractor: OpenMessageInteractor
    ): MessageOpeningContract.Presenter {
        return MessageOpeningPresenter(stateManager, executor, openMessageInteractor)
    }

    @ActivityScope
    @Provides
    fun provideHeaderComponentPresenter(stateManager: AppStateManager): HeaderComponentContract.Presenter {
        return HeaderComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideNotesComponentPresenter(stateManager: AppStateManager, updateMessageInteractor: UpdateMessageInteractor): NotesComponentContract.Presenter {
        return NotesComponentPresenter(stateManager, updateMessageInteractor)
    }

    @ActivityScope
    @Provides
    fun provideReplyButtonComponentPresenter(stateManager: AppStateManager): ReplyButtonComponentContract.Presenter {
        return ReplyButtonComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideAttachmentsComponentPresenter(
            stateManager: AppStateManager,
            openAttachmentInteractor: OpenAttachmentInteractor,
            saveAttachmentInteractor: SaveAttachmentInteractor
    ): AttachmentsComponentContract.Presenter {
        return AttachmentsComponentPresenter(
                stateManager,
                openAttachmentInteractor,
                saveAttachmentInteractor
        )
    }

    @ActivityScope
    @Provides
    fun provideFolderInfoComponentPresenter(stateManager: AppStateManager): FolderInfoComponentContract.Presenter {
        return FolderInfoComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideDocumentComponentPresenter(
            stateManager: AppStateManager,
            saveAttachmentInteractor: SaveAttachmentInteractor
    ): DocumentComponentContract.Presenter {
        return DocumentComponentPresenter(stateManager, saveAttachmentInteractor)
    }

    @ActivityScope
    @Provides
    fun providePdfPreviewComponentPresenter(stateManager: AppStateManager): PdfViewComponentContract.Presenter {
        return PdfViewComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideFoldersComponentPresenter(
            stateManager: AppStateManager,
            getFoldersInteractor: GetFoldersInteractor,
            openFolderInteractor: OpenFolderInteractor
    ): FoldersComponentContract.Presenter {
        return FoldersComponentPresenter(stateManager, getFoldersInteractor, openFolderInteractor)
    }

    @ActivityScope
    @Provides
    fun provideFolderShortcutsComponentPresenter(
            stateManager: AppStateManager,
            getCategoriesInteractor: GetCategoriesInteractor,
            openFolderInteractor: OpenFolderInteractor
    ): FolderShortcutsComponentContract.Presenter {
        return FolderShortcutsComponentPresenter(
                stateManager,
                getCategoriesInteractor,
                openFolderInteractor
        )
    }

    @ActivityScope
    @Provides
    fun provideSenderCarouselComponentPresenter(
            stateManager: AppStateManager,
            sendersInteractor: GetSendersInteractor
    ): SenderCarouselComponentContract.Presenter {
        return SenderCarouselComponentPresenter(stateManager, sendersInteractor)
    }

    @ActivityScope
    @Provides
    fun provideMailListComponentPresenter(
            stateManager: AppStateManager,
            getMessagesInteractor: GetMessagesInteractor,
            deleteMessagesInteractor: DeleteMessagesInteractor,
            moveMessagesInteractor: MoveMessagesInteractor,
            updateMessageInteractor: UpdateMessageInteractor

    ): MailListComponentContract.Presenter {
        return MailListComponentPresenter(
                stateManager,
                getMessagesInteractor,
                deleteMessagesInteractor,
                moveMessagesInteractor,
                updateMessageInteractor
        )
    }

    @ActivityScope
    @Provides
    fun provideNavBarComponentPresenter(stateManager: AppStateManager): NavBarComponentContract.Presenter {
        return NavBarComponentPresenter(stateManager)
    }


    @ActivityScope
    @Provides
    fun provideHtmlViewComponentPresenter(stateManager: AppStateManager): HtmlViewComponentContract.Presenter {
        return HtmlViewComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideImageViewComponentPresenter(stateManager: AppStateManager): ImageViewComponentContract.Presenter {
        return ImageViewComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideTextViewComponentPresenter(stateManager: AppStateManager): TextViewComponentContract.Presenter {
        return TextViewComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideQuarantineComponentPresenter(
            stateManager: AppStateManager,
            executor: Executor
    ): QuarantineComponentContract.Presenter {
        return QuarantineComponentPresenter(stateManager, executor)
    }

    @ActivityScope
    @Provides
    fun provideRecalledMessageComponentPresenter(
            stateManager: AppStateManager,
            executor: Executor
    ): RecalledComponentContract.Presenter {
        return RecalledComponentPresenter(stateManager, executor)
    }

    @ActivityScope
    @Provides
    fun providePromulgationComponentPresenter(
            stateManager: AppStateManager,
            executor: Executor
    ): PromulgationComponentContract.Presenter {
        return PromulgationComponentPresenter(stateManager, executor)
    }

    @ActivityScope
    @Provides
    fun provideProtectedMessageComponentPresenter(
            stateManager: AppStateManager,
            executor: Executor
    ): ProtectedMessageComponentContract.Presenter {
        return ProtectedMessageComponentPresenter(stateManager, executor)
    }

    @ActivityScope
    @Provides
    fun provideOpeningReceiptComponentPresenter(
            stateManager: AppStateManager,
            executor: Executor
    ): OpeningReceiptComponentContract.Presenter {
        return OpeningReceiptComponentPresenter(stateManager, executor)
    }

    @ActivityScope
    @Provides
    fun providePrivateSenderWarningComponentPresenter(
            stateManager: AppStateManager,
            executor: Executor
    ): PrivateSenderWarningComponentContract.Presenter {
        return PrivateSenderWarningComponentPresenter(stateManager, executor)
    }

    @ActivityScope
    @Provides
    fun provideChannelListComponentPresenter(
            stateManager: AppStateManager,
            getChannelsInteractor: GetChannelsInteractor
    ): ChannelOverviewComponentContract.Presenter {
        return ChannelOverviewComponentPresenter(stateManager, getChannelsInteractor)
    }

    @ActivityScope
    @Provides
    fun provideChannelSettingsPopUpComponentPresenter(stateManager: AppStateManager): ChannelRequirementsComponentContract.Presenter {
        return ChannelRequirementsComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideShareComponentPresenter(stateManager: AppStateManager): ShareComponentContract.Presenter {
        return ShareComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideSendersOverviewPresenter(
            stateManager: AppStateManager,
            collectionsInteractor: GetCollectionsInteractor
    ): SendersOverviewContract.Presenter {
        return SendersOverviewPresenter(stateManager, collectionsInteractor)
    }

    @ActivityScope
    @Provides
    fun provideStartPresenter(
            stateManager: AppStateManager,
            bootstrapInteractor: BootstrapInteractor
    ): StartContract.Presenter {
        return StartPresenter(stateManager, bootstrapInteractor)
    }

    @ActivityScope
    @Provides
    fun provideSignupComponentPresenter(
            stateManager: AppStateManager,
            createUserInteractor: CreateUserInteractor
    ): SignupComponentContract.Presenter {
        return SignupComponentPresenter(stateManager, createUserInteractor)
    }

    @ActivityScope
    @Provides
    fun provideWebLoginPresenter(stateManager: AppStateManager, transformTokenInteractor: TransformTokenInteractor): WebLoginContract.Presenter {
        return WebLoginPresenter(stateManager, transformTokenInteractor)
    }

    @ActivityScope
    @Provides
    fun provideNemIdLoginPresenter(stateManager: AppStateManager, transformTokenInteractor: TransformTokenInteractor): WebLoginContract.Presenter {
        return provideNemIdLoginPresenter(stateManager, transformTokenInteractor)
    }

    @ActivityScope
    @Provides
    fun provideIdPortenComponentPresenter(stateManager: AppStateManager, transformTokenInteractor: TransformTokenInteractor): WebLoginContract.Presenter {
        return IdPortenComponentPresenter(stateManager, transformTokenInteractor)
    }

    @ActivityScope
    @Provides
    fun provideBankIdSEComponentPresenter(stateManager: AppStateManager, transformTokenInteractor: TransformTokenInteractor): WebLoginContract.Presenter {
        return BankIdSEComponentPresenter(stateManager, transformTokenInteractor)
    }

    @ActivityScope
    @Provides
    fun provideBankIdNOComponentPresenter(stateManager: AppStateManager, transformTokenInteractor: TransformTokenInteractor): WebLoginContract.Presenter {
        return BankIdNOComponentPresenter(stateManager, transformTokenInteractor)
    }

    @ActivityScope
    @Provides
    fun provideVerificationComponentPresenter(stateManager: AppStateManager): VerificationComponentContract.Presenter {
        return VerificationComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideUserCarouselComponentPresenter(
            stateManager: AppStateManager,
            getUsersInteractor: GetUsersInteractor,
            deleteUserInteractor: DeleteUserInteractor
    ): UserCarouselComponentContract.Presenter {
        return UserCarouselComponentPresenter(
                stateManager,
                getUsersInteractor,
                deleteUserInteractor
        )
    }

    @ActivityScope
    @Provides
    fun provideLoginComponentPresenter(
            stateManager: AppStateManager,
            createUserInteractor: CreateUserInteractor,
            loginInteractor: LoginInteractor
    ): LoginComponentContract.Presenter {
        return LoginComponentPresenter(
                stateManager,
                createUserInteractor,
                loginInteractor
        )
    }

    @ActivityScope
    @Provides
    fun provideForgotPasswordComponentPresenter(stateManager: AppStateManager): ForgotPasswordComponentContract.Presenter {
        return ForgotPasswordComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideActivationCodeComponentPresenter(stateManager: AppStateManager, loginInteractor: LoginInteractor): ActivationCodeComponentContract.Presenter {
        return ActivationCodeComponentPresenter(stateManager, loginInteractor)
    }


    @ActivityScope
    @Provides
    fun provideChannelOpeningComponentPresenter(
            stateManager: AppStateManager,
            getChannelInteractor: GetChannelInteractor
    ): ChannelOpeningComponentContract.Presenter {
        return ChannelOpeningComponentPresenter(stateManager, getChannelInteractor)
    }

    @ActivityScope
    @Provides
    fun provideChannelVerificationComponentPresenter(stateManager: AppStateManager): ChannelVerificationComponentContract.Presenter {
        return ChannelVerificationComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideChannelContentComponentPresenter(stateManager: AppStateManager): ChannelContentComponentContract.Presenter {
        return ChannelContentComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideChannelContentStoreboxComponentPresenter(
            stateManager: AppStateManager,
            getStoreboxReceiptsInteractor: GetStoreboxReceiptsInteractor
    ): ChannelContentStoreboxComponentContract.Presenter {
        return ChannelContentStoreboxComponentPresenter(
                stateManager,
                getStoreboxReceiptsInteractor
        )
    }

    @ActivityScope
    @Provides
    fun provideChannelContentStoreboxDetailComponentPresenter(
            stateManager: AppStateManager,
            getStoreboxReceiptInteractor: GetStoreboxReceiptInteractor
    ): ChannelContentStoreboxDetailComponentContract.Presenter {
        return ChannelContentStoreboxDetailComponentPresenter(
                stateManager,
                getStoreboxReceiptInteractor
        )
    }

    @ActivityScope
    @Provides
    fun provideChannelSettingsComponentPresenter(
            stateManager: AppStateManager,
            getStoreboxCreditCardsInteractor: GetStoreboxCreditCardsInteractor,
            deleteStoreboxCreditCardInteractor: DeleteStoreboxCreditCardInteractor
    ): ChannelSettingsComponentContract.Presenter {
        return ChannelSettingsComponentPresenter(stateManager,getStoreboxCreditCardsInteractor,deleteStoreboxCreditCardInteractor)
    }

    @ActivityScope
    @Provides
    fun provideChannelContentPresenter(stateManager: AppStateManager): ChannelContentContract.Presenter {
        return ChannelContentPresenter(stateManager)
    }


    @ActivityScope
    @Provides
    fun provideAcceptTermsComponentPresenter(stateManager: AppStateManager): AcceptTermsComponentContract.Presenter {
        return AcceptTermsComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideCategoriesComponentPresenter(
            stateManager: AppStateManager,
            getSenderCategoriesInteractor: GetSenderCategoriesInteractor
    ): CategoriesComponentContract.Presenter {
        return CategoriesComponentPresenter(stateManager, getSenderCategoriesInteractor)
    }


    @ActivityScope
    @Provides
    fun provideBrowseCategoryPresenter(
            stateManager: AppStateManager,
            getSendersInteractor: GetSendersInteractor
    ): BrowseCategoryContract.Presenter {
        return BrowseCategoryPresenter(stateManager, getSendersInteractor)
    }


    @ActivityScope
    @Provides
    fun provideRegistrationsPresenter(
            stateManager: AppStateManager,
            registrationsInteractor: GetRegistrationsInteractor
    ): RegistrationsContract.Presenter {
        return RegistrationsPresenter(stateManager, registrationsInteractor)
    }

    @ActivityScope
    @Provides
    fun provideSenderGroupsComponentPresenter(stateManager: AppStateManager): SenderGroupsComponentContract.Presenter {
        return SenderGroupsComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideUploadOverviewPresenter(stateManager: AppStateManager): UploadOverviewComponentContract.Presenter {
        return UploadOverviewComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideRegisterPresenter(
            stateManager: AppStateManager,
            registerInteractor: RegisterInteractor,
            unRegisterInteractor: UnRegisterInteractor
    ): RegistrationContract.Presenter {
        return RegisterPresenter(stateManager, registerInteractor, unRegisterInteractor)
    }

    @ActivityScope
    @Provides
    fun providePendingPresenter(
            stateManager: AppStateManager,
            getPendingInteractor: GetPendingInteractor,
            registerInteractor: RegisterInteractor,
            unRegisterInteractor: UnRegisterInteractor
    ): PendingContract.Presenter {
        return PendingPresenter(
                stateManager,
                getPendingInteractor,
                registerInteractor,
                unRegisterInteractor
        )
    }

    @ActivityScope
    @Provides
    fun provideSenderDetailPresenter(
            stateManager: AppStateManager,
            getSenderDetailInteractor: GetSenderDetailInteractor,
            registerInteractor: RegisterInteractor,
            unRegisterInteractor: UnRegisterInteractor
    ): SenderDetailContract.Presenter {
        return SenderDetailPresenter(
                stateManager,
                getSenderDetailInteractor,
                registerInteractor,
                unRegisterInteractor
        )
    }

    @ActivityScope
    @Provides
    fun provideSegmentDetailPresenter(
            stateManager: AppStateManager,
            getSegmentInteractor: GetSegmentInteractor,
            registerInteractor: RegisterInteractor,
            unRegisterInteractor: UnRegisterInteractor
    ): SegmentDetailContract.Presenter {
        return SegmentDetailPresenter(
                stateManager,
                getSegmentInteractor,
                registerInteractor,
                unRegisterInteractor
        )
    }


    @ActivityScope
    @Provides
    fun provideHomePresenter(stateManager: AppStateManager): HomeContract.Presenter {
        return HomePresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideDebugOptionsComponentPresenter(stateManager: AppStateManager): DebugOptionsComponentContract.Presenter {
        return DebugOptionsComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideProfilePresenter(stateManager: AppStateManager): ProfileContract.Presenter {
        return ProfilePresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideProfileInfoComponentPresenter(
            stateManager: AppStateManager,
            saveUserInteractor: SaveUserInteractor
    ): ProfileInfoComponentContract.Presenter {
        return ProfileInfoComponentPresenter(stateManager, saveUserInteractor)
    }

    @ActivityScope
    @Provides
    fun provideMyInfoComponentPresenter(
            stateManager: AppStateManager,
            saveUserInteractor: SaveUserInteractor,
            updateUserInteractor: UpdateUserInteractor
    ): MyInfoComponentContract.Presenter {
        return MyInfoComponentPresenter(stateManager, saveUserInteractor, updateUserInteractor )
    }

    @ActivityScope
    @Provides
    fun provideDebugUserPresenter(
            stateManager: AppStateManager,
            createUserInteractor: CreateUserInteractor,
            saveUserInteractor: SaveUserInteractor
    ): DebugUserContract.Presenter {
        return DebugUserPresenter(stateManager, createUserInteractor, saveUserInteractor)
    }

    @ActivityScope
    @Provides
    fun provideEmailVerificationComponentPresenter(stateManager: AppStateManager, verifyEmailInteractor: VerifyEmailInteractor): EmailVerificationComponentContract.Presenter {
        return EmailVerificationComponentPresenter(stateManager, verifyEmailInteractor)
    }

    @ActivityScope
    @Provides
    fun providePhoneVerificationComponentPresenter(stateManager: AppStateManager): PhoneVerificationComponentContract.Presenter {
        return PhoneVerificationComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideFingerHintComponentPresenter(
            stateManager: AppStateManager,
            encryptUserLoginInfoInteractor: EncryptUserLoginInfoInteractor,
            saveUserInteractor: SaveUserInteractor
    ): FingerHintComponentContract.Presenter {
        return FingerHintComponentPresenter(
                stateManager,
                encryptUserLoginInfoInteractor,
                saveUserInteractor
        )
    }

    @ActivityScope
    @Provides
    fun provideFingerPrintComponentPresenter(stateManager: AppStateManager): FingerPrintComponentContract.Presenter {
        return FingerPrintComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideMergeAccountComponentPresenter(stateManager: AppStateManager): MergeAccountComponentContract.Presenter {
        return MergeAccountComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideUploadsPresenter(stateManager: AppStateManager): UploadsContract.Presenter {
        return UploadsPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideMyUploadsComponentPresenter(
            stateManager: AppStateManager,
            getMessagesInteractor: GetMessagesInteractor,
            openMessageInteractor: OpenMessageInteractor
    ): MyUploadsComponentContract.Presenter {
        return MyUploadsComponentPresenter(
                stateManager,
                getMessagesInteractor,
                openMessageInteractor
        )
    }

    @ActivityScope
    @Provides
    fun provideForgotPasswordDoneComponentPresenter(stateManager: AppStateManager): ForgotPasswordDoneComponentContract.Presenter {
        return ForgotPasswordDoneComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideOverlayPresenter(stateManager: AppStateManager): OverlayContract.Presenter {
        return OverlayPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideUploadFilePresenter(stateManager: AppStateManager): UploadFileComponentContract.Presenter {
        return UploadFileComponentPresenter(stateManager)
    }


    @ActivityScope
    @Provides
    fun provideStoreboxContentPresenter(stateManager: AppStateManager): StoreboxContentContract.Presenter {
        return StoreboxContentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideConnectStoreboxPresenter(
            stateManager: AppStateManager,
            linkStoreboxInteractor: LinkStoreboxInteractor,
            confirmStoreboxInteractor: ConfirmStoreboxInteractor
    ): ConnectStoreboxContract.Presenter {
        return ConnectStoreboxPresenter(
                stateManager,
                linkStoreboxInteractor,
                confirmStoreboxInteractor
        )
    }

    @ActivityScope
    @Provides
    fun provideMyInfoPresenter(stateManager: AppStateManager): MyInfoContract.Presenter {
        return MyInfoPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideReplyFormPresenter(
            appState: AppStateManager,
            getReplyFormInteractor: GetReplyFormInteractor,
            submitReplyFormInteractor: SubmitReplyFormInteractor
    ): ReplyFormContract.Presenter {
        return ReplyFormPresenter(appState, getReplyFormInteractor, submitReplyFormInteractor)
    }

    @ActivityScope
    @Provides
    fun provideSenderAllListPresenter(appState: AppStateManager): SenderAllListContract.Presenter {
        return SenderAllListPresenter(appState)
    }

    @ActivityScope
    @Provides
    fun provideSenderAllListComponentPresenter(
            stateManager: AppStateManager,
            sendersInteractor: GetSendersInteractor
    ): SenderAllListComponentContract.Presenter {
        return SenderAllListComponentPresenter(stateManager, sendersInteractor)
    }

    @ActivityScope
    @Provides
    fun provideNewFolderComponentPresenter(stateManager: AppStateManager): NewFolderComponentContract.Presenter {
        return NewFolderComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideFolderSelectUserComponentPresenter(stateManager: AppStateManager): FolderSelectUserComponentContract.Presenter {
        return FolderSelectUserComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideEkeyContentPresenter(stateManager: AppStateManager): EkeyContentContract.Presenter {
        return EkeyContentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideEkeyComponentPresenter(stateManager: AppStateManager): EkeyComponentContract.Presenter {
        return EkeyComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideEkeyAddItemComponentPresenter(stateManager: AppStateManager): EkeyAddItemComponentContract.Presenter {
        return EkeyAddItemComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideEkeyDetailComponentPresenter(stateManager: AppStateManager): EkeyDetailComponentContract.Presenter {
        return EkeyDetailComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideFolderPreviewComponentPresenter(
            stateManager: AppStateManager,
            getMessagesInteractor: GetMessagesInteractor
    ): FolderPreviewComponentContract.Presenter {
        return FolderPreviewComponentPresenter(stateManager, getMessagesInteractor)
    }

    @ActivityScope
    @Provides
    fun provideChannelControlComponentPresenter(
            stateManager: AppStateManager,
            getChannelHomeContentInteractor: GetChannelHomeContentInteractor
    ): ChannelControlComponentContract.Presenter {
        return ChannelControlComponentPresenter(stateManager, getChannelHomeContentInteractor)
    }


    @ActivityScope
    @Provides
    fun provideEkeyOpenItemComponentPresenter(stateManager: AppStateManager): EkeyOpenItemComponentContract.Presenter {
        return EkeyOpenItemComponentPresenter(stateManager)
    }

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

    /* Pasta
    @ActivityScope
    @Provides
    fun provideComponentPresenter(stateManager: AppStateManager) : ComponentContract.Presenter {
        return ComponentPresenter(stateManager)
    }
    */
}