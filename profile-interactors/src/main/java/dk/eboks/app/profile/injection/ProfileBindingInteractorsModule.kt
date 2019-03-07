package dk.eboks.app.profile.injection

import dagger.Binds
import dagger.Module
import dk.eboks.app.profile.interactors.SaveUserInteractor
import dk.eboks.app.profile.interactors.SaveUserInteractorImpl
import dk.eboks.app.profile.interactors.SaveUserSettingsInteractor
import dk.eboks.app.profile.interactors.SaveUsersInteractor
import dk.eboks.app.profile.interactors.SaveUsersInteractorImpl
import dk.eboks.app.profile.interactors.GetUserProfileInteractor
import dk.eboks.app.profile.interactors.GetUserProfileInteractorImpl
import dk.eboks.app.profile.interactors.VerifyEmailInteractor
import dk.eboks.app.profile.interactors.VerifyEmailInteractorImpl
import dk.eboks.app.profile.interactors.VerifyPhoneInteractor
import dk.eboks.app.profile.interactors.VerifyPhoneInteractorImpl
import dk.eboks.app.profile.interactors.SaveUserSettingsInteractorImpl
import dk.eboks.app.profile.interactors.UpdateUserInteractor
import dk.eboks.app.profile.interactors.UpdateUserInteractorImpl
import dk.eboks.app.profile.interactors.ConfirmPhoneInteractor
import dk.eboks.app.profile.interactors.ConfirmPhoneInteractorImpl

@Module
abstract class ProfileBindingInteractorsModule {

    @Binds
    internal abstract fun bindSaveUserSettingsInteractor(interactor: SaveUserSettingsInteractorImpl): SaveUserSettingsInteractor

    @Binds
    internal abstract fun bindSaveUserInteractor(interactor: SaveUserInteractorImpl): SaveUserInteractor

    @Binds
    internal abstract fun bindSaveUsersInteractor(interactor: SaveUsersInteractorImpl): SaveUsersInteractor

    @Binds
    internal abstract fun bindGetUserProfileInteractor(interactor: GetUserProfileInteractorImpl): GetUserProfileInteractor

    @Binds
    internal abstract fun bindVerifyEmailInteractor(interactor: VerifyEmailInteractorImpl): VerifyEmailInteractor

    @Binds
    internal abstract fun bindUpdateUserInteractor(interactor: UpdateUserInteractorImpl): UpdateUserInteractor

    @Binds
    internal abstract fun bindVerifyPhoneInteractor(interactor: VerifyPhoneInteractorImpl): VerifyPhoneInteractor

    @Binds
    internal abstract fun bindConfirmPhoneInteractor(interactor: ConfirmPhoneInteractorImpl): ConfirmPhoneInteractor
}