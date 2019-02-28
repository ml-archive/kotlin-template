package dk.eboks.app.injection.modules

import dagger.Binds
import dagger.Module
import dk.eboks.app.domain.interactors.BootstrapInteractor
import dk.eboks.app.domain.interactors.BootstrapInteractorImpl
import dk.eboks.app.domain.interactors.ChannelsBindingInteractorModule
import dk.eboks.app.domain.senders.injection.SendersBindingInteractorModule
import dk.eboks.app.keychain.injection.KeychainInteractorsModule
import dk.eboks.app.mail.domain.interactors.MailBindingInteractorModule
import dk.eboks.app.profile.injection.ProfileInteractorsModule

@Module(
    includes = [
        MailBindingInteractorModule::class,
        KeychainInteractorsModule::class,
        ChannelsBindingInteractorModule::class,
        ProfileInteractorsModule::class,
        CoreInteractorsModule::class,
        SendersBindingInteractorModule::class
    ]
)
abstract class InteractorModule {
    @Binds
    abstract fun bindBootstrapInteractor(interactor: BootstrapInteractorImpl): BootstrapInteractor
}