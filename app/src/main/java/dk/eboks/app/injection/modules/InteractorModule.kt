package dk.eboks.app.injection.modules

import dagger.Binds
import dagger.Module
import dk.eboks.app.domain.interactors.BootstrapInteractor
import dk.eboks.app.domain.interactors.BootstrapInteractorImpl
import dk.eboks.app.mail.domain.interactors.GetCategoriesInteractor
import dk.eboks.app.domain.interactors.GetMailCategoriesInteractorImpl
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
import dk.eboks.app.keychain.interactors.authentication.TestLoginInteractor
import dk.eboks.app.keychain.interactors.authentication.TestLoginInteractorImpl
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
import dk.eboks.app.domain.interactors.channel.GetChannelContentLinkInteractor
import dk.eboks.app.domain.interactors.channel.GetChannelContentLinkInteractorImpl
import dk.eboks.app.domain.interactors.channel.GetChannelHomeContentInteractor
import dk.eboks.app.domain.interactors.channel.GetChannelHomeContentInteractorImpl
import dk.eboks.app.domain.interactors.channel.GetChannelInteractor
import dk.eboks.app.domain.interactors.channel.GetChannelInteractorImpl
import dk.eboks.app.domain.interactors.channel.GetChannelsInteractor
import dk.eboks.app.domain.interactors.channel.GetChannelsInteractorImpl
import dk.eboks.app.domain.interactors.channel.InstallChannelInteractor
import dk.eboks.app.domain.interactors.channel.InstallChannelInteractorImpl
import dk.eboks.app.domain.interactors.channel.UninstallChannelInteractor
import dk.eboks.app.domain.interactors.channel.UninstallChannelInteractorImpl
import dk.eboks.app.domain.interactors.ekey.DeleteEKeyMasterkeyInteractor
import dk.eboks.app.domain.interactors.ekey.DeleteEKeyMasterkeyInteractorImpl
import dk.eboks.app.domain.interactors.ekey.DeleteEKeyVaultInteractor
import dk.eboks.app.domain.interactors.ekey.DeleteEKeyVaultInteractorImpl
import dk.eboks.app.domain.interactors.ekey.GetEKeyMasterkeyInteractor
import dk.eboks.app.domain.interactors.ekey.GetEKeyMasterkeyInteractorImpl
import dk.eboks.app.domain.interactors.ekey.GetEKeyVaultInteractor
import dk.eboks.app.domain.interactors.ekey.GetEKeyVaultInteractorImpl
import dk.eboks.app.domain.interactors.ekey.SetEKeyMasterkeyInteractor
import dk.eboks.app.domain.interactors.ekey.SetEKeyMasterkeyInteractorImpl
import dk.eboks.app.domain.interactors.ekey.SetEKeyVaultInteractor
import dk.eboks.app.domain.interactors.ekey.SetEKeyVaultInteractorImpl
import dk.eboks.app.keychain.interactors.encryption.DecryptUserLoginInfoInteractor
import dk.eboks.app.keychain.interactors.encryption.DecryptUserLoginInfoInteractorImpl
import dk.eboks.app.keychain.interactors.encryption.EncryptUserLoginInfoInteractor
import dk.eboks.app.keychain.interactors.encryption.EncryptUserLoginInfoInteractorImpl
import dk.eboks.app.mail.domain.interactors.folder.CreateFolderInteractor
import dk.eboks.app.mail.domain.interactors.folder.CreateFolderInteractorImpl
import dk.eboks.app.mail.domain.interactors.folder.DeleteFolderInteractor
import dk.eboks.app.mail.domain.interactors.folder.DeleteFolderInteractorImpl
import dk.eboks.app.mail.domain.interactors.folder.EditFolderInteractor
import dk.eboks.app.mail.domain.interactors.folder.EditFolderInteractorImpl
import dk.eboks.app.mail.domain.interactors.folder.GetFoldersInteractor
import dk.eboks.app.mail.domain.interactors.folder.GetFoldersInteractorImpl
import dk.eboks.app.mail.domain.interactors.folder.OpenFolderInteractor
import dk.eboks.app.mail.domain.interactors.folder.OpenFolderInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.GetLatestUploadsInteractor
import dk.eboks.app.mail.domain.interactors.message.GetLatestUploadsInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.GetMessagesInteractor
import dk.eboks.app.mail.domain.interactors.message.GetMessagesInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.GetReplyFormInteractor
import dk.eboks.app.mail.domain.interactors.message.GetReplyFormInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.GetSignLinkInteractor
import dk.eboks.app.mail.domain.interactors.message.GetSignLinkInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.GetStorageInteractor
import dk.eboks.app.mail.domain.interactors.message.GetStorageInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.OpenAttachmentInteractor
import dk.eboks.app.mail.domain.interactors.message.OpenAttachmentInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.OpenMessageInteractor
import dk.eboks.app.mail.domain.interactors.message.OpenMessageInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.SaveAttachmentInteractor
import dk.eboks.app.mail.domain.interactors.message.SaveAttachmentInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.SubmitReplyFormInteractor
import dk.eboks.app.mail.domain.interactors.message.SubmitReplyFormInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.UploadFileInteractor
import dk.eboks.app.mail.domain.interactors.message.UploadFileInteractorImpl
import dk.eboks.app.mail.domain.interactors.messageoperations.DeleteMessagesInteractor
import dk.eboks.app.mail.domain.interactors.messageoperations.DeleteMessagesInteractorImpl
import dk.eboks.app.mail.domain.interactors.messageoperations.MoveMessagesInteractor
import dk.eboks.app.mail.domain.interactors.messageoperations.MoveMessagesInteractorImpl
import dk.eboks.app.mail.domain.interactors.messageoperations.UpdateMessageInteractor
import dk.eboks.app.mail.domain.interactors.messageoperations.UpdateMessageInteractorImpl
import dk.eboks.app.domain.interactors.sender.GetCollectionsInteractor
import dk.eboks.app.domain.interactors.sender.GetCollectionsInteractorImpl
import dk.eboks.app.domain.interactors.sender.GetSegmentInteractor
import dk.eboks.app.domain.interactors.sender.GetSegmentInteractorImpl
import dk.eboks.app.domain.interactors.sender.GetSenderCategoriesInteractor
import dk.eboks.app.domain.interactors.sender.GetSenderCategoriesInteractorImpl
import dk.eboks.app.domain.interactors.sender.GetSenderDetailInteractor
import dk.eboks.app.domain.interactors.sender.GetSenderDetailInteractorImpl
import dk.eboks.app.mail.domain.interactors.senders.GetSendersInteractor
import dk.eboks.app.mail.domain.interactors.senders.GetSendersInteractorImpl
import dk.eboks.app.domain.interactors.sender.register.GetPendingInteractor
import dk.eboks.app.domain.interactors.sender.register.GetPendingInteractorImpl
import dk.eboks.app.domain.interactors.sender.register.GetRegistrationsInteractor
import dk.eboks.app.domain.interactors.sender.register.GetRegistrationsInteractorImpl
import dk.eboks.app.domain.interactors.sender.register.RegisterInteractor
import dk.eboks.app.domain.interactors.sender.register.RegisterInteractorImpl
import dk.eboks.app.domain.interactors.sender.register.UnRegisterInteractor
import dk.eboks.app.domain.interactors.sender.register.UnRegisterInteractorImpl
import dk.eboks.app.mail.domain.interactors.shares.GetAllSharesInteractor
import dk.eboks.app.mail.domain.interactors.shares.GetAllSharesInteractorImpl
import dk.eboks.app.keychain.interactors.signup.CheckSignupMailInteractor
import dk.eboks.app.keychain.interactors.signup.CheckSignupMailInteractorImpl
import dk.eboks.app.domain.interactors.storebox.ConfirmStoreboxInteractor
import dk.eboks.app.domain.interactors.storebox.ConfirmStoreboxInteractorImpl
import dk.eboks.app.domain.interactors.storebox.CreateStoreboxInteractor
import dk.eboks.app.domain.interactors.storebox.CreateStoreboxInteractorImpl
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxAccountLinkInteractor
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxAccountLinkInteractorImpl
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxCreditCardInteractor
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxCreditCardInteractorImpl
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxReceiptInteractor
import dk.eboks.app.domain.interactors.storebox.DeleteStoreboxReceiptInteractorImpl
import dk.eboks.app.domain.interactors.storebox.GetStoreboxCardLinkInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxCardLinkInteractorImpl
import dk.eboks.app.domain.interactors.storebox.GetStoreboxCreditCardsInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxCreditCardsInteractorImpl
import dk.eboks.app.domain.interactors.storebox.GetStoreboxProfileInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxProfileInteractorImpl
import dk.eboks.app.domain.interactors.storebox.GetStoreboxReceiptInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxReceiptInteractorImpl
import dk.eboks.app.domain.interactors.storebox.GetStoreboxReceiptsInteractor
import dk.eboks.app.domain.interactors.storebox.GetStoreboxReceiptsInteractorImpl
import dk.eboks.app.domain.interactors.storebox.LinkStoreboxInteractor
import dk.eboks.app.domain.interactors.storebox.LinkStoreboxInteractorImpl
import dk.eboks.app.domain.interactors.storebox.PutStoreboxProfileInteractor
import dk.eboks.app.domain.interactors.storebox.PutStoreboxProfileInteractorImpl
import dk.eboks.app.domain.interactors.storebox.SaveReceiptInteractor
import dk.eboks.app.domain.interactors.storebox.SaveReceiptInteractorImpl
import dk.eboks.app.domain.interactors.storebox.ShareReceiptInteractor
import dk.eboks.app.domain.interactors.storebox.ShareReceiptInteractorImpl
import dk.eboks.app.domain.interactors.storebox.UpdateStoreboxFlagsInteractor
import dk.eboks.app.domain.interactors.storebox.UpdateStoreboxFlagsInteractorImpl
import dk.eboks.app.domain.interactors.user.CheckSsnExistsInteractor
import dk.eboks.app.domain.interactors.user.CheckSsnExistsInteractorImpl
import dk.eboks.app.domain.interactors.user.ConfirmPhoneInteractor
import dk.eboks.app.domain.interactors.user.ConfirmPhoneInteractorImpl
import dk.eboks.app.domain.interactors.user.CreateUserInteractor
import dk.eboks.app.domain.interactors.user.CreateUserInteractorImpl
import dk.eboks.app.keychain.interactors.user.DeleteUserInteractor
import dk.eboks.app.keychain.interactors.user.DeleteUserInteractorImpl
import dk.eboks.app.domain.interactors.user.GetUserProfileInteractor
import dk.eboks.app.domain.interactors.user.GetUserProfileInteractorImpl
import dk.eboks.app.keychain.interactors.user.GetUsersInteractor
import dk.eboks.app.keychain.interactors.user.GetUsersInteractorImpl
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

@Module
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
    abstract fun bindGetCategoriesInteractor(interactor: GetMailCategoriesInteractorImpl): GetCategoriesInteractor

    @Binds
    abstract fun bindGetFoldersInteractor(interactor: GetFoldersInteractorImpl): GetFoldersInteractor

    @Binds
    abstract fun bindDeleteMessagesInteractor(interactor: DeleteMessagesInteractorImpl): DeleteMessagesInteractor

    @Binds
    abstract fun bindMoveMessagesInteractor(interactor: MoveMessagesInteractorImpl): MoveMessagesInteractor

    @Binds
    abstract fun bindGetMessagesInteractor(interactor: GetMessagesInteractorImpl): GetMessagesInteractor

    @Binds
    abstract fun bindGetStorageInteractor(interactor: GetStorageInteractorImpl): GetStorageInteractor

    @Binds
    abstract fun bindGetLatestUploadsInteractor(interactor: GetLatestUploadsInteractorImpl): GetLatestUploadsInteractor

    @Binds
    abstract fun bindUploadFileInteractor(interactor: UploadFileInteractorImpl): UploadFileInteractor

    @Binds
    abstract fun bindGetStoreboxCreditCardsInteractor(interactor: GetStoreboxCreditCardsInteractorImpl): GetStoreboxCreditCardsInteractor

    @Binds
    abstract fun bindDeleteStoreboxCreditCardInteractor(interactor: DeleteStoreboxCreditCardInteractorImpl): DeleteStoreboxCreditCardInteractor

    @Binds
    abstract fun bindOpenFolderInteractor(interactor: OpenFolderInteractorImpl): OpenFolderInteractor

    @Binds
    abstract fun bindOpenMessageInteractor(interactor: OpenMessageInteractorImpl): OpenMessageInteractor

    @Binds
    abstract fun bindOpenAttachmentInteractor(interactor: OpenAttachmentInteractorImpl): OpenAttachmentInteractor

    @Binds
    abstract fun bindGetReplyFormInteractor(interactor: GetReplyFormInteractorImpl): GetReplyFormInteractor

    @Binds
    abstract fun bindSubmitReplyFormInteractor(interactor: SubmitReplyFormInteractorImpl): SubmitReplyFormInteractor

    @Binds
    abstract fun bindSaveAttachmentInteractor(interactor: SaveAttachmentInteractorImpl): SaveAttachmentInteractor

    @Binds
    abstract fun bindGetChannelsInteractor(interactor: GetChannelsInteractorImpl): GetChannelsInteractor

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
    abstract fun bindUpdateMessageInteractor(interactor: UpdateMessageInteractorImpl): UpdateMessageInteractor

    @Binds
    abstract fun bindGetChannelInteractor(interactor: GetChannelInteractorImpl): GetChannelInteractor

    @Binds
    abstract fun bindInstallChannelInteractor(interactor: InstallChannelInteractorImpl): InstallChannelInteractor

    @Binds
    abstract fun bindUninstallChannelInteractor(interactor: UninstallChannelInteractorImpl): UninstallChannelInteractor

    @Binds
    abstract fun bindGetChannelHomeContentInteractor(interactor: GetChannelHomeContentInteractorImpl): GetChannelHomeContentInteractor

    @Binds
    abstract fun bindGetChannelContentLinkInteractor(interactor: GetChannelContentLinkInteractorImpl): GetChannelContentLinkInteractor

    @Binds
    abstract fun bindGetSenderCategoriesInteractor(interactor: GetSenderCategoriesInteractorImpl): GetSenderCategoriesInteractor

    @Binds
    abstract fun bindGetSendersInteractor(interactor: GetSendersInteractorImpl): GetSendersInteractor

    @Binds
    abstract fun bindGetSenderDetailInteractor(interactor: GetSenderDetailInteractorImpl): GetSenderDetailInteractor

    @Binds
    abstract fun bindGetStoreboxReceiptsInteractor(interactor: GetStoreboxReceiptsInteractorImpl): GetStoreboxReceiptsInteractor

    @Binds
    abstract fun bindGetStoreboxReceiptInteractor(interactor: GetStoreboxReceiptInteractorImpl): GetStoreboxReceiptInteractor

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
    abstract fun bindLinkStoreboxInteractor(interactor: LinkStoreboxInteractorImpl): LinkStoreboxInteractor

    @Binds
    abstract fun bindCreateStoreboxInteractor(interactor: CreateStoreboxInteractorImpl): CreateStoreboxInteractor

    @Binds
    abstract fun bindGetStoreboxProfileInteractor(interactor: GetStoreboxProfileInteractorImpl): GetStoreboxProfileInteractor

    @Binds
    abstract fun bindPutStoreboxProfileInteractor(interactor: PutStoreboxProfileInteractorImpl): PutStoreboxProfileInteractor

    @Binds
    abstract fun bindGetStoreboxCardLinkInteractor(interactor: GetStoreboxCardLinkInteractorImpl): GetStoreboxCardLinkInteractor

    @Binds
    abstract fun bindDeleteStoreboxAccountLinkInteractor(interactor: DeleteStoreboxAccountLinkInteractorImpl): DeleteStoreboxAccountLinkInteractor

    @Binds
    abstract fun bindDeleteStoreboxReceiptInteractor(interactor: DeleteStoreboxReceiptInteractorImpl): DeleteStoreboxReceiptInteractor

    @Binds
    abstract fun bindUpdateStoreboxFlagsInteractor(interactor: UpdateStoreboxFlagsInteractorImpl): UpdateStoreboxFlagsInteractor

    @Binds
    abstract fun bindConfirmStoreboxInteractor(interactor: ConfirmStoreboxInteractorImpl): ConfirmStoreboxInteractor

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
    abstract fun bindGetSignLinkInteractor(interactor: GetSignLinkInteractorImpl): GetSignLinkInteractor

    // E Key Interactors

    @Binds
    abstract fun bindGetEKeyVaultInteractor(interactor: GetEKeyVaultInteractorImpl): GetEKeyVaultInteractor

    @Binds
    abstract fun bindSetEKeyVaultInteractor(interactor: SetEKeyVaultInteractorImpl): SetEKeyVaultInteractor

    @Binds
    abstract fun bindDeleteEKeyVaultInteractor(interactor: DeleteEKeyVaultInteractorImpl): DeleteEKeyVaultInteractor

    @Binds
    abstract fun bindGetEKeyMasterkeyInteractor(interactor: GetEKeyMasterkeyInteractorImpl): GetEKeyMasterkeyInteractor

    @Binds
    abstract fun bindSetEKeyMasterkeyInteractor(interactor: SetEKeyMasterkeyInteractorImpl): SetEKeyMasterkeyInteractor

    @Binds
    abstract fun bindDeleteEKeyMasterkeyInteractor(interactor: DeleteEKeyMasterkeyInteractorImpl): DeleteEKeyMasterkeyInteractor

    @Binds
    abstract fun bindSaveReceiptInteractor(interactor: SaveReceiptInteractorImpl): SaveReceiptInteractor

    @Binds
    abstract fun bindShareReceiptInteractor(interactor: ShareReceiptInteractorImpl): ShareReceiptInteractor

    @Binds
    abstract fun bindCheckRSAKeyPresenceInteractor(interactor: CheckRSAKeyPresenceInteractorImpl): CheckRSAKeyPresenceInteractor

    @Binds
    abstract fun bindGenerateRSAKey(interactor: GenerateRSAKeyInteractorImpl): GenerateRSAKeyInteractor

    @Binds
    abstract fun bindDeleteRSAKey(interactor: DeleteRSAKeyInteractorImpl): DeleteRSAKeyInteractor

    @Binds
    abstract fun bindActivateDevice(interactor: ActivateDeviceInteractorImpl): ActivateDeviceInteractor

    @Binds
    abstract fun bindCreateFolder(interactor: CreateFolderInteractorImpl): CreateFolderInteractor

    @Binds
    abstract fun bindDeleteFolder(interactor: DeleteFolderInteractorImpl): DeleteFolderInteractor

    @Binds
    abstract fun bindEditFolder(interactor: EditFolderInteractorImpl): EditFolderInteractor

    @Binds
    abstract fun bindGetAllSharesInteractor(interactor: GetAllSharesInteractorImpl): GetAllSharesInteractor
}