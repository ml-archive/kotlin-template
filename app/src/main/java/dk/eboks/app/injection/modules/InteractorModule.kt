package dk.eboks.app.injection.modules

import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.interactors.BootstrapInteractor
import dk.eboks.app.domain.interactors.BootstrapInteractorImpl
import dk.eboks.app.domain.interactors.GetCategoriesInteractor
import dk.eboks.app.domain.interactors.GetMailCategoriesInteractorImpl
import dk.eboks.app.domain.interactors.authentication.CheckRSAKeyPresenceInteractor
import dk.eboks.app.domain.interactors.authentication.CheckRSAKeyPresenceInteractorImpl
import dk.eboks.app.domain.interactors.authentication.LoginInteractor
import dk.eboks.app.domain.interactors.authentication.LoginInteractorImpl
import dk.eboks.app.domain.interactors.authentication.MergeAndImpersonateInteractor
import dk.eboks.app.domain.interactors.authentication.MergeAndImpersonateInteractorImpl
import dk.eboks.app.domain.interactors.authentication.ResetPasswordInteractor
import dk.eboks.app.domain.interactors.authentication.ResetPasswordInteractorImpl
import dk.eboks.app.domain.interactors.authentication.SetCurrentUserInteractor
import dk.eboks.app.domain.interactors.authentication.SetCurrentUserInteractorImpl
import dk.eboks.app.domain.interactors.authentication.TestLoginInteractor
import dk.eboks.app.domain.interactors.authentication.TestLoginInteractorImpl
import dk.eboks.app.domain.interactors.authentication.TransformTokenInteractor
import dk.eboks.app.domain.interactors.authentication.TransformTokenInteractorImpl
import dk.eboks.app.domain.interactors.authentication.VerifyProfileInteractor
import dk.eboks.app.domain.interactors.authentication.VerifyProfileInteractorImpl
import dk.eboks.app.domain.interactors.authentication.mobileacces.ActivateDeviceInteractor
import dk.eboks.app.domain.interactors.authentication.mobileacces.ActivateDeviceInteractorImpl
import dk.eboks.app.domain.interactors.authentication.mobileacces.DeleteRSAKeyForUserInteractor
import dk.eboks.app.domain.interactors.authentication.mobileacces.DeleteRSAKeyForUserInteractorImpl
import dk.eboks.app.domain.interactors.authentication.mobileacces.DeleteRSAKeyInteractor
import dk.eboks.app.domain.interactors.authentication.mobileacces.DeleteRSAKeyInteractorImpl
import dk.eboks.app.domain.interactors.authentication.mobileacces.GenerateRSAKeyInteractor
import dk.eboks.app.domain.interactors.authentication.mobileacces.GenerateRSAKeyInteractorImpl
import dk.eboks.app.domain.interactors.channel.GetChannelContentLinkInteractor
import dk.eboks.app.domain.interactors.channel.GetChannelContentLinkInteractorImpl
import dk.eboks.app.domain.interactors.channel.GetChannelHomeContentInteractor
import dk.eboks.app.domain.interactors.channel.GetChannelHomeContentInteractorImpl
import dk.eboks.app.domain.interactors.channel.GetChannelInteractor
import dk.eboks.app.domain.interactors.channel.GetChannelInteractorImpl
import dk.eboks.app.domain.interactors.channel.GetChannelsInteractor
import dk.eboks.app.domain.interactors.channel.GetChannelsInteractorImpl
import dk.eboks.app.domain.interactors.channel.InstallChannelInteractor
import dk.eboks.app.domain.interactors.channel.InstallChannelInteractorImpl
import dk.eboks.app.domain.interactors.channel.UninstallChannelInteractor
import dk.eboks.app.domain.interactors.channel.UninstallChannelInteractorImpl
import dk.eboks.app.domain.interactors.ekey.DeleteEKeyMasterkeyInteractor
import dk.eboks.app.domain.interactors.ekey.DeleteEKeyMasterkeyInteractorImpl
import dk.eboks.app.domain.interactors.ekey.DeleteEKeyVaultInteractor
import dk.eboks.app.domain.interactors.ekey.DeleteEKeyVaultInteractorImpl
import dk.eboks.app.domain.interactors.ekey.GetEKeyMasterkeyInteractor
import dk.eboks.app.domain.interactors.ekey.GetEKeyMasterkeyInteractorImpl
import dk.eboks.app.domain.interactors.ekey.GetEKeyVaultInteractor
import dk.eboks.app.domain.interactors.ekey.GetEKeyVaultInteractorImpl
import dk.eboks.app.domain.interactors.ekey.SetEKeyMasterkeyInteractor
import dk.eboks.app.domain.interactors.ekey.SetEKeyMasterkeyInteractorImpl
import dk.eboks.app.domain.interactors.ekey.SetEKeyVaultInteractor
import dk.eboks.app.domain.interactors.ekey.SetEKeyVaultInteractorImpl
import dk.eboks.app.domain.interactors.encryption.DecryptUserLoginInfoInteractor
import dk.eboks.app.domain.interactors.encryption.DecryptUserLoginInfoInteractorImpl
import dk.eboks.app.domain.interactors.encryption.EncryptUserLoginInfoInteractor
import dk.eboks.app.domain.interactors.encryption.EncryptUserLoginInfoInteractorImpl
import dk.eboks.app.domain.interactors.folder.CreateFolderInteractor
import dk.eboks.app.domain.interactors.folder.CreateFolderInteractorImpl
import dk.eboks.app.domain.interactors.folder.DeleteFolderInteractor
import dk.eboks.app.domain.interactors.folder.DeleteFolderInteractorImpl
import dk.eboks.app.domain.interactors.folder.EditFolderInteractor
import dk.eboks.app.domain.interactors.folder.EditFolderInteractorImpl
import dk.eboks.app.domain.interactors.folder.GetFoldersInteractor
import dk.eboks.app.domain.interactors.folder.GetFoldersInteractorImpl
import dk.eboks.app.domain.interactors.folder.OpenFolderInteractor
import dk.eboks.app.domain.interactors.folder.OpenFolderInteractorImpl
import dk.eboks.app.domain.interactors.message.GetLatestUploadsInteractor
import dk.eboks.app.domain.interactors.message.GetLatestUploadsInteractorImpl
import dk.eboks.app.domain.interactors.message.GetMessagesInteractor
import dk.eboks.app.domain.interactors.message.GetMessagesInteractorImpl
import dk.eboks.app.domain.interactors.message.GetReplyFormInteractor
import dk.eboks.app.domain.interactors.message.GetReplyFormInteractorImpl
import dk.eboks.app.domain.interactors.message.GetSignLinkInteractor
import dk.eboks.app.domain.interactors.message.GetSignLinkInteractorImpl
import dk.eboks.app.domain.interactors.message.GetStorageInteractor
import dk.eboks.app.domain.interactors.message.GetStorageInteractorImpl
import dk.eboks.app.domain.interactors.message.OpenAttachmentInteractor
import dk.eboks.app.domain.interactors.message.OpenAttachmentInteractorImpl
import dk.eboks.app.domain.interactors.message.OpenMessageInteractor
import dk.eboks.app.domain.interactors.message.OpenMessageInteractorImpl
import dk.eboks.app.domain.interactors.message.SaveAttachmentInteractor
import dk.eboks.app.domain.interactors.message.SaveAttachmentInteractorImpl
import dk.eboks.app.domain.interactors.message.SubmitReplyFormInteractor
import dk.eboks.app.domain.interactors.message.SubmitReplyFormInteractorImpl
import dk.eboks.app.domain.interactors.message.UploadFileInteractor
import dk.eboks.app.domain.interactors.message.UploadFileInteractorImpl
import dk.eboks.app.domain.interactors.message.messageoperations.DeleteMessagesInteractor
import dk.eboks.app.domain.interactors.message.messageoperations.DeleteMessagesInteractorImpl
import dk.eboks.app.domain.interactors.message.messageoperations.MoveMessagesInteractor
import dk.eboks.app.domain.interactors.message.messageoperations.MoveMessagesInteractorImpl
import dk.eboks.app.domain.interactors.message.messageoperations.UpdateMessageInteractor
import dk.eboks.app.domain.interactors.message.messageoperations.UpdateMessageInteractorImpl
import dk.eboks.app.domain.interactors.sender.GetCollectionsInteractor
import dk.eboks.app.domain.interactors.sender.GetCollectionsInteractorImpl
import dk.eboks.app.domain.interactors.sender.GetSegmentInteractor
import dk.eboks.app.domain.interactors.sender.GetSegmentInteractorImpl
import dk.eboks.app.domain.interactors.sender.GetSenderCategoriesInteractor
import dk.eboks.app.domain.interactors.sender.GetSenderCategoriesInteractorImpl
import dk.eboks.app.domain.interactors.sender.GetSenderDetailInteractor
import dk.eboks.app.domain.interactors.sender.GetSenderDetailInteractorImpl
import dk.eboks.app.domain.interactors.sender.GetSendersInteractor
import dk.eboks.app.domain.interactors.sender.GetSendersInteractorImpl
import dk.eboks.app.domain.interactors.sender.register.GetPendingInteractor
import dk.eboks.app.domain.interactors.sender.register.GetPendingInteractorImpl
import dk.eboks.app.domain.interactors.sender.register.GetRegistrationsInteractor
import dk.eboks.app.domain.interactors.sender.register.GetRegistrationsInteractorImpl
import dk.eboks.app.domain.interactors.sender.register.RegisterInteractor
import dk.eboks.app.domain.interactors.sender.register.RegisterInteractorImpl
import dk.eboks.app.domain.interactors.sender.register.UnRegisterInteractor
import dk.eboks.app.domain.interactors.sender.register.UnRegisterInteractorImpl
import dk.eboks.app.domain.interactors.shares.GetAllSharesInteractor
import dk.eboks.app.domain.interactors.shares.GetAllSharesInteractorImpl
import dk.eboks.app.domain.interactors.signup.CheckSignupMailInteractor
import dk.eboks.app.domain.interactors.signup.CheckSignupMailInteractorImpl
import dk.eboks.app.domain.interactors.storebox.ConfirmStoreboxInteractor
import dk.eboks.app.domain.interactors.storebox.ConfirmStoreboxInteractorImpl
import dk.eboks.app.domain.interactors.storebox.CreateStoreboxInteractor
import dk.eboks.app.domain.interactors.storebox.CreateStoreboxInteractorImpl
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxAccountLinkInteractor
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxAccountLinkInteractorImpl
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxCreditCardInteractor
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxCreditCardInteractorImpl
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxReceiptInteractor
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxReceiptInteractorImpl
import dk.eboks.app.domain.interactors.storebox.GetStoreboxCardLinkInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxCardLinkInteractorImpl
import dk.eboks.app.domain.interactors.storebox.GetStoreboxCreditCardsInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxCreditCardsInteractorImpl
import dk.eboks.app.domain.interactors.storebox.GetStoreboxProfileInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxProfileInteractorImpl
import dk.eboks.app.domain.interactors.storebox.GetStoreboxReceiptInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxReceiptInteractorImpl
import dk.eboks.app.domain.interactors.storebox.GetStoreboxReceiptsInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxReceiptsInteractorImpl
import dk.eboks.app.domain.interactors.storebox.LinkStoreboxInteractor
import dk.eboks.app.domain.interactors.storebox.LinkStoreboxInteractorImpl
import dk.eboks.app.domain.interactors.storebox.PutStoreboxProfileInteractor
import dk.eboks.app.domain.interactors.storebox.PutStoreboxProfileInteractorImpl
import dk.eboks.app.domain.interactors.storebox.SaveReceiptInteractor
import dk.eboks.app.domain.interactors.storebox.SaveReceiptInteractorImpl
import dk.eboks.app.domain.interactors.storebox.ShareReceiptInteractor
import dk.eboks.app.domain.interactors.storebox.ShareReceiptInteractorImpl
import dk.eboks.app.domain.interactors.storebox.UpdateStoreboxFlagsInteractor
import dk.eboks.app.domain.interactors.storebox.UpdateStoreboxFlagsInteractorImpl
import dk.eboks.app.domain.interactors.user.CheckSsnExistsInteractor
import dk.eboks.app.domain.interactors.user.CheckSsnExistsInteractorImpl
import dk.eboks.app.domain.interactors.user.ConfirmPhoneInteractor
import dk.eboks.app.domain.interactors.user.ConfirmPhoneInteractorImpl
import dk.eboks.app.domain.interactors.user.CreateDebugUserInteractorImpl
import dk.eboks.app.domain.interactors.user.CreateUserInteractor
import dk.eboks.app.domain.interactors.user.CreateUserInteractorImpl
import dk.eboks.app.domain.interactors.user.DeleteUserInteractor
import dk.eboks.app.domain.interactors.user.DeleteUserInteractorImpl
import dk.eboks.app.domain.interactors.user.GetUserProfileInteractor
import dk.eboks.app.domain.interactors.user.GetUserProfileInteractorImpl
import dk.eboks.app.domain.interactors.user.GetUsersInteractor
import dk.eboks.app.domain.interactors.user.GetUsersInteractorImpl
import dk.eboks.app.domain.interactors.user.SaveUserInteractor
import dk.eboks.app.domain.interactors.user.SaveUserInteractorImpl
import dk.eboks.app.domain.interactors.user.SaveUserSettingsInteractor
import dk.eboks.app.domain.interactors.user.SaveUserSettingsInteractorImpl
import dk.eboks.app.domain.interactors.user.SaveUsersInteractor
import dk.eboks.app.domain.interactors.user.SaveUsersInteractorImpl
import dk.eboks.app.domain.interactors.user.UpdateUserInteractor
import dk.eboks.app.domain.interactors.user.UpdateUserInteractorImpl
import dk.eboks.app.domain.interactors.user.VerifyEmailInteractor
import dk.eboks.app.domain.interactors.user.VerifyEmailInteractorImpl
import dk.eboks.app.domain.interactors.user.VerifyPhoneInteractor
import dk.eboks.app.domain.interactors.user.VerifyPhoneInteractorImpl
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.AuthClient
import dk.eboks.app.domain.managers.CacheManager
import dk.eboks.app.domain.managers.CryptoManager
import dk.eboks.app.domain.managers.DownloadManager
import dk.eboks.app.domain.managers.EncryptionPreferenceManager
import dk.eboks.app.domain.managers.FileCacheManager
import dk.eboks.app.domain.managers.PermissionManager
import dk.eboks.app.domain.managers.PrefManager
import dk.eboks.app.domain.managers.ResourceManager
import dk.eboks.app.domain.managers.UIManager
import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.repositories.ChannelsRepository
import dk.eboks.app.domain.repositories.CollectionsRepository
import dk.eboks.app.domain.repositories.FoldersRepository
import dk.eboks.app.domain.repositories.MailCategoriesRepository
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.domain.repositories.SenderCategoriesRepository
import dk.eboks.app.domain.repositories.SendersRepository
import dk.eboks.app.domain.repositories.SettingsRepository
import dk.eboks.app.domain.repositories.SignupRepository
import dk.eboks.app.domain.repositories.UserRepository
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.Executor

@Module
class InteractorModule {
    @Provides
    fun provideLoginInteractor(
        executor: Executor,
        api: Api,
        appStateManager: AppStateManager,
        userManager: UserManager,
        userSettingsManager: UserSettingsManager,
        authClient: AuthClient,
        cacheManager: CacheManager,
        mailCategoriesRepository: MailCategoriesRepository
    ): LoginInteractor {
        return LoginInteractorImpl(
            executor,
            api,
            appStateManager,
            userManager,
            userSettingsManager,
            authClient,
            cacheManager,
            mailCategoriesRepository
        )
    }

    @Provides
    fun provideTestLoginInteractor(
        executor: Executor,
        appStateManager: AppStateManager,
        authClient: AuthClient
    ): TestLoginInteractor {
        return TestLoginInteractorImpl(executor, appStateManager, authClient)
    }

    @Provides
    fun provideDecryptInteractor(
        executor: Executor,
        encryptionPreferenceManager: EncryptionPreferenceManager
    ): DecryptUserLoginInfoInteractor {
        return DecryptUserLoginInfoInteractorImpl(executor, encryptionPreferenceManager)
    }

    @Provides
    fun provideCheckSsnExistsInteractor(
        executor: Executor,
        api: Api,
        userRestRepository: UserRepository
    ): CheckSsnExistsInteractor {
        return CheckSsnExistsInteractorImpl(executor, api, userRestRepository)
    }

    @Provides
    fun provideVerifyignupMailInteractor(
        executor: Executor,
        signupRepository: SignupRepository
    ): CheckSignupMailInteractor {
        return CheckSignupMailInteractorImpl(executor, signupRepository)
    }

    @Provides
    fun provideVerifyEmailInteractor(
        executor: Executor,
        api: Api,
        userRestRepository: UserRepository
    ): VerifyEmailInteractor {
        return VerifyEmailInteractorImpl(executor, api, userRestRepository)
    }

    @Provides
    fun provideUpdateUserInteractor(
        executor: Executor,
        api: Api,
        userRestRepository: UserRepository
    ): UpdateUserInteractor {
        return UpdateUserInteractorImpl(executor, api, userRestRepository)
    }

    @Provides
    fun provideBootstrapInteractor(interactor: BootstrapInteractorImpl): BootstrapInteractor {
        return interactor
    }

    @Provides
    fun provideGetCategoriesInteractor(
        executor: Executor,
        mailCategoriesRepository: MailCategoriesRepository
    ): GetCategoriesInteractor {
        return GetMailCategoriesInteractorImpl(executor, mailCategoriesRepository)
    }

    @Provides
    fun provideGetFoldersInteractor(
        executor: Executor,
        foldersRepository: FoldersRepository,
        resourceManager: ResourceManager
    ): GetFoldersInteractor {
        return GetFoldersInteractorImpl(executor, foldersRepository, resourceManager)
    }

    @Provides
    fun provideDeleteMessagesInteractor(
        executor: Executor,
        messagesRepository: MessagesRepository
    ): DeleteMessagesInteractor {
        return DeleteMessagesInteractorImpl(executor, messagesRepository)
    }

    @Provides
    fun provideMoveMessagesInteractor(
        executor: Executor,
        messagesRepository: MessagesRepository
    ): MoveMessagesInteractor {
        return MoveMessagesInteractorImpl(executor, messagesRepository)
    }

    @Provides
    fun provideGetMessagesInteractor(
        executor: Executor,
        messagesRepository: MessagesRepository
    ): GetMessagesInteractor {
        return GetMessagesInteractorImpl(executor, messagesRepository)
    }

    @Provides
    fun provideGetStorageInteractor(
        executor: Executor,
        messagesRepository: MessagesRepository
    ): GetStorageInteractor {
        return GetStorageInteractorImpl(executor, messagesRepository)
    }

    @Provides
    fun provideGetLatestUploadsInteractor(
        executor: Executor,
        messagesRepository: MessagesRepository
    ): GetLatestUploadsInteractor {
        return GetLatestUploadsInteractorImpl(executor, messagesRepository)
    }

    @Provides
    fun provideUploadFileInteractor(
        executor: Executor,
        messagesRepository: MessagesRepository
    ): UploadFileInteractor {
        return UploadFileInteractorImpl(executor, messagesRepository)
    }

    @Provides
    fun provideGetStoreboxCreditCardsInteractor(
        executor: Executor,
        api: Api
    ): GetStoreboxCreditCardsInteractor {
        return GetStoreboxCreditCardsInteractorImpl(executor, api)
    }

    @Provides
    fun provideDeleteStoreboxCreditCardInteractor(
        executor: Executor,
        api: Api
    ): DeleteStoreboxCreditCardInteractor {
        return DeleteStoreboxCreditCardInteractorImpl(executor, api)
    }

    @Provides
    fun provideOpenFolderInteractor(
        executor: Executor,
        appStateManager: AppStateManager,
        uiManager: UIManager
    ): OpenFolderInteractor {
        return OpenFolderInteractorImpl(executor, appStateManager, uiManager)
    }

    @Provides
    fun provideOpenMessageInteractor(interactor: OpenMessageInteractorImpl): OpenMessageInteractor {
        return interactor
    }

    @Provides
    fun provideOpenAttachmentInteractor(
        executor: Executor,
        appStateManager: AppStateManager,
        uiManager: UIManager,
        downloadManager: DownloadManager,
        fileCacheManager: FileCacheManager
    ): OpenAttachmentInteractor {
        return OpenAttachmentInteractorImpl(
            executor,
            appStateManager,
            uiManager,
            downloadManager,
            fileCacheManager
        )
    }

    @Provides
    fun provideGetReplyFormInteractor(
        executor: Executor,
        messagesRepository: MessagesRepository
    ): GetReplyFormInteractor {
        return GetReplyFormInteractorImpl(executor, messagesRepository)
    }

    @Provides
    fun provideSubmitReplyFormInteractor(
        executor: Executor,
        messagesRepository: MessagesRepository
    ): SubmitReplyFormInteractor {
        return SubmitReplyFormInteractorImpl(executor, messagesRepository)
    }

    @Provides
    fun provideSaveAttachmentInteractor(
        executor: Executor,
        appStateManager: AppStateManager,
        fileCacheManager: FileCacheManager,
        permissionManager: PermissionManager
    ): SaveAttachmentInteractor {
        return SaveAttachmentInteractorImpl(
            executor,
            appStateManager,
            fileCacheManager,
            permissionManager
        )
    }

    @Provides
    fun provideGetChannelsInteractor(
        executor: Executor,
        channelsRepository: ChannelsRepository
    ): GetChannelsInteractor {
        return GetChannelsInteractorImpl(executor, channelsRepository)
    }

    @Provides
    fun provideCreateUserInteractor(interactor: CreateUserInteractorImpl): CreateUserInteractor {
        return interactor
    }

    @Provides
    fun provideDebugCreateUserInteractor(
        executor: Executor,
        userManager: UserManager
    ): CreateDebugUserInteractorImpl {
        return CreateDebugUserInteractorImpl(executor, userManager)
    }

    @Provides
    fun provideSaveUserSettingsInteractor(
        executor: Executor,
        appStateManager: AppStateManager,
        userManager: UserSettingsManager
    ): SaveUserSettingsInteractor {
        return SaveUserSettingsInteractorImpl(executor, appStateManager, userManager)
    }

    @Provides
    fun provideSaveUserInteractor(
        executor: Executor,
        userManager: UserManager
    ): SaveUserInteractor {
        return SaveUserInteractorImpl(executor, userManager)
    }

    @Provides
    fun provideSaveUsersInteractor(
        executor: Executor,
        userManager: UserManager
    ): SaveUsersInteractor {
        return SaveUsersInteractorImpl(executor, userManager)
    }

    @Provides
    fun provideDeleteRSAKeyForUserInteractor(
        executor: Executor,
        cryptoManager: CryptoManager
    ): DeleteRSAKeyForUserInteractor {
        return DeleteRSAKeyForUserInteractorImpl(executor, cryptoManager)
    }

    @Provides
    fun provideDeleteUserInteractor(
        executor: Executor,
        userManager: UserManager,
        userSettingsManager: UserSettingsManager,
        deleteRSAKeyForUserInteractor: DeleteRSAKeyForUserInteractor
    ): DeleteUserInteractor {
        return DeleteUserInteractorImpl(
            executor,
            userManager,
            userSettingsManager,
            deleteRSAKeyForUserInteractor
        )
    }

    @Provides
    fun provideGetUsersInteractor(
        executor: Executor,
        userManager: UserManager
    ): GetUsersInteractor {
        return GetUsersInteractorImpl(executor, userManager)
    }

    @Provides
    fun provideGetUserProfileInteractor(
        executor: Executor,
        api: Api,
        appStateManager: AppStateManager,
        userManager: UserManager,
        userSettingsManager: UserSettingsManager
    ): GetUserProfileInteractor {
        return GetUserProfileInteractorImpl(
            executor,
            api,
            appStateManager,
            userManager,
            userSettingsManager
        )
    }

    @Provides
    fun provideUpdateMessageInteractor(
        executor: Executor,
        messagesRepository: MessagesRepository
    ): UpdateMessageInteractor {
        return UpdateMessageInteractorImpl(executor, messagesRepository)
    }

    @Provides
    fun provideGetChannelInteractor(
        executor: Executor,
        channelsRepository: ChannelsRepository
    ): GetChannelInteractor {
        return GetChannelInteractorImpl(executor, channelsRepository)
    }

    @Provides
    fun provideInstallChannelInteractor(
        executor: Executor,
        api: Api
    ): InstallChannelInteractor {
        return InstallChannelInteractorImpl(executor, api)
    }

    @Provides
    fun provideUninstallChannelInteractor(
        executor: Executor,
        api: Api
    ): UninstallChannelInteractor {
        return UninstallChannelInteractorImpl(executor, api)
    }

    @Provides
    fun provideGetChannelHomeContentInteractor(
        executor: Executor,
        channelsRepository: ChannelsRepository
    ): GetChannelHomeContentInteractor {
        return GetChannelHomeContentInteractorImpl(executor, channelsRepository)
    }

    @Provides
    fun provideGetChannelContentLinkInteractor(interactor: GetChannelContentLinkInteractorImpl): GetChannelContentLinkInteractor {
        return interactor
    }

    @Provides
    fun provideGetSenderCategoriesInteractor(
        executor: Executor,
        senderCategoriesRepository: SenderCategoriesRepository
    ): GetSenderCategoriesInteractor {
        return GetSenderCategoriesInteractorImpl(executor, senderCategoriesRepository)
    }

    @Provides
    fun provideGetSendersInteractor(
        executor: Executor,
        sendersRepository: SendersRepository,
        senderCategoriesRepository: SenderCategoriesRepository
    ): GetSendersInteractor {
        return GetSendersInteractorImpl(executor, sendersRepository, senderCategoriesRepository)
    }

    @Provides
    fun provideGetSenderDetailInteractor(
        executor: Executor,
        sendersRepository: SendersRepository
    ): GetSenderDetailInteractor {
        return GetSenderDetailInteractorImpl(executor, sendersRepository)
    }

    @Provides
    fun provideGetStoreboxReceiptsInteractor(
        executor: Executor,
        api: Api
    ): GetStoreboxReceiptsInteractor {
        return GetStoreboxReceiptsInteractorImpl(executor, api)
    }

    @Provides
    fun provideGetStoreboxReceiptInteractor(
        executor: Executor,
        api: Api
    ): GetStoreboxReceiptInteractor {
        return GetStoreboxReceiptInteractorImpl(executor, api)
    }

    @Provides
    fun provideGetSegmentDetailInteractor(executor: Executor, api: Api): GetSegmentInteractor {
        return GetSegmentInteractorImpl(executor, api)
    }

    @Provides
    fun provideGetPendingInteractor(executor: Executor, api: Api): GetPendingInteractor {
        return GetPendingInteractorImpl(executor, api)
    }

    @Provides
    fun provideEncryptUserLoginInfoInteractor(
        executor: Executor,
        encryptionPreferenceManager: EncryptionPreferenceManager,
        prefManager: PrefManager
    ): EncryptUserLoginInfoInteractor {
        return EncryptUserLoginInfoInteractorImpl(executor, encryptionPreferenceManager)
    }

    @Provides
    fun provideGetCollectionsInteractor(
        executor: Executor,
        collectionsRepository: CollectionsRepository
    ): GetCollectionsInteractor {
        return GetCollectionsInteractorImpl(executor, collectionsRepository)
    }

    @Provides
    fun provideRegisterInteractor(executor: Executor, api: Api): RegisterInteractor {
        return RegisterInteractorImpl(executor, api)
    }

    @Provides
    fun provideUnRegisterInteractor(executor: Executor, api: Api): UnRegisterInteractor {
        return UnRegisterInteractorImpl(executor, api)
    }

    @Provides
    fun provideRegistrationsInteractor(executor: Executor, api: Api): GetRegistrationsInteractor {
        return GetRegistrationsInteractorImpl(executor, api)
    }

    @Provides
    fun provideLinkStoreboxInteractor(executor: Executor, api: Api): LinkStoreboxInteractor {
        return LinkStoreboxInteractorImpl(executor, api)
    }

    @Provides
    fun provideCreateStoreboxInteractor(executor: Executor, api: Api): CreateStoreboxInteractor {
        return CreateStoreboxInteractorImpl(executor, api)
    }

    @Provides
    fun provideGetStoreboxProfileInteractor(
        executor: Executor,
        api: Api
    ): GetStoreboxProfileInteractor {
        return GetStoreboxProfileInteractorImpl(executor, api)
    }

    @Provides
    fun providePutStoreboxProfileInteractor(
        executor: Executor,
        api: Api
    ): PutStoreboxProfileInteractor {
        return PutStoreboxProfileInteractorImpl(executor, api)
    }

    @Provides
    fun provideGetStoreboxCardLinkInteractor(
        executor: Executor,
        api: Api
    ): GetStoreboxCardLinkInteractor {
        return GetStoreboxCardLinkInteractorImpl(executor, api)
    }

    @Provides
    fun provideDeleteStoreboxAccountLinkInteractor(
        executor: Executor,
        api: Api
    ): DeleteStoreboxAccountLinkInteractor {
        return DeleteStoreboxAccountLinkInteractorImpl(executor, api)
    }

    @Provides
    fun provideDeleteStoreboxReceiptInteractor(
        executor: Executor,
        api: Api
    ): DeleteStoreboxReceiptInteractor {
        return DeleteStoreboxReceiptInteractorImpl(executor, api)
    }

    @Provides
    fun provideUpdateStoreboxFlagsInteractor(
        executor: Executor,
        api: Api
    ): UpdateStoreboxFlagsInteractor {
        return UpdateStoreboxFlagsInteractorImpl(executor, api)
    }

    @Provides
    fun provideConfirmStoreboxInteractor(executor: Executor, api: Api): ConfirmStoreboxInteractor {
        return ConfirmStoreboxInteractorImpl(executor, api)
    }

    @Provides
    fun provideTransformTokenInteractor(
        executor: Executor,
        api: Api,
        appStateManager: AppStateManager,
        userManager: UserManager,
        userSettingsManager: UserSettingsManager,
        authClient: AuthClient,
        cacheManager: CacheManager,
        mailCategoriesRepository: MailCategoriesRepository
    ): TransformTokenInteractor {
        return TransformTokenInteractorImpl(
            executor,
            api,
            appStateManager,
            userManager,
            userSettingsManager,
            authClient,
            cacheManager,
            mailCategoriesRepository
        )
    }

    @Provides
    fun provideMergeAndImpersonateInteractor(
        executor: Executor,
        api: Api,
        appStateManager: AppStateManager,
        userManager: UserManager,
        userSettingsManager: UserSettingsManager,
        authClient: AuthClient,
        cacheManager: CacheManager,
        mailCategoriesRepository: MailCategoriesRepository
    ): MergeAndImpersonateInteractor {
        return MergeAndImpersonateInteractorImpl(
            executor,
            api,
            appStateManager,
            userManager,
            userSettingsManager,
            authClient,
            cacheManager,
            mailCategoriesRepository
        )
    }

    @Provides
    fun provideVerifyProfileInteractor(
        executor: Executor,
        api: Api,
        appStateManager: AppStateManager,
        userManager: UserManager,
        userSettingsManager: UserSettingsManager,
        authClient: AuthClient,
        cacheManager: CacheManager,
        mailCategoriesRepository: MailCategoriesRepository
    ): VerifyProfileInteractor {
        return VerifyProfileInteractorImpl(
            executor,
            api,
            appStateManager,
            userManager,
            userSettingsManager,
            authClient,
            cacheManager,
            mailCategoriesRepository
        )
    }

    @Provides
    fun provideSetCurrentUserInteractor(
        executor: Executor,
        api: Api,
        appStateManager: AppStateManager,
        userManager: UserManager,
        userSettingsManager: UserSettingsManager
    ): SetCurrentUserInteractor {
        return SetCurrentUserInteractorImpl(
            executor,
            api,
            appStateManager,
            userManager,
            userSettingsManager
        )
    }

    @Provides
    fun provideResetPasswordInteractor(interactor: ResetPasswordInteractorImpl): ResetPasswordInteractor {
        return interactor
    }

    @Provides
    fun provideVerifyPhoneInteractor(
        executor: Executor,
        api: Api,
        userRestRepo: UserRepository
    ): VerifyPhoneInteractor {
        return VerifyPhoneInteractorImpl(executor, api, userRestRepo)
    }

    @Provides
    fun provideConfirmPhoneInteractor(
        executor: Executor,
        api: Api,
        userRestRepo: UserRepository
    ): ConfirmPhoneInteractor {
        return ConfirmPhoneInteractorImpl(executor, api, userRestRepo)
    }

    @Provides
    fun provideGetSignLinkInteractor(executor: Executor, api: Api): GetSignLinkInteractor {
        return GetSignLinkInteractorImpl(executor, api)
    }

    // E Key Interactors

    @Provides
    fun provideGetEKeyVaultInteractor(executor: Executor, api: Api): GetEKeyVaultInteractor {
        return GetEKeyVaultInteractorImpl(executor, api)
    }

    @Provides
    fun provideSetEKeyVaultInteractor(executor: Executor, api: Api): SetEKeyVaultInteractor {
        return SetEKeyVaultInteractorImpl(executor, api)
    }

    @Provides
    fun provideDeleteEKeyVaultInteractor(executor: Executor, api: Api): DeleteEKeyVaultInteractor {
        return DeleteEKeyVaultInteractorImpl(executor, api)
    }

    @Provides
    fun provideGetEKeyMasterkeyInteractor(
        executor: Executor,
        api: Api
    ): GetEKeyMasterkeyInteractor {
        return GetEKeyMasterkeyInteractorImpl(executor, api)
    }

    @Provides
    fun provideSetEKeyMasterkeyInteractor(
        executor: Executor,
        api: Api
    ): SetEKeyMasterkeyInteractor {
        return SetEKeyMasterkeyInteractorImpl(executor, api)
    }

    @Provides
    fun provideDeleteEKeyMasterkeyInteractor(
        executor: Executor,
        api: Api
    ): DeleteEKeyMasterkeyInteractor {
        return DeleteEKeyMasterkeyInteractorImpl(executor, api)
    }

    @Provides
    fun provideSaveReceiptInteractor(executor: Executor, api: Api): SaveReceiptInteractor {
        return SaveReceiptInteractorImpl(executor, api)
    }

    @Provides
    fun provideShareReceiptInteractor(
        executor: Executor,
        downloadManager: DownloadManager
    ): ShareReceiptInteractor {
        return ShareReceiptInteractorImpl(executor, downloadManager)
    }

    @Provides
    fun provideCheckRSAKeyPresenceInteractor(
        executor: Executor,
        cryptoManager: CryptoManager
    ): CheckRSAKeyPresenceInteractor {
        return CheckRSAKeyPresenceInteractorImpl(executor, cryptoManager)
    }

    @Provides
    fun provideGenerateRSAKey(
        executor: Executor,
        cryptoManager: CryptoManager
    ): GenerateRSAKeyInteractor {
        return GenerateRSAKeyInteractorImpl(executor, cryptoManager)
    }

    @Provides
    fun provideDeleteRSAKey(
        executor: Executor,
        cryptoManager: CryptoManager
    ): DeleteRSAKeyInteractor {
        return DeleteRSAKeyInteractorImpl(executor, cryptoManager)
    }

    @Provides
    fun provideActivateDevice(
        executor: Executor,
        settingsRepository: SettingsRepository,
        api: Api
    ): ActivateDeviceInteractor {
        return ActivateDeviceInteractorImpl(executor, settingsRepository, api)
    }

    @Provides
    fun provideCreateFolder(
        executor: Executor,
        foldersRepository: FoldersRepository
    ): CreateFolderInteractor {
        return CreateFolderInteractorImpl(executor, foldersRepository)
    }

    @Provides
    fun provideDeleteFolder(
        executor: Executor,
        foldersRepository: FoldersRepository
    ): DeleteFolderInteractor {
        return DeleteFolderInteractorImpl(executor, foldersRepository)
    }

    @Provides
    fun provideEditFolder(
        executor: Executor,
        foldersRepository: FoldersRepository
    ): EditFolderInteractor {
        return EditFolderInteractorImpl(executor, foldersRepository)
    }

    @Provides
    fun provideGetAllSharesInteractor(executor: Executor, api: Api): GetAllSharesInteractor {
        return GetAllSharesInteractorImpl(executor, api)
    }
}