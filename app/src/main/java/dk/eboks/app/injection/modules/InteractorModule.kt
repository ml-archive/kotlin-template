package dk.eboks.app.injection.modules

import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.interactors.BootstrapInteractor
import dk.eboks.app.domain.interactors.BootstrapInteractorImpl
import dk.eboks.app.domain.interactors.GetCategoriesInteractor
import dk.eboks.app.domain.interactors.GetMailCategoriesInteractorImpl
import dk.eboks.app.domain.interactors.authentication.*
import dk.eboks.app.domain.interactors.authentication.mobileacces.*
import dk.eboks.app.domain.interactors.channel.*
import dk.eboks.app.domain.interactors.encryption.DecryptUserLoginInfoInteractor
import dk.eboks.app.domain.interactors.encryption.DecryptUserLoginInfoInteractorImpl
import dk.eboks.app.domain.interactors.encryption.EncryptUserLoginInfoInteractor
import dk.eboks.app.domain.interactors.encryption.EncryptUserLoginInfoInteractorImpl
import dk.eboks.app.domain.interactors.folder.*
import dk.eboks.app.domain.interactors.message.*
import dk.eboks.app.domain.interactors.message.messageoperations.*
import dk.eboks.app.domain.interactors.message.messageoperations.MoveMessagesInteractorImpl
import dk.eboks.app.domain.interactors.sender.*
import dk.eboks.app.domain.interactors.sender.register.*
import dk.eboks.app.domain.interactors.signup.CheckSignupMailInteractor
import dk.eboks.app.domain.interactors.signup.CheckSignupMailInteractorImpl
import dk.eboks.app.domain.interactors.storebox.*
import dk.eboks.app.domain.interactors.user.*
import dk.eboks.app.domain.managers.*
import dk.eboks.app.domain.repositories.*
import dk.eboks.app.network.Api
import dk.eboks.app.network.repositories.SignupRestRepository
import dk.nodes.arch.domain.executor.Executor
import okhttp3.OkHttpClient

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
        return LoginInteractorImpl(executor, api, appStateManager, userManager, userSettingsManager, authClient, cacheManager, mailCategoriesRepository)
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
            api: Api,
            signupRestRepository: SignupRestRepository
    ): CheckSignupMailInteractor {
        return CheckSignupMailInteractorImpl(executor, api, signupRestRepository)
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
    fun provideBootstrapInteractor(
            executor: Executor,
            guidManager: GuidManager,
            settingsRepository: SettingsRepository,
            appStateManager: AppStateManager,
            fileCacheManager: FileCacheManager,
            cacheManager: CacheManager,
            userManager: UserManager,
            api: Api,
            prefManager: PrefManager
    ): BootstrapInteractor {
        return BootstrapInteractorImpl(
                executor,
                guidManager,
                settingsRepository,
                appStateManager,
                fileCacheManager,
                cacheManager,
                userManager,
                api,
                prefManager
        )
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
    fun provideMoveMessagesInteractor(executor: Executor, messagesRepository: MessagesRepository): MoveMessagesInteractor {
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
    fun provideOpenMessageInteractor(
            executor: Executor,
            appStateManager: AppStateManager,
            uiManager: UIManager,
            downloadManager: DownloadManager,
            fileCacheManager: FileCacheManager,
            messagesRepository: MessagesRepository
    ): OpenMessageInteractor {
        return OpenMessageInteractorImpl(
                executor,
                appStateManager,
                uiManager,
                downloadManager,
                fileCacheManager,
                messagesRepository
        )
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
            executor: Executor, appStateManager: AppStateManager,
            fileCacheManager: FileCacheManager, permissionManager: PermissionManager
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
    fun provideCreateUserInteractor(
            executor: Executor,
            userManager: UserManager,
            api: Api
    ): CreateUserInteractor {
        return CreateUserInteractorImpl(executor, userManager, api)
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
    fun provideDeleteUserInteractor(
            executor: Executor,
            userManager: UserManager,
            userSettingsManager: UserSettingsManager
    ): DeleteUserInteractor {
        return DeleteUserInteractorImpl(executor, userManager, userSettingsManager)
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
        return GetUserProfileInteractorImpl(executor, api, appStateManager, userManager, userSettingsManager)
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
    fun provideGetChannelContentLinkInteractor(
            executor: Executor,
            httpClient: OkHttpClient,
            appStateManager: AppStateManager
    ): GetChannelContentLinkInteractor {
        return GetChannelContentLinkInteractorImpl(executor, httpClient, appStateManager)
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
    fun provideGetStoreboxProfileInteractor(executor: Executor, api: Api): GetStoreboxProfileInteractor {
        return GetStoreboxProfileInteractorImpl(executor, api)
    }

    @Provides
    fun providePutStoreboxProfileInteractor(executor: Executor, api: Api): PutStoreboxProfileInteractor {
        return PutStoreboxProfileInteractorImpl(executor, api)
    }

    @Provides
    fun provideGetStoreboxCardLinkInteractor(executor: Executor, api: Api): GetStoreboxCardLinkInteractor {
        return GetStoreboxCardLinkInteractorImpl(executor, api)
    }

    @Provides
    fun provideDeleteStoreboxAccountLinkInteractor(executor: Executor, api: Api): DeleteStoreboxAccountLinkInteractor {
        return DeleteStoreboxAccountLinkInteractorImpl(executor, api)
    }

    @Provides
    fun provideDeleteStoreboxReceiptInteractor(executor: Executor, api: Api): DeleteStoreboxReceiptInteractor {
        return DeleteStoreboxReceiptInteractorImpl(executor, api)
    }

    @Provides
    fun provideUpdateStoreboxFlagsInteractor(executor: Executor, api: Api): UpdateStoreboxFlagsInteractor {
        return UpdateStoreboxFlagsInteractorImpl(executor, api)
    }

    @Provides
    fun provideConfirmStoreboxInteractor(executor: Executor, api: Api): ConfirmStoreboxInteractor {
        return ConfirmStoreboxInteractorImpl(executor, api)
    }

    @Provides
    fun provideTransformTokenInteractor(executor: Executor, api: Api, appStateManager: AppStateManager, userManager: UserManager, userSettingsManager: UserSettingsManager, authClient: AuthClient, cacheManager: CacheManager, mailCategoriesRepository: MailCategoriesRepository): TransformTokenInteractor {
        return TransformTokenInteractorImpl(executor, api, appStateManager, userManager, userSettingsManager, authClient, cacheManager, mailCategoriesRepository)
    }

    @Provides
    fun provideMergeAndImpersonateInteractor(executor: Executor, api: Api, appStateManager: AppStateManager, userManager: UserManager, userSettingsManager: UserSettingsManager, authClient: AuthClient, cacheManager: CacheManager, mailCategoriesRepository: MailCategoriesRepository): MergeAndImpersonateInteractor {
        return MergeAndImpersonateInteractorImpl(executor, api, appStateManager, userManager, userSettingsManager, authClient, cacheManager, mailCategoriesRepository)
    }

    @Provides
    fun provideVerifyProfileInteractor(executor: Executor, api: Api, appStateManager: AppStateManager, userManager: UserManager, userSettingsManager: UserSettingsManager, authClient: AuthClient, cacheManager: CacheManager, mailCategoriesRepository: MailCategoriesRepository): VerifyProfileInteractor {
        return VerifyProfileInteractorImpl(executor, api, appStateManager, userManager, userSettingsManager, authClient, cacheManager, mailCategoriesRepository)
    }

    @Provides
    fun provideSetCurrentUserInteractor(executor: Executor, api: Api, appStateManager: AppStateManager, userManager: UserManager, userSettingsManager: UserSettingsManager): SetCurrentUserInteractor {
        return SetCurrentUserInteractorImpl(executor, api, appStateManager, userManager, userSettingsManager)
    }

    @Provides
    fun provideResetPasswordInteractor(executor: Executor, api: Api): ResetPasswordInteractor {
        return ResetPasswordInteractorImpl(executor, api)
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

    @Provides
    fun provideSaveReceiptInteractor(executor: Executor, api: Api): SaveReceiptInteractor {
        return SaveReceiptInteractorImpl(executor, api)
    }

    @Provides
    fun provideShareReceiptInteractor(executor: Executor, downloadManager: DownloadManager): ShareReceiptInteractor {
        return ShareReceiptInteractorImpl(executor, downloadManager)
    }

    @Provides
    fun provideCheckRSAKeyPresenceInteractor(executor: Executor, cryptoManager: CryptoManager): CheckRSAKeyPresenceInteractor {
        return CheckRSAKeyPresenceInteractorImpl(executor, cryptoManager)
    }

    @Provides
    fun provideGenerateRSAKey(executor: Executor, cryptoManager: CryptoManager): GenerateRSAKeyInteractor {
        return GenerateRSAKeyInteractorImpl(executor, cryptoManager)
    }

    @Provides
    fun provideDeleteRSAKey(executor: Executor, cryptoManager: CryptoManager): DeleteRSAKeyInteractor {
        return DeleteRSAKeyInteractorImpl(executor, cryptoManager)
    }

    @Provides
    fun provideActivateDevice(executor: Executor, settingsRepository: SettingsRepository, api: Api): ActivateDeviceInteractor {
        return ActivateDeviceInteractorImpl(executor, settingsRepository, api)
    }

    @Provides
    fun provideCreateFolder(executor: Executor, foldersRepository: FoldersRepository): CreateFolderInteractor {
        return CreateFolderInteractorImpl(executor, foldersRepository)
    }

    @Provides
    fun provideDeleteFolder(executor: Executor, foldersRepository: FoldersRepository): DeleteFolderInteractor {
        return DeleteFolderInteractorImpl(executor, foldersRepository)
    }

    @Provides
    fun provideEditFolder(executor: Executor, foldersRepository: FoldersRepository): EditFolderInteractor {
        return EditFolderInteractorImpl(executor, foldersRepository)
    }
}