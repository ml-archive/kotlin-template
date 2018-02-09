package dk.eboks.app.injection.modules

import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.interactors.*
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.GuidManager
import dk.eboks.app.domain.managers.ProtocolManager
import dk.eboks.app.domain.managers.ResourceManager
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
}