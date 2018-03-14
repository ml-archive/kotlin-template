package dk.eboks.app.injection.modules

import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.interactors.*
import dk.eboks.app.domain.interactors.channel.GetChannelsInteractor
import dk.eboks.app.domain.interactors.channel.GetChannelsInteractorImpl
import dk.eboks.app.domain.interactors.folder.GetFoldersInteractor
import dk.eboks.app.domain.interactors.folder.GetFoldersInteractorImpl
import dk.eboks.app.domain.interactors.folder.OpenFolderInteractor
import dk.eboks.app.domain.interactors.folder.OpenFolderInteractorImpl
import dk.eboks.app.domain.interactors.message.*
import dk.eboks.app.domain.interactors.sender.*
import dk.eboks.app.domain.interactors.user.CreateUserInteractor
import dk.eboks.app.domain.interactors.user.CreateUserInteractorImpl
import dk.eboks.app.domain.interactors.user.GetUsersInteractor
import dk.eboks.app.domain.interactors.user.GetUsersInteractorImpl
import dk.eboks.app.domain.managers.*
import dk.eboks.app.domain.repositories.*
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.Executor

/**
 * Created by bison on 09/10/17.
 */
@Module
class InteractorModule {
    @Provides fun provideLoginInteractor(executor: Executor, api: Api, protocolManager: ProtocolManager) : LoginInteractor
    {
        return LoginInteractorImpl(executor, api, protocolManager)
    }

    @Provides fun provideBootstrapInteractor(executor: Executor, guidManager: GuidManager, settingsRepository: SettingsRepository,
                                             protocolManager: ProtocolManager, appStateManager: AppStateManager,
                                             fileCacheManager: FileCacheManager, userManager: UserManager) : BootstrapInteractor
    {
        return BootstrapInteractorImpl(executor, guidManager, settingsRepository, protocolManager, appStateManager, fileCacheManager, userManager)
    }

    @Provides fun provideGetSendersInteractor(executor: Executor, sendersRepository: SendersRepository) : GetSendersInteractor
    {
        return GetSendersInteractorImpl(executor, sendersRepository)
    }

    @Provides fun provideGetCategoriesInteractor(executor: Executor, categoriesRepository: CategoriesRepository) : GetCategoriesInteractor
    {
        return GetCategoriesInteractorImpl(executor, categoriesRepository)
    }

    @Provides fun provideGetFoldersInteractor(executor: Executor, foldersRepository: FoldersRepository, resourceManager: ResourceManager) : GetFoldersInteractor
    {
        return GetFoldersInteractorImpl(executor, foldersRepository, resourceManager)
    }

    @Provides fun provideGetMessagesInteractor(executor: Executor, messagesRepository: MessagesRepository) : GetMessagesInteractor
    {
        return GetMessagesInteractorImpl(executor, messagesRepository)
    }

    @Provides fun provideOpenFolderInteractor(executor: Executor, appStateManager: AppStateManager, uiManager: UIManager) : OpenFolderInteractor
    {
        return OpenFolderInteractorImpl(executor, appStateManager, uiManager)
    }

    @Provides fun provideOpenMessageInteractor(executor: Executor, appStateManager: AppStateManager, uiManager: UIManager, downloadManager: DownloadManager,
                                               fileCacheManager: FileCacheManager, messagesRepository: MessagesRepository) : OpenMessageInteractor
    {
        return OpenMessageInteractorImpl(executor, appStateManager, uiManager, downloadManager, fileCacheManager, messagesRepository)
    }

    @Provides fun provideOpenAttachmentInteractor(executor: Executor, appStateManager: AppStateManager, uiManager: UIManager, downloadManager: DownloadManager,
                                                  fileCacheManager: FileCacheManager) : OpenAttachmentInteractor
    {
        return OpenAttachmentInteractorImpl(executor, appStateManager, uiManager, downloadManager, fileCacheManager)
    }

    @Provides fun provideSaveAttachmentInteractor(executor: Executor, appStateManager: AppStateManager,
                                                  fileCacheManager: FileCacheManager, permissionManager: PermissionManager) : SaveAttachmentInteractor
    {
        return SaveAttachmentInteractorImpl(executor, appStateManager, fileCacheManager, permissionManager)
    }

    @Provides fun provideGetChannelsInteractor(executor: Executor, channelsRepository: ChannelsRepository) : GetChannelsInteractor
    {
        return GetChannelsInteractorImpl(executor, channelsRepository)
    }

    @Provides fun provideCreateUserInteractor(executor: Executor, userManager: UserManager) : CreateUserInteractor
    {
        return CreateUserInteractorImpl(executor, userManager)
    }

    @Provides fun provideGetUsersInteractor(executor: Executor, userManager: UserManager) : GetUsersInteractor
    {
        return GetUsersInteractorImpl(executor, userManager)
    }

    @Provides
    fun provideGetSenderCategoriesInteractor(executor: Executor) : GetSenderCategoriesInteractor {
        return GetSenderCategoriesInteractorImpl(executor)
    }

    @Provides
    fun provideSearchSendersInterActor(executor: Executor) : SearchSendersInteractor {
        return SearchSendersInteractorImpl(executor)
    }
}