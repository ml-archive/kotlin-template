package dk.eboks.app.keychain.injection

import dagger.Binds
import dagger.Module
import dk.eboks.app.keychain.interactors.user.CheckSsnExistsInteractor
import dk.eboks.app.keychain.interactors.user.CreateUserInteractor
import dk.eboks.app.keychain.interactors.user.CreateUserInteractorImpl
import dk.eboks.app.keychain.interactors.authentication.CheckRSAKeyPresenceInteractor
import dk.eboks.app.keychain.interactors.authentication.CheckRSAKeyPresenceInteractorImpl
import dk.eboks.app.keychain.interactors.authentication.LoginInteractor
import dk.eboks.app.keychain.interactors.authentication.LoginInteractorImpl
import dk.eboks.app.keychain.interactors.authentication.MergeAndImpersonateInteractor
import dk.eboks.app.keychain.interactors.authentication.MergeAndImpersonateInteractorImpl
import dk.eboks.app.keychain.interactors.authentication.ResetPasswordInteractor
import dk.eboks.app.keychain.interactors.authentication.ResetPasswordInteractorImpl
import dk.eboks.app.keychain.interactors.authentication.SetCurrentUserInteractor
import dk.eboks.app.keychain.interactors.authentication.SetCurrentUserInteractorImpl
import dk.eboks.app.keychain.interactors.authentication.TransformTokenInteractor
import dk.eboks.app.keychain.interactors.authentication.TransformTokenInteractorImpl
import dk.eboks.app.keychain.interactors.authentication.VerifyProfileInteractor
import dk.eboks.app.keychain.interactors.authentication.VerifyProfileInteractorImpl
import dk.eboks.app.keychain.interactors.mobileacces.ActivateDeviceInteractor
import dk.eboks.app.keychain.interactors.mobileacces.ActivateDeviceInteractorImpl
import dk.eboks.app.keychain.interactors.mobileacces.DeleteRSAKeyForUserInteractor
import dk.eboks.app.keychain.interactors.mobileacces.DeleteRSAKeyForUserInteractorImpl
import dk.eboks.app.keychain.interactors.mobileacces.DeleteRSAKeyInteractor
import dk.eboks.app.keychain.interactors.mobileacces.DeleteRSAKeyInteractorImpl
import dk.eboks.app.keychain.interactors.mobileacces.GenerateRSAKeyInteractor
import dk.eboks.app.keychain.interactors.mobileacces.GenerateRSAKeyInteractorImpl
import dk.eboks.app.keychain.interactors.signup.CheckSignupMailInteractor
import dk.eboks.app.keychain.interactors.signup.CheckSignupMailInteractorImpl
import dk.eboks.app.keychain.interactors.user.CheckSsnExistsInteractorImpl
import dk.eboks.app.keychain.interactors.user.DeleteUserInteractor
import dk.eboks.app.keychain.interactors.user.DeleteUserInteractorImpl
import dk.eboks.app.keychain.interactors.user.GetUsersInteractor
import dk.eboks.app.keychain.interactors.user.GetUsersInteractorImpl

@Module
abstract class KeychainBindingInteractorsModule {

    @Binds
    internal abstract fun bindTransformTokenInteractor(interactor: TransformTokenInteractorImpl): TransformTokenInteractor

    @Binds
    internal abstract fun bindMergeAndImpersonateInteractor(interactor: MergeAndImpersonateInteractorImpl): MergeAndImpersonateInteractor

    @Binds
    internal abstract fun bindVerifyProfileInteractor(interactor: VerifyProfileInteractorImpl): VerifyProfileInteractor

    @Binds
    internal abstract fun bindSetCurrentUserInteractor(interactor: SetCurrentUserInteractorImpl): SetCurrentUserInteractor

    @Binds
    internal abstract fun bindResetPasswordInteractor(interactor: ResetPasswordInteractorImpl): ResetPasswordInteractor

    @Binds
    internal abstract fun bindCheckRSAKeyPresenceInteractor(interactor: CheckRSAKeyPresenceInteractorImpl): CheckRSAKeyPresenceInteractor

    @Binds
    internal abstract fun bindGenerateRSAKey(interactor: GenerateRSAKeyInteractorImpl): GenerateRSAKeyInteractor

    @Binds
    internal abstract fun bindDeleteRSAKey(interactor: DeleteRSAKeyInteractorImpl): DeleteRSAKeyInteractor

    @Binds
    internal abstract fun bindActivateDevice(interactor: ActivateDeviceInteractorImpl): ActivateDeviceInteractor

    @Binds
    internal abstract fun bindDeleteRSAKeyForUserInteractor(interactor: DeleteRSAKeyForUserInteractorImpl): DeleteRSAKeyForUserInteractor

    @Binds
    internal abstract fun bindDeleteUserInteractor(interactor: DeleteUserInteractorImpl): DeleteUserInteractor

    @Binds
    internal abstract fun bindGetUsersInteractor(interactor: GetUsersInteractorImpl): GetUsersInteractor

    @Binds
    internal abstract fun bindCreateUserInteractor(interactor: CreateUserInteractorImpl): CreateUserInteractor

    @Binds
    internal abstract fun bindLoginInteractor(interactor: LoginInteractorImpl): LoginInteractor

    @Binds
    internal abstract fun bindCheckSsnExistsInteractor(interactor: CheckSsnExistsInteractorImpl): CheckSsnExistsInteractor

    @Binds
    internal abstract fun bindVerifyignupMailInteractor(interactor: CheckSignupMailInteractorImpl): CheckSignupMailInteractor
}
