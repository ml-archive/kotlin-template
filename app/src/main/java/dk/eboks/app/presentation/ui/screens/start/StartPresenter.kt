package dk.eboks.app.presentation.ui.screens.start

import dk.eboks.app.domain.interactors.BootstrapInteractor
import dk.eboks.app.domain.interactors.user.GetUserProfileInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber

/**
 * Created by bison on 20-05-2017.
 */
class StartPresenter(val appStateManager: AppStateManager, val bootstrapInteractor: BootstrapInteractor) :
        StartContract.Presenter,
        BasePresenterImpl<StartContract.View>(),
        BootstrapInteractor.Output,
        GetUserProfileInteractor.Output {

    init {
        bootstrapInteractor.output = this
    }

    override fun startup() {
        Timber.e("Startup, running version control")
        runAction { v -> v.performVersionControl() }
    }

    override fun proceed() {
        Timber.e("Proceeding to run bootstrap interactor")
        bootstrapInteractor.run()
    }

    override fun onBootstrapDone(hasUsers: Boolean) {
        Timber.e("Boostrap done")
        runAction { v ->
            if (hasUsers) {
                v.showUserCarouselComponent()
            } else {
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