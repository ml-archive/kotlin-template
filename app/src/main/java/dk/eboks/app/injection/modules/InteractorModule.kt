package dk.eboks.app.injection.modules

import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.interactors.*
import dk.eboks.app.domain.managers.GuidManager
import dk.eboks.app.domain.managers.ProtocolManager
import dk.eboks.app.domain.repositories.SendersRepository
import dk.eboks.app.domain.repositories.SettingsRepository
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

    @Provides fun provideBootstrapInteractor(executor: Executor, guidManager: GuidManager, settingsRepository: SettingsRepository, protocolManager: ProtocolManager) : BootstrapInteractor
    {
        return BootstrapInteractorImpl(executor, guidManager, settingsRepository, protocolManager)
    }

    @Provides fun provideGetSendersInteractor(executor: Executor, sendersRepository: SendersRepository) : GetSendersInteractor
    {
        return GetSendersInteractorImpl(executor, sendersRepository)
    }

}