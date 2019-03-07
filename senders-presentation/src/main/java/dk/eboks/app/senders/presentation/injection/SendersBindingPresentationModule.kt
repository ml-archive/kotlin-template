package dk.eboks.app.senders.presentation.injection

import dagger.Binds
import dagger.Module
import dk.eboks.app.senders.presentation.ui.components.SenderGroupsComponentContract
import dk.eboks.app.senders.presentation.ui.components.SenderGroupsComponentPresenter
import dk.eboks.app.senders.presentation.ui.components.categories.CategoriesComponentContract
import dk.eboks.app.senders.presentation.ui.components.categories.CategoriesComponentPresenter
import dk.eboks.app.senders.presentation.ui.components.list.SenderAllListComponentContract
import dk.eboks.app.senders.presentation.ui.components.list.SenderAllListComponentPresenter
import dk.eboks.app.senders.presentation.ui.components.register.RegisterPresenter
import dk.eboks.app.senders.presentation.ui.components.register.RegistrationContract
import dk.eboks.app.senders.presentation.ui.screens.browse.BrowseCategoryContract
import dk.eboks.app.senders.presentation.ui.screens.browse.BrowseCategoryPresenter
import dk.eboks.app.senders.presentation.ui.screens.detail.SenderDetailContract
import dk.eboks.app.senders.presentation.ui.screens.detail.SenderDetailPresenter
import dk.eboks.app.senders.presentation.ui.screens.list.SenderAllListContract
import dk.eboks.app.senders.presentation.ui.screens.list.SenderAllListPresenter
import dk.eboks.app.senders.presentation.ui.screens.overview.SendersOverviewContract
import dk.eboks.app.senders.presentation.ui.screens.overview.SendersOverviewPresenter
import dk.eboks.app.senders.presentation.ui.screens.registrations.PendingContract
import dk.eboks.app.senders.presentation.ui.screens.registrations.PendingPresenter
import dk.eboks.app.senders.presentation.ui.screens.registrations.RegistrationsContract
import dk.eboks.app.senders.presentation.ui.screens.registrations.RegistrationsPresenter
import dk.eboks.app.senders.presentation.ui.screens.segment.SegmentDetailContract
import dk.eboks.app.senders.presentation.ui.screens.segment.SegmentDetailPresenter
import dk.nodes.arch.domain.injection.scopes.ActivityScope

@Module
abstract class SendersBindingPresentationModule {
    @ActivityScope
    @Binds
    internal abstract fun bindCategoriesComponentPresenter(presenter: CategoriesComponentPresenter): CategoriesComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun bindSendersOverviewPresenter(presenter: SendersOverviewPresenter): SendersOverviewContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun bindBrowseCategoryPresenter(presenter: BrowseCategoryPresenter): BrowseCategoryContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun bindRegistrationsPresenter(presenter: RegistrationsPresenter): RegistrationsContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun bindSenderGroupsComponentPresenter(presenter: SenderGroupsComponentPresenter): SenderGroupsComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun bindPendingPresenter(presenter: PendingPresenter): PendingContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun bindSenderDetailPresenter(presenter: SenderDetailPresenter): SenderDetailContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun bindSegmentDetailPresenter(presenter: SegmentDetailPresenter): SegmentDetailContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun bindSenderAllListPresenter(presenter: SenderAllListPresenter): SenderAllListContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun bindSenderAllListComponentPresenter(presenter: SenderAllListComponentPresenter): SenderAllListComponentContract.Presenter

    @ActivityScope
    @Binds
    internal abstract fun bindRegisterPresenter(presenter: RegisterPresenter): RegistrationContract.Presenter
}