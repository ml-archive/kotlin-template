package dk.eboks.app.domain.senders.injection

import dagger.Binds
import dagger.Module
import dk.eboks.app.domain.senders.interactors.GetCollectionsInteractor
import dk.eboks.app.domain.senders.interactors.GetCollectionsInteractorImpl
import dk.eboks.app.domain.senders.interactors.GetSegmentInteractor
import dk.eboks.app.domain.senders.interactors.GetSegmentInteractorImpl
import dk.eboks.app.domain.senders.interactors.GetSenderCategoriesInteractor
import dk.eboks.app.domain.senders.interactors.GetSenderCategoriesInteractorImpl
import dk.eboks.app.domain.senders.interactors.GetSenderDetailInteractor
import dk.eboks.app.domain.senders.interactors.GetSenderDetailInteractorImpl
import dk.eboks.app.domain.senders.interactors.register.*

@Module
abstract class SendersBindingInteractorModule {
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

    @Binds
    internal abstract fun getSenderRegistrationLinkInteractor(interactor: GetSenderRegistrationLinkInteractorImpl) : GetSenderRegistrationLinkInteractor
}