package dk.eboks.app.presentation.ui.profile.components.myinfo

import dk.eboks.app.domain.interactors.user.SaveUserInteractor
import dk.eboks.app.domain.interactors.user.UpdateUserInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.ContactPoint
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.util.guard
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
    var closeView = true

    override fun setup() {
        appState.state?.currentUser?.let { user ->
            runAction { v ->
                v.setName(user.name)
                user.getPrimaryEmail()?.let { v.setPrimaryEmail(it, user.getPrimaryEmailIsVerified()) }
                if(user.verified)
                {
                    user.getSecondaryEmail()?.let { v.setSecondaryEmail(it, user.getSecondaryEmailIsVerified()) }
                    v.showSecondaryEmail(true)
                }
                else
                {
                    v.showSecondaryEmail(false)
                }

                user.mobilenumber?.let {
                    it.value?.let { value ->
                        v.setMobileNumber(value, it.verified)
                    }
                }
                v.setNewsletter(user.newsletter)
            }
        }
    }

    override fun save(closeView : Boolean) {
        this.closeView = closeView
        runAction { v ->
            v.showProgress(true)
            v.setSaveEnabled(false)

            user.setPrimaryEmail(v.getPrimaryEmail())
            user.setSecondaryEmail(v.getSecondaryEmail())
            user.newsletter = v.getNewsletter()
            user.name = v.getName()
            user.mobilenumber?.let {
                it.value = v.getMobileNumber()
            }.guard {
                user.mobilenumber = ContactPoint(v.getMobileNumber(), false)
            }
            user.newsletter = v.getNewsletter()
            appState.state?.currentUser = user

            // save user on the server
            updateUserInteractor.input = UpdateUserInteractor.Input(user)
            updateUserInteractor.run()

        }
    }

    override fun onSaveUser(user: User, numberOfUsers: Int) {
        Timber.e("User saved")
        runAction { v ->
            v.setSaveEnabled(true)
            v.showProgress(false)
            v.showToast(Translation.profile.yourInfoWasSaved)
            if(closeView)
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
        //todo  needs to save the user locally after the profile has been saved on the server.
        Timber.e("onUpdateProfile succesfull")
        // save user locally
            saveUserInteractor.input = SaveUserInteractor.Input(user)
            saveUserInteractor.run()

    }

    override fun onUpdateProfileError(error: ViewError) {
        //todo
        Timber.e(error.message)
        runAction { v ->
            v.setSaveEnabled(true)
            v.showProgress(false)
            v.showToast(Translation.profile.failedToSaveProfile)
            v.onDone()
        }
    }
}