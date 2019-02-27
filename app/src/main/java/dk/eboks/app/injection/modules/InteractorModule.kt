package dk.eboks.app.injection.modules

import dagger.Binds
import dagger.Module
import dk.eboks.app.domain.interactors.BootstrapInteractor
import dk.eboks.app.domain.interactors.BootstrapInteractorImpl
import dk.eboks.app.domain.interactors.ChannelsBindingInteractorModule
import dk.eboks.app.domain.interactors.authentication.CheckRSAKeyPresenceInteractor
import dk.eboks.app.domain.interactors.authentication.CheckRSAKeyPresenceInteractorImpl
import dk.eboks.app.domain.interactors.authentication.LoginInteractor
import dk.eboks.app.domain.interactors.authentication.LoginInteractorImpl
import dk.eboks.app.domain.interactors.authentication.MergeAndImpersonateInteractor
import dk.eboks.app.domain.interactors.authentication.MergeAndImpersonateInteractorImpl
import dk.eboks.app.domain.interactors.authentication.ResetPasswordInteractor
import dk.eboks.app.domain.interactors.authentication.ResetPasswordInteractorImpl
import dk.eboks.app.domain.interactors.authentication.SetCurrentUserInteractor
import dk.eboks.app.domain.interactors.authentication.SetCurrentUserInteractorImpl
import dk.eboks.app.domain.interactors.authentication.TestLoginInteractor
import dk.eboks.app.domain.interactors.authentication.TestLoginInteractorImpl
import dk.eboks.app.domain.interactors.authentication.TransformTokenInteractor
import dk.eboks.app.domain.interactors.authentication.TransformTokenInteractorImpl
import dk.eboks.app.domain.interactors.authentication.VerifyProfileInteractor
import dk.eboks.app.domain.interactors.authentication.VerifyProfileInteractorImpl
import dk.eboks.app.domain.interactors.authentication.mobileacces.ActivateDeviceInteractor
import dk.eboks.app.domain.interactors.authentication.mobileacces.ActivateDeviceInteractorImpl
import dk.eboks.app.domain.interactors.authentication.mobileacces.DeleteRSAKeyForUserInteractor
import dk.eboks.app.domain.interactors.authentication.mobileacces.DeleteRSAKeyForUserInteractorImpl
import dk.eboks.app.domain.interactors.authentication.mobileacces.DeleteRSAKeyInteractor
import dk.eboks.app.domain.interactors.authentication.mobileacces.DeleteRSAKeyInteractorImpl
import dk.eboks.app.domain.interactors.authentication.mobileacces.GenerateRSAKeyInteractor
import dk.eboks.app.domain.interactors.authentication.mobileacces.GenerateRSAKeyInteractorImpl
import dk.eboks.app.domain.interactors.encryption.DecryptUserLoginInfoInteractor
import dk.eboks.app.domain.interactors.encryption.DecryptUserLoginInfoInteractorImpl
import dk.eboks.app.domain.interactors.encryption.EncryptUserLoginInfoInteractor
import dk.eboks.app.domain.interactors.encryption.EncryptUserLoginInfoInteractorImpl
import dk.eboks.app.domain.interactors.sender.GetCollectionsInteractor
import dk.eboks.app.domain.interactors.sender.GetCollectionsInteractorImpl
import dk.eboks.app.domain.interactors.sender.GetSegmentInteractor
import dk.eboks.app.domain.interactors.sender.GetSegmentInteractorImpl
import dk.eboks.app.domain.interactors.sender.GetSenderCategoriesInteractor
import dk.eboks.app.domain.interactors.sender.GetSenderCategoriesInteractorImpl
import dk.eboks.app.domain.interactors.sender.GetSenderDetailInteractor
import dk.eboks.app.domain.interactors.sender.GetSenderDetailInteractorImpl
import dk.eboks.app.domain.interactors.sender.register.GetPendingInteractor
import dk.eboks.app.domain.interactors.sender.register.GetPendingInteractorImpl
import dk.eboks.app.domain.interactors.sender.register.GetRegistrationsInteractor
import dk.eboks.app.domain.interactors.sender.register.GetRegistrationsInteractorImpl
import dk.eboks.app.domain.interactors.sender.register.RegisterInteractor
import dk.eboks.app.domain.interactors.sender.register.RegisterInteractorImpl
import dk.eboks.app.domain.interactors.sender.register.UnRegisterInteractor
import dk.eboks.app.domain.interactors.sender.register.UnRegisterInteractorImpl
import dk.eboks.app.domain.interactors.signup.CheckSignupMailInteractor
import dk.eboks.app.domain.interactors.signup.CheckSignupMailInteractorImpl
import dk.eboks.app.domain.interactors.user.CheckSsnExistsInteractor
import dk.eboks.app.domain.interactors.user.CheckSsnExistsInteractorImpl
import dk.eboks.app.domain.interactors.user.ConfirmPhoneInteractor
import dk.eboks.app.domain.interactors.user.ConfirmPhoneInteractorImpl
import dk.eboks.app.domain.interactors.user.CreateUserInteractor
import dk.eboks.app.domain.interactors.user.CreateUserInteractorImpl
import dk.eboks.app.domain.interactors.user.DeleteUserInteractor
import dk.eboks.app.domain.interactors.user.DeleteUserInteractorImpl
import dk.eboks.app.domain.interactors.user.GetUserProfileInteractor
import dk.eboks.app.domain.interactors.user.GetUserProfileInteractorImpl
import dk.eboks.app.domain.interactors.user.GetUsersInteractor
import dk.eboks.app.domain.interactors.user.GetUsersInteractorImpl
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
import dk.eboks.app.mail.injection.MailBindingInteractorModule

@Module(
    includes = [
        MailBindingInteractorModule::class,
        ChannelsBindingInteractorModule::class
    ]
)
abstract class InteractorModule {
    @Binds
    abstract fun bindLoginInteractor(interactor: LoginInteractorImpl): LoginInteractor

    @Binds
    abstract fun bindTestLoginInteractor(interactor: TestLoginInteractorImpl): TestLoginInteractor

    @Binds
    abstract fun bindDecryptInteractor(interactor: DecryptUserLoginInfoInteractorImpl): DecryptUserLoginInfoInteractor

    @Binds
    abstract fun bindCheckSsnExistsInteractor(interactor: CheckSsnExistsInteractorImpl): CheckSsnExistsInteractor

    @Binds
    abstract fun bindVerifyignupMailInteractor(interactor: CheckSignupMailInteractorImpl): CheckSignupMailInteractor

    @Binds
    abstract fun bindVerifyEmailInteractor(interactor: VerifyEmailInteractorImpl): VerifyEmailInteractor

    @Binds
    abstract fun bindUpdateUserInteractor(interactor: UpdateUserInteractorImpl): UpdateUserInteractor

    @Binds
    abstract fun bindBootstrapInteractor(interactor: BootstrapInteractorImpl): BootstrapInteractor

    @Binds

    abstract fun bindCreateUserInteractor(interactor: CreateUserInteractorImpl): CreateUserInteractor

    @Binds
    abstract fun bindSaveUserSettingsInteractor(interactor: SaveUserSettingsInteractorImpl): SaveUserSettingsInteractor

    @Binds
    abstract fun bindSaveUserInteractor(interactor: SaveUserInteractorImpl): SaveUserInteractor

    @Binds
    abstract fun bindSaveUsersInteractor(interactor: SaveUsersInteractorImpl): SaveUsersInteractor

    @Binds
    abstract fun bindDeleteRSAKeyForUserInteractor(interactor: DeleteRSAKeyForUserInteractorImpl): DeleteRSAKeyForUserInteractor

    @Binds
    abstract fun bindDeleteUserInteractor(interactor: DeleteUserInteractorImpl): DeleteUserInteractor

    @Binds
    abstract fun bindGetUsersInteractor(interactor: GetUsersInteractorImpl): GetUsersInteractor

    @Binds
    abstract fun bindGetUserProfileInteractor(interactor: GetUserProfileInteractorImpl): GetUserProfileInteractor

    @Binds
    abstract fun bindGetSenderCategoriesInteractor(interactor: GetSenderCategoriesInteractorImpl): GetSenderCategoriesInteractor

    @Binds
    abstract fun bindGetSenderDetailInteractor(interactor: GetSenderDetailInteractorImpl): GetSenderDetailInteractor

    @Binds
    abstract fun bindGetSegmentDetailInteractor(interactor: GetSegmentInteractorImpl): GetSegmentInteractor

    @Binds
    abstract fun bindGetPendingInteractor(interactor: GetPendingInteractorImpl): GetPendingInteractor

    @Binds
    abstract fun bindEncryptUserLoginInfoInteractor(interactor: EncryptUserLoginInfoInteractorImpl): EncryptUserLoginInfoInteractor

    @Binds
    abstract fun bindGetCollectionsInteractor(interactor: GetCollectionsInteractorImpl): GetCollectionsInteractor

    @Binds
    abstract fun bindRegisterInteractor(interactor: RegisterInteractorImpl): RegisterInteractor

    @Binds
    abstract fun bindUnRegisterInteractor(interactor: UnRegisterInteractorImpl): UnRegisterInteractor

    @Binds
    abstract fun bindRegistrationsInteractor(interactor: GetRegistrationsInteractorImpl): GetRegistrationsInteractor

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
    abstract fun bindVerifyPhoneInteractor(interactor: VerifyPhoneInteractorImpl): VerifyPhoneInteractor

    @Binds
    abstract fun bindConfirmPhoneInteractor(interactor: ConfirmPhoneInteractorImpl): ConfirmPhoneInteractor

    @Binds
    abstract fun bindCheckRSAKeyPresenceInteractor(interactor: CheckRSAKeyPresenceInteractorImpl): CheckRSAKeyPresenceInteractor

    @Binds
    abstract fun bindGenerateRSAKey(interactor: GenerateRSAKeyInteractorImpl): GenerateRSAKeyInteractor

    @Binds
    abstract fun bindDeleteRSAKey(interactor: DeleteRSAKeyInteractorImpl): DeleteRSAKeyInteractor

    @Binds
    abstract fun bindActivateDevice(interactor: ActivateDeviceInteractorImpl): ActivateDeviceInteractor
}