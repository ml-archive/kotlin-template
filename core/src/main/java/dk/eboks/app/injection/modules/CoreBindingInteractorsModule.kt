package dk.eboks.app.injection.modules

import dagger.Binds
import dagger.Module
import dk.eboks.app.domain.interactors.TestLoginInteractor
import dk.eboks.app.domain.interactors.TestLoginInteractorImpl
import dk.eboks.app.domain.interactors.encryption.DecryptUserLoginInfoInteractor
import dk.eboks.app.domain.interactors.encryption.DecryptUserLoginInfoInteractorImpl
import dk.eboks.app.domain.interactors.encryption.EncryptUserLoginInfoInteractor
import dk.eboks.app.domain.interactors.encryption.EncryptUserLoginInfoInteractorImpl

@Module
abstract class CoreBindingInteractorsModule {

    @Binds
    internal abstract fun bindEncryptUserLoginInfoInteractor(interactor: EncryptUserLoginInfoInteractorImpl): EncryptUserLoginInfoInteractor

    @Binds
    internal abstract fun bindTestLoginInteractor(interactor: TestLoginInteractorImpl): TestLoginInteractor

    @Binds
    internal abstract fun bindDecryptInteractor(interactor: DecryptUserLoginInfoInteractorImpl): DecryptUserLoginInfoInteractor
}