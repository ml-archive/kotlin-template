package dk.eboks.app.domain.interactors

import dagger.Binds
import dagger.Module
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

@Module
abstract class ChannelsBindingInteractorModule {
    @Binds
    internal abstract fun bindGetChannelsInteractor(interactor: GetChannelsInteractorImpl): GetChannelsInteractor

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
    internal abstract fun bindGetStoreboxCreditCardsInteractor(interactor: GetStoreboxCreditCardsInteractorImpl): GetStoreboxCreditCardsInteractor

    @Binds
    internal abstract fun bindDeleteStoreboxCreditCardInteractor(interactor: DeleteStoreboxCreditCardInteractorImpl): DeleteStoreboxCreditCardInteractor

    @Binds
    internal abstract fun bindGetStoreboxReceiptsInteractor(interactor: GetStoreboxReceiptsInteractorImpl): GetStoreboxReceiptsInteractor

    @Binds
    internal abstract fun bindGetStoreboxReceiptInteractor(interactor: GetStoreboxReceiptInteractorImpl): GetStoreboxReceiptInteractor

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
    internal abstract fun bindSaveReceiptInteractor(interactor: SaveReceiptInteractorImpl): SaveReceiptInteractor

    @Binds
    internal abstract fun bindShareReceiptInteractor(interactor: ShareReceiptInteractorImpl): ShareReceiptInteractor

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
}