package dk.eboks.app.profile.injection

import dagger.Binds
import dagger.Module
import dk.eboks.app.profile.interactors.*

@Module
abstract class ProfileInteractorsModule {


    @Binds
    abstract fun bindSaveUserSettingsInteractor(interactor: SaveUserSettingsInteractorImpl): SaveUserSettingsInteractor

    @Binds
    abstract fun bindSaveUserInteractor(interactor: SaveUserInteractorImpl): SaveUserInteractor

    @Binds
    abstract fun bindSaveUsersInteractor(interactor: SaveUsersInteractorImpl): SaveUsersInteractor

    @Binds
    internal abstract fun bindGetUserProfileInteractor(interactor: GetUserProfileInteractorImpl): GetUserProfileInteractor

    @Binds
    abstract fun bindVerifyEmailInteractor(interactor: VerifyEmailInteractorImpl): VerifyEmailInteractor

    @Binds
    abstract fun bindUpdateUserInteractor(interactor: UpdateUserInteractorImpl): UpdateUserInteractor

    @Binds
    internal abstract fun bindVerifyPhoneInteractor(interactor: VerifyPhoneInteractorImpl): VerifyPhoneInteractor

    @Binds
    internal abstract fun bindConfirmPhoneInteractor(interactor: ConfirmPhoneInteractorImpl): ConfirmPhoneInteractor

}