package dk.eboks.app.presentation.ui.components.profile.myinfo

import dk.eboks.app.domain.interactors.user.SaveUserInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

class MyInfoComponentPresenter @Inject constructor(val appState: AppStateManager, val saveUserInteractor: SaveUserInteractor) :
        MyInfoComponentContract.Presenter,
        BasePresenterImpl<MyInfoComponentContract.View>(),
        SaveUserInteractor.Output
{
    init {
        saveUserInteractor.output = this
    }

    override fun setup() {
        appState.state?.currentUser?.let { user->
            runAction { v->
                v.setName(user.name)
                user.email?.let { v.setPrimaryEmail(it) }
                user.secondaryEmail?.let { v.setSecondaryEmail(it) }
                user.mobileNumber?.let { v.setMobileNumber(it) }
                v.setNewsletter(user.newsletter)
            }
        }
    }

    override fun save() {
        runAction { v->
            appState.state?.currentUser?.let { user ->
                user.name = v.getName()
                user.email = v.getPrimaryEmail()
                user.secondaryEmail = v.getSecondaryEmail()
                user.mobileNumber = v.getMobileNumber()
                user.newsletter = v.getNewsletter()
                saveUserInteractor.input = SaveUserInteractor.Input(user)
                saveUserInteractor.run()
            }
        }
    }

    override fun onSaveUser(user: User, numberOfUsers: Int) {
        Timber.e("User saved")
    }

    override fun onSaveUserError(error: ViewError) {
        runAction { v->v.showErrorDialog(error) }
    }
}