package dk.eboks.app.injection.modules

import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.interactors.BootstrapInteractor
import dk.eboks.app.domain.interactors.GetSendersInteractor
import dk.eboks.app.domain.interactors.LoginInteractor
import dk.eboks.app.presentation.ui.mail.MailOverviewContract
import dk.eboks.app.presentation.ui.mail.MailOverviewPresenter
import dk.eboks.app.presentation.ui.main.MainContract
import dk.eboks.app.presentation.ui.main.MainPresenter
import dk.eboks.app.presentation.ui.splash.SplashContract
import dk.eboks.app.presentation.ui.splash.SplashPresenter
import dk.nodes.arch.domain.injection.scopes.ActivityScope

/**
 * Created by bison on 07/12/17.
 */

@Module
class PresentationModule {
    @ActivityScope
    @Provides
    fun provideMainPresenter(getPostsInteractor: LoginInteractor) : MainContract.Presenter {
        return MainPresenter(getPostsInteractor)
    }

    @ActivityScope
    @Provides
    fun provideSplashPresenter(bootstrapInteractor: BootstrapInteractor, loginInteractor: LoginInteractor) : SplashContract.Presenter {
        return SplashPresenter(bootstrapInteractor, loginInteractor)
    }

    @ActivityScope
    @Provides
    fun provideMailOverviewPresenter(getSendersInteractor: GetSendersInteractor) : MailOverviewContract.Presenter {
        return MailOverviewPresenter(getSendersInteractor)
    }
}