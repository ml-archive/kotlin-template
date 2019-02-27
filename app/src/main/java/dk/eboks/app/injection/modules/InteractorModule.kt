package dk.eboks.app.injection.modules

import dagger.Binds
import dagger.Module
import dk.eboks.app.domain.interactors.BootstrapInteractor
import dk.eboks.app.domain.interactors.BootstrapInteractorImpl
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
import dk.eboks.app.keychain.injection.KeychainInteractorsModule
import dk.eboks.app.mail.injection.MailBindingInteractorModule

@Module(
    includes = [
        MailBindingInteractorModule::class,
        KeychainInteractorsModule::class
    ]
)
 abstract class InteractorModule {


    @Binds
    internal abstract fun bindVerifyEmailInteractor(interactor: VerifyEmailInteractorImpl): VerifyEmailInteractor

    @Binds
    internal abstract fun bindUpdateUserInteractor(interactor: UpdateUserInteractorImpl): UpdateUserInteractor

    @Binds
    internal abstract fun bindBootstrapInteractor(interactor: BootstrapInteractorImpl): BootstrapInteractor

    @Binds
    internal abstract fun bindGetStoreboxCreditCardsInteractor(interactor: GetStoreboxCreditCardsInteractorImpl): GetStoreboxCreditCardsInteractor

    @Binds
    internal abstract fun bindDeleteStoreboxCreditCardInteractor(interactor: DeleteStoreboxCreditCardInteractorImpl): DeleteStoreboxCreditCardInteractor

    @Binds
    internal abstract fun bindGetChannelsInteractor(interactor: GetChannelsInteractorImpl): GetChannelsInteractor



    @Binds
    internal abstract fun bindSaveUserSettingsInteractor(interactor: SaveUserSettingsInteractorImpl): SaveUserSettingsInteractor

    @Binds
    internal abstract fun bindSaveUserInteractor(interactor: SaveUserInteractorImpl): SaveUserInteractor

    @Binds
    internal abstract fun bindSaveUsersInteractor(interactor: SaveUsersInteractorImpl): SaveUsersInteractor



    @Binds
    internal abstract fun bindGetUserProfileInteractor(interactor: GetUserProfileInteractorImpl): GetUserProfileInteractor

    @Binds
    internal abstract fun bindGetChannelInteractor(interactor: GetChannelInteractorImpl): GetChannelInteractor

    @Binds
    internal abstract fun bindInstallChannelInteractor(interactor: InstallChannelInteractorImpl): InstallChannelInteractor

    @Binds
    internal abstract fun bindUninstallChannelInteractor(interactor: UninstallChannelInteractorImpl): UninstallChannelInteractor

    @Binds
    internal abstract fun bindGetChannelHomeContentInteractor(interactor: GetChannelHomeContentInteractorImpl): GetChannelHomeContentInteractor

    @Binds
    internal abstract fun bindGetChannelContentLinkInteractor(interactor: GetChannelContentLinkInteractorImpl): GetChannelContentLinkInteractor

    @Binds
    internal abstract fun bindGetSenderCategoriesInteractor(interactor: GetSenderCategoriesInteractorImpl): GetSenderCategoriesInteractor

    @Binds
    internal abstract fun bindGetSenderDetailInteractor(interactor: GetSenderDetailInteractorImpl): GetSenderDetailInteractor

    @Binds
    internal abstract fun bindGetStoreboxReceiptsInteractor(interactor: GetStoreboxReceiptsInteractorImpl): GetStoreboxReceiptsInteractor

    @Binds
    internal abstract fun bindGetStoreboxReceiptInteractor(interactor: GetStoreboxReceiptInteractorImpl): GetStoreboxReceiptInteractor

    @Binds
    internal abstract fun bindGetSegmentDetailInteractor(interactor: GetSegmentInteractorImpl): GetSegmentInteractor

    @Binds
    internal abstract fun bindGetPendingInteractor(interactor: GetPendingInteractorImpl): GetPendingInteractor



    @Binds
    internal abstract fun bindGetCollectionsInteractor(interactor: GetCollectionsInteractorImpl): GetCollectionsInteractor

    @Binds
    internal abstract fun bindRegisterInteractor(interactor: RegisterInteractorImpl): RegisterInteractor

    @Binds
    internal abstract fun bindUnRegisterInteractor(interactor: UnRegisterInteractorImpl): UnRegisterInteractor

    @Binds
    internal abstract fun bindRegistrationsInteractor(interactor: GetRegistrationsInteractorImpl): GetRegistrationsInteractor

    @Binds
    internal abstract fun bindLinkStoreboxInteractor(interactor: LinkStoreboxInteractorImpl): LinkStoreboxInteractor

    @Binds
    internal abstract fun bindCreateStoreboxInteractor(interactor: CreateStoreboxInteractorImpl): CreateStoreboxInteractor

    @Binds
    internal abstract fun bindGetStoreboxProfileInteractor(interactor: GetStoreboxProfileInteractorImpl): GetStoreboxProfileInteractor

    @Binds
    internal abstract fun bindPutStoreboxProfileInteractor(interactor: PutStoreboxProfileInteractorImpl): PutStoreboxProfileInteractor

    @Binds
    internal abstract fun bindGetStoreboxCardLinkInteractor(interactor: GetStoreboxCardLinkInteractorImpl): GetStoreboxCardLinkInteractor

    @Binds
    internal abstract fun bindDeleteStoreboxAccountLinkInteractor(interactor: DeleteStoreboxAccountLinkInteractorImpl): DeleteStoreboxAccountLinkInteractor

    @Binds
    internal abstract fun bindDeleteStoreboxReceiptInteractor(interactor: DeleteStoreboxReceiptInteractorImpl): DeleteStoreboxReceiptInteractor

    @Binds
    internal abstract fun bindUpdateStoreboxFlagsInteractor(interactor: UpdateStoreboxFlagsInteractorImpl): UpdateStoreboxFlagsInteractor

    @Binds
    internal abstract fun bindConfirmStoreboxInteractor(interactor: ConfirmStoreboxInteractorImpl): ConfirmStoreboxInteractor



    @Binds
    internal abstract fun bindVerifyPhoneInteractor(interactor: VerifyPhoneInteractorImpl): VerifyPhoneInteractor

    @Binds
    internal abstract fun bindConfirmPhoneInteractor(interactor: ConfirmPhoneInteractorImpl): ConfirmPhoneInteractor

    // E Key Interactors

    @Binds
    internal abstract fun bindGetEKeyVaultInteractor(interactor: GetEKeyVaultInteractorImpl): GetEKeyVaultInteractor

    @Binds
    internal abstract fun bindSetEKeyVaultInteractor(interactor: SetEKeyVaultInteractorImpl): SetEKeyVaultInteractor

    @Binds
    internal abstract fun bindDeleteEKeyVaultInteractor(interactor: DeleteEKeyVaultInteractorImpl): DeleteEKeyVaultInteractor

    @Binds
    internal abstract fun bindGetEKeyMasterkeyInteractor(interactor: GetEKeyMasterkeyInteractorImpl): GetEKeyMasterkeyInteractor

    @Binds
    internal abstract fun bindSetEKeyMasterkeyInteractor(interactor: SetEKeyMasterkeyInteractorImpl): SetEKeyMasterkeyInteractor

    @Binds
    internal abstract fun bindDeleteEKeyMasterkeyInteractor(interactor: DeleteEKeyMasterkeyInteractorImpl): DeleteEKeyMasterkeyInteractor

    @Binds
    internal abstract fun bindSaveReceiptInteractor(interactor: SaveReceiptInteractorImpl): SaveReceiptInteractor

    @Binds
    internal abstract fun bindShareReceiptInteractor(interactor: ShareReceiptInteractorImpl): ShareReceiptInteractor

    @Binds
    internal abstract fun bindEncryptUserLoginInfoInteractor(interactor: EncryptUserLoginInfoInteractorImpl): EncryptUserLoginInfoInteractor

    @Binds
    internal abstract fun bindTestLoginInteractor(interactor: TestLoginInteractorImpl): TestLoginInteractor

    @Binds
    internal abstract fun bindDecryptInteractor(interactor: DecryptUserLoginInfoInteractorImpl): DecryptUserLoginInfoInteractor
}