package dk.eboks.app.injection.modules

import dagger.Binds
import dagger.Module
import dk.eboks.app.domain.interactors.ChannelsBindingInteractorModule
import dk.eboks.app.domain.interactors.ekey.DeleteEKeyMasterkeyInteractor
import dk.eboks.app.domain.interactors.ekey.DeleteEKeyMasterkeyInteractorImpl
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
import dk.eboks.app.keychain.injection.KeychainInteractorsModule
import dk.eboks.app.mail.domain.interactors.MailBindingInteractorModule
import dk.eboks.app.profile.injection.ProfileInteractorsModule

@Module(
    includes = [
        MailBindingInteractorModule::class,
        KeychainInteractorsModule::class,
        ChannelsBindingInteractorModule::class,
        ProfileInteractorsModule::class,
        CoreInteractorsModule::class
    ]
)
abstract class InteractorModule {

    @Binds
    internal abstract fun bindGetSenderCategoriesInteractor(interactor: GetSenderCategoriesInteractorImpl): GetSenderCategoriesInteractor

    @Binds
    internal abstract fun bindGetSenderDetailInteractor(interactor: GetSenderDetailInteractorImpl): GetSenderDetailInteractor

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

    // E Key Interactors

    @Binds
    internal abstract fun bindDeleteEKeyMasterkeyInteractor(interactor: DeleteEKeyMasterkeyInteractorImpl): DeleteEKeyMasterkeyInteractor
}