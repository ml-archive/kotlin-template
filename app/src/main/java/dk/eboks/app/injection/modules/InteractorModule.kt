package dk.eboks.app.injection.modules

import dagger.Binds
import dagger.Module
import dk.eboks.app.domain.interactors.BootstrapInteractor
import dk.eboks.app.domain.interactors.BootstrapInteractorImpl
import dk.eboks.app.domain.interactors.ChannelsBindingInteractorModule
import dk.eboks.app.domain.interactors.TestLoginInteractor
import dk.eboks.app.domain.interactors.TestLoginInteractorImpl
import dk.eboks.app.domain.interactors.ekey.DeleteEKeyMasterkeyInteractor
import dk.eboks.app.domain.interactors.ekey.DeleteEKeyMasterkeyInteractorImpl
import dk.eboks.app.domain.interactors.encryption.DecryptUserLoginInfoInteractor
import dk.eboks.app.domain.interactors.encryption.DecryptUserLoginInfoInteractorImpl
import dk.eboks.app.domain.interactors.encryption.EncryptUserLoginInfoInteractor
import dk.eboks.app.domain.interactors.encryption.EncryptUserLoginInfoInteractorImpl
import dk.eboks.app.domain.interactors.user.ConfirmPhoneInteractor
import dk.eboks.app.domain.interactors.user.ConfirmPhoneInteractorImpl
import dk.eboks.app.domain.interactors.user.GetUserProfileInteractor
import dk.eboks.app.domain.interactors.user.GetUserProfileInteractorImpl
import dk.eboks.app.domain.interactors.user.SaveUserInteractor
import dk.eboks.app.domain.interactors.user.SaveUserInteractorImpl
import dk.eboks.app.domain.interactors.user.SaveUserSettingsInteractor
import dk.eboks.app.domain.interactors.user.SaveUserSettingsInteractorImpl
import dk.eboks.app.domain.interactors.user.SaveUsersInteractor
import dk.eboks.app.domain.interactors.user.SaveUsersInteractorImpl
import dk.eboks.app.domain.interactors.user.UpdateUserInteractor
import dk.eboks.app.domain.interactors.user.UpdateUserInteractorImpl
import dk.eboks.app.domain.interactors.user.VerifyEmailInteractor
import dk.eboks.app.domain.interactors.user.VerifyEmailInteractorImpl
import dk.eboks.app.domain.interactors.user.VerifyPhoneInteractor
import dk.eboks.app.domain.interactors.user.VerifyPhoneInteractorImpl
import dk.eboks.app.domain.senders.injection.SendersBindingInteractorModule
import dk.eboks.app.keychain.injection.KeychainInteractorsModule
import dk.eboks.app.mail.domain.interactors.MailBindingInteractorModule

@Module(
    includes = [
        MailBindingInteractorModule::class,
        KeychainInteractorsModule::class,
        ChannelsBindingInteractorModule::class,
        SendersBindingInteractorModule::class
    ]
)
abstract class InteractorModule {

    @Binds
    abstract fun bindTestLoginInteractor(interactor: TestLoginInteractorImpl): TestLoginInteractor

    @Binds
    abstract fun bindDecryptInteractor(interactor: DecryptUserLoginInfoInteractorImpl): DecryptUserLoginInfoInteractor

    @Binds
    abstract fun bindVerifyEmailInteractor(interactor: VerifyEmailInteractorImpl): VerifyEmailInteractor

    @Binds
    abstract fun bindUpdateUserInteractor(interactor: UpdateUserInteractorImpl): UpdateUserInteractor

    @Binds
    abstract fun bindBootstrapInteractor(interactor: BootstrapInteractorImpl): BootstrapInteractor

    @Binds
    abstract fun bindSaveUserSettingsInteractor(interactor: SaveUserSettingsInteractorImpl): SaveUserSettingsInteractor

    @Binds
    abstract fun bindSaveUserInteractor(interactor: SaveUserInteractorImpl): SaveUserInteractor

    @Binds
    abstract fun bindSaveUsersInteractor(interactor: SaveUsersInteractorImpl): SaveUsersInteractor

    @Binds
    internal abstract fun bindGetUserProfileInteractor(interactor: GetUserProfileInteractorImpl): GetUserProfileInteractor

    @Binds
    internal abstract fun bindVerifyPhoneInteractor(interactor: VerifyPhoneInteractorImpl): VerifyPhoneInteractor

    @Binds
    internal abstract fun bindConfirmPhoneInteractor(interactor: ConfirmPhoneInteractorImpl): ConfirmPhoneInteractor

    // E Key Interactors

    @Binds
    internal abstract fun bindDeleteEKeyMasterkeyInteractor(interactor: DeleteEKeyMasterkeyInteractorImpl): DeleteEKeyMasterkeyInteractor

    @Binds
    internal abstract fun bindEncryptUserLoginInfoInteractor(interactor: EncryptUserLoginInfoInteractorImpl): EncryptUserLoginInfoInteractor
}