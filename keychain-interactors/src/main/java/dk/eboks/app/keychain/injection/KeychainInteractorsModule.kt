package dk.eboks.app.keychain.injection

import dagger.Binds
import dagger.Module
import dk.eboks.app.domain.interactors.user.*
import dk.eboks.app.keychain.interactors.authentication.MergeAndImpersonateInteractor
import dk.eboks.app.keychain.interactors.authentication.MergeAndImpersonateInteractorImpl
import dk.eboks.app.keychain.interactors.authentication.TransformTokenInteractor
import dk.eboks.app.keychain.interactors.authentication.*
import dk.eboks.app.keychain.interactors.mobileacces.*
import dk.eboks.app.keychain.interactors.signup.CheckSignupMailInteractor
import dk.eboks.app.keychain.interactors.signup.*
import dk.eboks.app.keychain.interactors.user.*

@Module
abstract class KeychainInteractorsModule {


    @Binds
    abstract fun bindTransformTokenInteractor(interactor: TransformTokenInteractorImpl): TransformTokenInteractor

    @Binds
    abstract fun bindMergeAndImpersonateInteractor(interactor: MergeAndImpersonateInteractorImpl): MergeAndImpersonateInteractor

    @Binds
    abstract fun bindVerifyProfileInteractor(interactor: VerifyProfileInteractorImpl): VerifyProfileInteractor

    @Binds
    abstract fun bindSetCurrentUserInteractor(interactor: SetCurrentUserInteractorImpl): SetCurrentUserInteractor

    @Binds
    abstract fun bindResetPasswordInteractor(interactor: ResetPasswordInteractorImpl): ResetPasswordInteractor

    @Binds
    abstract fun bindCheckRSAKeyPresenceInteractor(interactor: CheckRSAKeyPresenceInteractorImpl): CheckRSAKeyPresenceInteractor

    @Binds
    abstract fun bindGenerateRSAKey(interactor: GenerateRSAKeyInteractorImpl): GenerateRSAKeyInteractor

    @Binds
    abstract fun bindDeleteRSAKey(interactor: DeleteRSAKeyInteractorImpl): DeleteRSAKeyInteractor

    @Binds
    abstract fun bindActivateDevice(interactor: ActivateDeviceInteractorImpl): ActivateDeviceInteractor

    @Binds
    abstract fun bindDeleteRSAKeyForUserInteractor(interactor: DeleteRSAKeyForUserInteractorImpl): DeleteRSAKeyForUserInteractor

    @Binds
    abstract fun bindDeleteUserInteractor(interactor: DeleteUserInteractorImpl): DeleteUserInteractor

    @Binds
    abstract fun bindGetUsersInteractor(interactor: GetUsersInteractorImpl): GetUsersInteractor

    @Binds
    abstract fun bindCreateUserInteractor(interactor: CreateUserInteractorImpl): CreateUserInteractor


    @Binds
    abstract fun bindLoginInteractor(interactor: LoginInteractorImpl): LoginInteractor

    @Binds
    abstract fun bindCheckSsnExistsInteractor(interactor: CheckSsnExistsInteractorImpl): CheckSsnExistsInteractor

    @Binds
    abstract fun bindVerifyignupMailInteractor(interactor: CheckSignupMailInteractorImpl): CheckSignupMailInteractor

}
