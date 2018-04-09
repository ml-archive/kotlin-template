package dk.eboks.app.injection.modules

import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.interactors.*
import dk.eboks.app.domain.interactors.channel.GetChannelInteractor
import dk.eboks.app.domain.interactors.channel.GetChannelsInteractor
import dk.eboks.app.domain.interactors.folder.GetFoldersInteractor
import dk.eboks.app.domain.interactors.folder.OpenFolderInteractor
import dk.eboks.app.domain.interactors.message.GetMessagesInteractor
import dk.eboks.app.domain.interactors.message.OpenAttachmentInteractor
import dk.eboks.app.domain.interactors.message.OpenMessageInteractor
import dk.eboks.app.domain.interactors.message.SaveAttachmentInteractor
import dk.eboks.app.domain.interactors.sender.*
import dk.eboks.app.domain.interactors.sender.register.GetPendingInteractor
import dk.eboks.app.domain.interactors.sender.register.GetRegistrationsInteractor
import dk.eboks.app.domain.interactors.sender.register.RegisterInteractor
import dk.eboks.app.domain.interactors.sender.register.UnRegisterInteractor
import dk.eboks.app.domain.interactors.user.CreateUserInteractor
import dk.eboks.app.domain.interactors.user.DeleteUserInteractor
import dk.eboks.app.domain.interactors.user.GetUsersInteractor
import dk.eboks.app.domain.interactors.user.SaveUserInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.presentation.ui.components.folder.folders.FoldersComponentContract
import dk.eboks.app.presentation.ui.components.folder.folders.FoldersComponentPresenter
import dk.eboks.app.presentation.ui.components.mail.foldershortcuts.FolderShortcutsComponentContract
import dk.eboks.app.presentation.ui.components.mail.foldershortcuts.FolderShortcutsComponentPresenter
import dk.eboks.app.presentation.ui.components.mail.maillist.MailListComponentContract
import dk.eboks.app.presentation.ui.components.mail.maillist.MailListComponentPresenter
import dk.eboks.app.presentation.ui.components.mail.sendercarousel.SenderCarouselComponentContract
import dk.eboks.app.presentation.ui.components.mail.sendercarousel.SenderCarouselComponentPresenter
import dk.eboks.app.presentation.ui.screens.mail.folder.FolderContract
import dk.eboks.app.presentation.ui.screens.mail.folder.FolderPresenter
import dk.eboks.app.presentation.ui.screens.mail.list.MailListContract
import dk.eboks.app.presentation.ui.screens.mail.list.MailListPresenter
import dk.eboks.app.presentation.ui.screens.mail.overview.MailOverviewContract
import dk.eboks.app.presentation.ui.screens.mail.overview.MailOverviewPresenter
import dk.eboks.app.pasta.activity.PastaContract
import dk.eboks.app.pasta.activity.PastaPresenter
import dk.eboks.app.presentation.ui.components.channels.content.ChannelContentComponentContract
import dk.eboks.app.presentation.ui.components.channels.content.ChannelContentComponentPresenter
import dk.eboks.app.presentation.ui.components.channels.content.ChannelContentStoreboxComponentContract
import dk.eboks.app.presentation.ui.components.channels.content.ChannelContentStoreboxComponentPresenter
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
import dk.eboks.app.presentation.ui.components.home.HomeComponentContract
import dk.eboks.app.presentation.ui.components.home.HomeComponentPresenter
import dk.eboks.app.presentation.ui.screens.message.MessageContract
import dk.eboks.app.presentation.ui.screens.message.MessagePresenter
import dk.eboks.app.presentation.ui.screens.message.embedded.MessageEmbeddedContract
import dk.eboks.app.presentation.ui.screens.message.embedded.MessageEmbeddedPresenter
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
import dk.eboks.app.presentation.ui.components.message.opening.privatesender.PrivateSenderWarningComponentContract
import dk.eboks.app.presentation.ui.components.message.opening.privatesender.PrivateSenderWarningComponentPresenter
import dk.eboks.app.presentation.ui.components.message.opening.protectedmessage.ProtectedMessageComponentContract
import dk.eboks.app.presentation.ui.components.message.opening.protectedmessage.ProtectedMessageComponentPresenter
import dk.eboks.app.presentation.ui.components.message.detail.share.ShareComponentContract
import dk.eboks.app.presentation.ui.components.message.detail.share.ShareComponentPresenter
import dk.eboks.app.presentation.ui.components.message.opening.promulgation.PromulgationComponentContract
import dk.eboks.app.presentation.ui.components.message.opening.promulgation.PromulgationComponentPresenter
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
import dk.eboks.app.presentation.ui.components.profile.drawer.*
import dk.eboks.app.presentation.ui.components.profile.main.ProfileInfoComponentContract
import dk.eboks.app.presentation.ui.components.profile.main.ProfileInfoComponentPresenter
import dk.eboks.app.presentation.ui.components.profile.myinfo.MyInfoComponentContract
import dk.eboks.app.presentation.ui.components.profile.myinfo.MyInfoComponentPresenter
import dk.eboks.app.presentation.ui.components.senders.SenderGroupsComponentContract
import dk.eboks.app.presentation.ui.components.senders.SenderGroupsComponentPresenter
import dk.eboks.app.presentation.ui.components.senders.categories.CategoriesComponentContract
import dk.eboks.app.presentation.ui.components.senders.categories.CategoriesComponentPresenter
import dk.eboks.app.presentation.ui.components.senders.register.RegisterPresenter
import dk.eboks.app.presentation.ui.components.senders.register.RegistrationContract
import dk.eboks.app.presentation.ui.components.start.login.*
import dk.eboks.app.presentation.ui.components.start.login.providers.bankidno.BankIdNOComponentContract
import dk.eboks.app.presentation.ui.components.start.login.providers.bankidno.BankIdNOComponentPresenter
import dk.eboks.app.presentation.ui.components.start.login.providers.bankidse.BankIdSEComponentContract
import dk.eboks.app.presentation.ui.components.start.login.providers.bankidse.BankIdSEComponentPresenter
import dk.eboks.app.presentation.ui.components.start.login.providers.idporten.IdPortenComponentContract
import dk.eboks.app.presentation.ui.components.start.login.providers.idporten.IdPortenComponentPresenter
import dk.eboks.app.presentation.ui.components.start.login.providers.nemid.NemIdComponentContract
import dk.eboks.app.presentation.ui.components.start.login.providers.nemid.NemIdComponentPresenter
import dk.eboks.app.presentation.ui.components.start.signup.AcceptTermsComponentContract
import dk.eboks.app.presentation.ui.components.start.signup.AcceptTermsComponentPresenter
import dk.eboks.app.presentation.ui.components.start.signup.SignupComponentContract
import dk.eboks.app.presentation.ui.components.start.signup.SignupComponentPresenter
import dk.eboks.app.presentation.ui.components.uploads.UploadOverviewComponentContract
import dk.eboks.app.presentation.ui.components.uploads.UploadOverviewComponentPresenter
import dk.eboks.app.presentation.ui.components.uploads.myuploads.MyUploadsComponentContract
import dk.eboks.app.presentation.ui.components.uploads.myuploads.MyUploadsComponentPresenter
import dk.eboks.app.presentation.ui.components.verification.VerificationComponentContract
import dk.eboks.app.presentation.ui.components.verification.VerificationComponentPresenter
import dk.eboks.app.presentation.ui.screens.channels.content.ChannelContentContract
import dk.eboks.app.presentation.ui.screens.channels.content.ChannelContentPresenter
import dk.eboks.app.presentation.ui.screens.channels.overview.ChannelOverviewContract
import dk.eboks.app.presentation.ui.screens.channels.overview.ChannelOverviewPresenter
import dk.eboks.app.presentation.ui.screens.debug.user.DebugUserContract
import dk.eboks.app.presentation.ui.screens.debug.user.DebugUserPresenter
import dk.eboks.app.presentation.ui.screens.home.HomeContract
import dk.eboks.app.presentation.ui.screens.home.HomePresenter
import dk.eboks.app.presentation.ui.screens.start.StartContract
import dk.eboks.app.presentation.ui.screens.start.StartPresenter
import dk.eboks.app.presentation.ui.screens.message.opening.MessageOpeningContract
import dk.eboks.app.presentation.ui.screens.message.opening.MessageOpeningPresenter
import dk.eboks.app.presentation.ui.screens.profile.ProfileContract
import dk.eboks.app.presentation.ui.screens.profile.ProfilePresenter
import dk.eboks.app.presentation.ui.screens.senders.browse.BrowseCategoryContract
import dk.eboks.app.presentation.ui.screens.senders.browse.BrowseCategoryPresenter
import dk.eboks.app.presentation.ui.screens.senders.detail.SenderDetailContract
import dk.eboks.app.presentation.ui.screens.senders.detail.SenderDetailPresenter
import dk.eboks.app.presentation.ui.screens.senders.overview.SendersOverviewContract
import dk.eboks.app.presentation.ui.screens.senders.overview.SendersOverviewPresenter
import dk.eboks.app.presentation.ui.screens.senders.registrations.PendingContract
import dk.eboks.app.presentation.ui.screens.senders.registrations.PendingPresenter
import dk.eboks.app.presentation.ui.screens.senders.registrations.RegistrationsPresenter
import dk.eboks.app.presentation.ui.screens.senders.registrations.RegistrationsContract
import dk.eboks.app.presentation.ui.screens.senders.segment.SegmentDetailContract
import dk.eboks.app.presentation.ui.screens.senders.segment.SegmentDetailPresenter
import dk.eboks.app.presentation.ui.screens.uploads.UploadsContract
import dk.eboks.app.presentation.ui.screens.uploads.UploadsPresenter
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.injection.scopes.ActivityScope
import dk.nodes.arch.domain.injection.scopes.AppScope
import javax.inject.Singleton

/**
 * Created by bison on 07/12/17.
 */

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
            executor: Executor
    ): MessageOpeningContract.Presenter {
        return MessageOpeningPresenter(stateManager, executor)
    }

    @ActivityScope
    @Provides
    fun provideHeaderComponentPresenter(stateManager: AppStateManager) : HeaderComponentContract.Presenter {
        return HeaderComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideNotesComponentPresenter(stateManager: AppStateManager) : NotesComponentContract.Presenter {
        return NotesComponentPresenter(stateManager)
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
    fun provideDocumentComponentPresenter(stateManager: AppStateManager, saveAttachmentInteractor: SaveAttachmentInteractor) : DocumentComponentContract.Presenter {
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
            openMessageInteractor: OpenMessageInteractor
    ): MailListComponentContract.Presenter {
        return MailListComponentPresenter(
                stateManager,
                getMessagesInteractor,
                openMessageInteractor
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
    fun provideQuarantineComponentPresenter(stateManager: AppStateManager, executor: Executor) : QuarantineComponentContract.Presenter {
        return QuarantineComponentPresenter(stateManager, executor)
    }

    @ActivityScope
    @Provides
    fun provideRecalledMessageComponentPresenter(stateManager: AppStateManager, executor: Executor) : RecalledComponentContract.Presenter {
        return RecalledComponentPresenter(stateManager, executor)
    }

    @ActivityScope
    @Provides
    fun providePromulgationComponentPresenter(stateManager: AppStateManager, executor: Executor) : PromulgationComponentContract.Presenter {
        return PromulgationComponentPresenter(stateManager, executor)
    }

    @ActivityScope
    @Provides
    fun provideProtectedMessageComponentPresenter(stateManager: AppStateManager, executor: Executor) : ProtectedMessageComponentContract.Presenter {
        return ProtectedMessageComponentPresenter(stateManager, executor)
    }

    @ActivityScope
    @Provides
    fun provideOpeningReceiptComponentPresenter(stateManager: AppStateManager, executor: Executor) : OpeningReceiptComponentContract.Presenter {
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
    fun provideSignupComponentPresenter(stateManager: AppStateManager, createUserInteractor: CreateUserInteractor): SignupComponentContract.Presenter {
        return SignupComponentPresenter(stateManager, createUserInteractor)
    }

    @ActivityScope
    @Provides
    fun provideNemIdComponentPresenter(stateManager: AppStateManager): NemIdComponentContract.Presenter {
        return NemIdComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideIdPortenComponentPresenter(stateManager: AppStateManager): IdPortenComponentContract.Presenter {
        return IdPortenComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideBankIdSEComponentPresenter(stateManager: AppStateManager): BankIdSEComponentContract.Presenter {
        return BankIdSEComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideBankIdNOComponentPresenter(stateManager: AppStateManager): BankIdNOComponentContract.Presenter {
        return BankIdNOComponentPresenter(stateManager)
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
            createUserInteractor: CreateUserInteractor
    ): LoginComponentContract.Presenter {
        return LoginComponentPresenter(stateManager, createUserInteractor)
    }

    @ActivityScope
    @Provides
    fun provideForgotPasswordComponentPresenter(stateManager: AppStateManager): ForgotPasswordComponentContract.Presenter {
        return ForgotPasswordComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideActivationCodeComponentPresenter(stateManager: AppStateManager): ActivationCodeComponentContract.Presenter {
        return ActivationCodeComponentPresenter(stateManager)
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
    fun provideChannelContentStoreboxComponentPresenter(stateManager: AppStateManager): ChannelContentStoreboxComponentContract.Presenter {
        return ChannelContentStoreboxComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideChannelSettingsComponentPresenter(stateManager: AppStateManager): ChannelSettingsComponentContract.Presenter {
        return ChannelSettingsComponentPresenter(stateManager)
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
    fun provideRegistrationsPresenter(stateManager: AppStateManager, registrationsInteractor: GetRegistrationsInteractor): RegistrationsContract.Presenter {
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
        return PendingPresenter(stateManager, getPendingInteractor, registerInteractor, unRegisterInteractor)
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
    fun provideSegmentDetailPresenter(stateManager: AppStateManager, getSegmentInteractor: GetSegmentInteractor, registerInteractor: RegisterInteractor, unRegisterInteractor: UnRegisterInteractor): SegmentDetailContract.Presenter {
        return SegmentDetailPresenter(stateManager, getSegmentInteractor, registerInteractor, unRegisterInteractor)
    }


    @ActivityScope
    @Provides
    fun provideHomePresenter(stateManager: AppStateManager): HomeContract.Presenter {
        return HomePresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideHomeComponentPresenter(stateManager: AppStateManager): HomeComponentContract.Presenter {
        return HomeComponentPresenter(stateManager)
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
    fun provideProfileInfoComponentPresenter(stateManager: AppStateManager): ProfileInfoComponentContract.Presenter {
        return ProfileInfoComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideMyInfoComponentPresenter(stateManager: AppStateManager, saveUserInteractor: SaveUserInteractor): MyInfoComponentContract.Presenter {
        return MyInfoComponentPresenter(stateManager, saveUserInteractor)
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
    fun provideEmailVerificationComponentPresenter(stateManager: AppStateManager) : EmailVerificationComponentContract.Presenter {
        return EmailVerificationComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun providePhoneVerificationComponentPresenter(stateManager: AppStateManager) : PhoneVerificationComponentContract.Presenter {
        return PhoneVerificationComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideFingerHintComponentPresenter(stateManager: AppStateManager) : FingerHintComponentContract.Presenter {
        return FingerHintComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideFingerPrintComponentPresenter(stateManager: AppStateManager) : FingerPrintComponentContract.Presenter {
        return FingerPrintComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideMergeAccountComponentPresenter(stateManager: AppStateManager) : MergeAccountComponentContract.Presenter {
        return MergeAccountComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideForgotPasswordDoneComponentPresenter(stateManager: AppStateManager) : ForgotPasswordDoneComponentContract.Presenter {
        return ForgotPasswordDoneComponentPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideUploadsPresenter(stateManager: AppStateManager): UploadsContract.Presenter {
        return UploadsPresenter(stateManager)
    }

    @ActivityScope
    @Provides
    fun provideMyUploadsComponentPresenter(stateManager: AppStateManager): MyUploadsComponentContract.Presenter {
        return MyUploadsComponentPresenter(stateManager)
    }

    /* Pasta
    @ActivityScope
    @Provides
    fun provideComponentPresenter(stateManager: AppStateManager) : ComponentContract.Presenter {
        return ComponentPresenter(stateManager)
    }
    */
}