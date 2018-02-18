package dk.eboks.app.injection.modules

import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.interactors.*
import dk.eboks.app.domain.interactors.folder.GetFoldersInteractor
import dk.eboks.app.domain.interactors.folder.GetFoldersInteractorImpl
import dk.eboks.app.domain.interactors.folder.OpenFolderInteractor
import dk.eboks.app.domain.interactors.folder.OpenFolderInteractorImpl
import dk.eboks.app.domain.interactors.message.GetMessagesInteractor
import dk.eboks.app.domain.interactors.message.GetMessagesInteractorImpl
import dk.eboks.app.domain.interactors.message.OpenMessageInteractor
import dk.eboks.app.domain.interactors.message.OpenMessageInteractorImpl
import dk.eboks.app.domain.interactors.sender.GetSendersInteractor
import dk.eboks.app.domain.interactors.sender.GetSendersInteractorImpl
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

    @Provides fun provideBootstrapInteractor(executor: Executor, guidManager: GuidManager, settingsRepository: SettingsRepository, protocolManager: ProtocolManager, appStateManager: AppStateManager) : BootstrapInteractor
    {
        return BootstrapInteractorImpl(executor, guidManager, settingsRepository, protocolManager, appStateManager)
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

    @Provides fun provideOpenMessageInteractor(executor: Executor, appStateManager: AppStateManager, uiManager: UIManager, downloadManager: DownloadManager, fileCacheManager: FileCacheManager) : OpenMessageInteractor
    {
        return OpenMessageInteractorImpl(executor, appStateManager, uiManager, downloadManager, fileCacheManager)
    }
}