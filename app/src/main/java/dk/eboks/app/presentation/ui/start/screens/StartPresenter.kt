package dk.eboks.app.presentation.ui.start.screens

import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.interactors.BootstrapInteractor
import dk.eboks.app.domain.managers.PrefManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.profile.interactors.GetUserProfileInteractor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class StartPresenter @Inject constructor(
    private val bootstrapInteractor: BootstrapInteractor,
    private val prefManager: PrefManager,
    private val appConfig: AppConfig
) :
    StartContract.Presenter,
    BasePresenterImpl<StartContract.View>(),
    BootstrapInteractor.Output,
    GetUserProfileInteractor.Output {

    init {
        bootstrapInteractor.output = this
    }

    override fun startup() {
        Timber.e("Startup, running version control")
        if (appConfig.isDebug) {
            runAction { v -> v.performVersionControl() }
        } else {
            Timber.e("Release not running appOpen call")
            proceed()
        }
    }

    override fun proceed() {
        Timber.e("Proceeding to run bootstrap interactor")
        bootstrapInteractor.run()
    }

    override fun onBootstrapDone(hasUsers: Boolean, autoLogin: Boolean) {
        Timber.e("Boostrap done")
        runAction { v ->
            v.bootstrapDone()
            if (autoLogin) {
                v.startMain()
            } else if (hasUsers) {
                v.showUserCarouselComponent()
            } else {
                if (BuildConfig.ENABLE_BETA_DISCLAIMER) {
                    if (!prefManager.getBoolean("didShowBetaDisclaimer", false)) {
                        prefManager.setBoolean("didShowBetaDisclaimer", true)
                        v.showDisclaimer()
                    } else
                        v.showWelcomeComponent()
                } else
                    v.showWelcomeComponent()
            }
        }
    }

    override fun onBootstrapError(error: ViewError) {
        runAction { v -> v.showErrorDialog(error) }
    }

    override fun onGetUser(user: User) {
        runAction { v ->
            v.startMain()
        }
    }

    override fun onGetUserError(error: ViewError) {
        Timber.w("WARNING: SERVER COULDN'T FIND THE USER")
        runAction { v ->
            v.showUserCarouselComponent()
        }
    }
}