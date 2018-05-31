package dk.eboks.app.presentation.ui.components.profile.myinfo

import dk.eboks.app.domain.interactors.user.SaveUserInteractor
import dk.eboks.app.domain.interactors.user.UpdateUserInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

class MyInfoComponentPresenter @Inject constructor(
        val appState: AppStateManager,
        val saveUserInteractor: SaveUserInteractor,
        val updateUserInteractor: UpdateUserInteractor
) :
        MyInfoComponentContract.Presenter,
        BasePresenterImpl<MyInfoComponentContract.View>(),
        UpdateUserInteractor.Output,
        SaveUserInteractor.Output {
    init {
        saveUserInteractor.output = this
        updateUserInteractor.output = this
    }

    var user = User()

    override fun setup() {
        appState.state?.currentUser?.let { user ->
            runAction { v ->
                v.setName(user.name)
                user.getPrimaryEmail()?.let { v.setPrimaryEmail(it) }
                user.getSecondaryEmail()?.let { v.setSecondaryEmail(it) }
                user.mobileNumber?.value?.let { v.setMobileNumber(it) }
                v.setNewsletter(user.newsletter)
            }
        }
    }

    override fun save() {
        runAction { v ->
            v.showProgress(true)


            user.setPrimaryEmail(v.getPrimaryEmail())
            user.setSecondaryEmail(v.getSecondaryEmail())
            user.newsletter = v.getNewsletter()
            user.name = v.getName()
            user.mobileNumber?.value = v.getMobileNumber()
            user.newsletter = v.getNewsletter()
            appState.state?.currentUser = user

            // save user on the server
            updateUserInteractor.input = UpdateUserInteractor.Input(user)
            updateUserInteractor.run()

//            // save user locally
//            saveUserInteractor.input = SaveUserInteractor.Input(user)
//            saveUserInteractor.run()



        }
    }

    override fun onSaveUser(user: User, numberOfUsers: Int) {
        Timber.e("User saved")
        runAction { v ->
            v.setSaveEnabled(false)
            v.showProgress(false)
            v.showToast(Translation.profile.yourInfoWasSaved)
            v.onDone()
        }
    }

    override fun onSaveUserError(error: ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }

    override fun onUpdateProfile() {
        //todo
        Timber.e("onUpdateProfile succesfull")
    }

    override fun onUpdateProfileError(error: ViewError) {
        //todo
        Timber.e(error.message)
    }
}