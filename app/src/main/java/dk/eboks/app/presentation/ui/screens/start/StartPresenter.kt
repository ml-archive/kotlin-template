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
class StartPresenter(val appStateManager: AppStateManager, val bootstrapInteractor: BootstrapInteractor, val userProfileInteractor: GetUserProfileInteractor) :
        StartContract.Presenter,
        BasePresenterImpl<StartContract.View>(),
        BootstrapInteractor.Output,
        GetUserProfileInteractor.Output {

    init {
        bootstrapInteractor.output = this
        userProfileInteractor.output = this
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
                userProfileInteractor.run()
            } else {
                v.showWelcomeComponent()
            }
        }
        /*
        loginInteractor.input = LoginInteractor.Input(UserInfo(identity = "0703151319", identityType = "P", nationality = "DK", pincode = "a12345", activationCode = "Rg9d2X3D"))
        loginInteractor.run()
        */
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
        runAction { v ->
            v.showUserCarouselComponent()
        }
    }
}