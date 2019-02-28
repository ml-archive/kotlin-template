package dk.eboks.app.injection.modules

import dagger.Binds
import dagger.Module
import dk.eboks.app.presentation.ui.home.screens.HomeContract
import dk.eboks.app.presentation.ui.home.screens.HomePresenter
import dk.eboks.app.presentation.ui.message.components.detail.payment.PaymentButtonComponentContract
import dk.eboks.app.presentation.ui.message.components.detail.payment.PaymentButtonComponentPresenter
import dk.eboks.app.presentation.ui.message.components.detail.payment.PaymentComponentContract
import dk.eboks.app.presentation.ui.message.components.detail.payment.PaymentComponentPresenter
import dk.nodes.arch.domain.injection.scopes.ActivityScope

@Module
abstract class PresentationBindingModule {
    @ActivityScope
    @Binds
    abstract fun provideHomePresenter(presenter: HomePresenter): HomeContract.Presenter

}