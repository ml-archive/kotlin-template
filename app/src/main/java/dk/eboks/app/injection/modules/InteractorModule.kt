package dk.eboks.app.injection.modules

import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.interactors.BootstrapInteractor
import dk.eboks.app.domain.interactors.BootstrapInteractorImpl
import dk.eboks.app.domain.interactors.LoginInteractor
import dk.eboks.app.domain.interactors.LoginInteractorImpl
import dk.eboks.app.domain.managers.GuidManager
import dk.eboks.app.domain.repositories.SettingsRepository
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.Executor

/**
 * Created by bison on 09/10/17.
 */
@Module
class InteractorModule {
    @Provides fun provideLoginInteractor(executor: Executor, api: Api) : LoginInteractor
    {
        return LoginInteractorImpl(executor, api)
    }

    @Provides fun provideBootstrapInteractor(executor: Executor, api: Api, guidManager: GuidManager, settingsRepository: SettingsRepository) : BootstrapInteractor
    {
        return BootstrapInteractorImpl(executor, api, guidManager, settingsRepository)
    }
}