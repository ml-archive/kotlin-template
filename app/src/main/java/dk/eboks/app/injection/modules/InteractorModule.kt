package dk.eboks.app.injection.modules

import dagger.Binds
import dagger.Module
import dk.eboks.app.domain.interactors.BootstrapInteractor
import dk.eboks.app.domain.interactors.BootstrapInteractorImpl
import dk.eboks.app.domain.interactors.ChannelsBindingInteractorModule
import dk.eboks.app.domain.senders.injection.SendersBindingInteractorModule
import dk.eboks.app.keychain.injection.KeychainBindingInteractorsModule
import dk.eboks.app.mail.domain.interactors.MailBindingInteractorModule
import dk.eboks.app.profile.injection.ProfileBindingInteractorsModule

@Module(
        includes = [
            MailBindingInteractorModule::class,
            KeychainBindingInteractorsModule::class,
            ChannelsBindingInteractorModule::class,
            ProfileBindingInteractorsModule::class,
            CoreBindingInteractorsModule::class,
            SendersBindingInteractorModule::class
        ]
)
abstract class InteractorModule {
    @Binds
    abstract fun bindBootstrapInteractor(interactor: BootstrapInteractorImpl): BootstrapInteractor
}