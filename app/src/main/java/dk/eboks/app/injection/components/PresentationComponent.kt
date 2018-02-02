package dk.eboks.app.injection.components

import dagger.Component
import dagger.Module
import dagger.Provides
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.presentation.ui.mail.MailFolderActivity
import dk.eboks.app.presentation.ui.mail.MailFolderPresenter
import dk.eboks.app.presentation.ui.mail.MailOverviewActivity
import dk.eboks.app.presentation.ui.mail.MailOverviewPresenter
import dk.eboks.app.presentation.ui.main.MainActivity
import dk.eboks.app.presentation.ui.main.MainPresenter
import dk.eboks.app.presentation.ui.splash.SplashActivity
import dk.eboks.app.presentation.ui.splash.SplashPresenter
import dk.nodes.arch.domain.injection.scopes.ActivityScope

/**
 * Created by bison on 26-07-2017.
 */

@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(PresentationModule::class))
@ActivityScope
interface PresentationComponent {
    fun inject(target : MainActivity)
    fun inject(target : MainPresenter)
    fun inject(target : SplashActivity)
    fun inject(target : SplashPresenter)
    fun inject(target : MailOverviewActivity)
    fun inject(target : MailOverviewPresenter)
    fun inject(target : MailFolderActivity)
    fun inject(target : MailFolderPresenter)

}
