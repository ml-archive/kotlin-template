package dk.eboks.app.injection.modules

import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.interactors.*
import dk.eboks.app.domain.interactors.channel.*
import dk.eboks.app.domain.interactors.encryption.EncryptUserLoginInfoInteractor
import dk.eboks.app.domain.interactors.encryption.EncryptUserLoginInfoInteractorImpl
import dk.eboks.app.domain.interactors.folder.GetFoldersInteractor
import dk.eboks.app.domain.interactors.folder.GetFoldersInteractorImpl
import dk.eboks.app.domain.interactors.folder.OpenFolderInteractor
import dk.eboks.app.domain.interactors.folder.OpenFolderInteractorImpl
import dk.eboks.app.domain.interactors.message.*
import dk.eboks.app.domain.interactors.sender.*
import dk.eboks.app.domain.interactors.sender.register.*
import dk.eboks.app.domain.interactors.storebox.GetStoreboxReceiptsInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxReceiptsInteractorImpl
import dk.eboks.app.domain.interactors.user.*
import dk.eboks.app.domain.managers.*
import dk.eboks.app.domain.repositories.*
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.Executor

/**
 * Created by bison on 09/10/17.
 */
@Module
class InteractorModule {
    @Provides
    fun provideLoginInteractor(
            executor: Executor,
            api: Api,
            protocolManager: ProtocolManager
    ): LoginInteractor {
        return LoginInteractorImpl(executor, api, protocolManager)
    }

    @Provides
    fun provideBootstrapInteractor(
            executor: Executor, guidManager: GuidManager, settingsRepository: SettingsRepository,
            protocolManager: ProtocolManager, appStateManager: AppStateManager,
            fileCacheManager: FileCacheManager, userManager: UserManager
    ): BootstrapInteractor {
        return BootstrapInteractorImpl(
                executor,
                guidManager,
                settingsRepository,
                protocolManager,
                appStateManager,
                fileCacheManager,
                userManager
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
    fun provideGetMessagesInteractor(
            executor: Executor,
            messagesRepository: MessagesRepository
    ): GetMessagesInteractor {
        return GetMessagesInteractorImpl(executor, messagesRepository)
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
            userManager: UserManager
    ): CreateUserInteractor {
        return CreateUserInteractorImpl(executor, userManager)
    }

    @Provides
    fun provideSaveUserInteractor(
            executor: Executor,
            userManager: UserManager
    ): SaveUserInteractor {
        return SaveUserInteractorImpl(executor, userManager)
    }

    @Provides
    fun provideDeleteUserInteractor(
            executor: Executor,
            userManager: UserManager
    ): DeleteUserInteractor {
        return DeleteUserInteractorImpl(executor, userManager)
    }

    @Provides
    fun provideGetUsersInteractor(
            executor: Executor,
            userManager: UserManager
    ): GetUsersInteractor {
        return GetUsersInteractorImpl(executor, userManager)
    }

    @Provides
    fun provideGetChannelInteractor(
            executor: Executor,
            channelsRepository: ChannelsRepository
    ): GetChannelInteractor {
        return GetChannelInteractorImpl(executor, channelsRepository)
    }

    @Provides
    fun provideGetChannelHomeContentInteractor(
            executor: Executor,
            channelsRepository: ChannelsRepository
    ): GetChannelHomeContentInteractor {
        return GetChannelHomeContentInteractorImpl(executor, channelsRepository)
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
}