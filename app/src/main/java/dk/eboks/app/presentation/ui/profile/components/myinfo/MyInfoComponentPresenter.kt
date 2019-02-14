package dk.eboks.app.presentation.ui.profile.components.myinfo

import dk.eboks.app.domain.interactors.user.GetUserProfileInteractor
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
    private val appState: AppStateManager,
    private val saveUserInteractor: SaveUserInteractor,
    private val updateUserInteractor: UpdateUserInteractor,
    private val getUserProfileInteractor: GetUserProfileInteractor
) :
    MyInfoComponentContract.Presenter,
    BasePresenterImpl<MyInfoComponentContract.View>(),
    UpdateUserInteractor.Output,
    SaveUserInteractor.Output,
    GetUserProfileInteractor.Output {
    init {
        saveUserInteractor.output = this
        updateUserInteractor.output = this
        getUserProfileInteractor.output = this
    }

    private var currentUser: User? = null
    private var closeView = true

    override fun setup() {
        appState.state?.currentUser?.let { user ->
            currentUser = user
            showUser(user)
        }
    }

    override fun refresh() {
        getUserProfileInteractor.run()
    }

    private fun showUser(user: User) {
        runAction { v ->
            Timber.e("SHOWING USER ID = ${user.id}")
            v.setName(user.name)
            user.getPrimaryEmail()
                ?.let { v.setPrimaryEmail(it, user.getPrimaryEmailIsVerified(), user.verified) }
            if (user.verified) {
                user.getSecondaryEmail()
                    ?.let { v.setSecondaryEmail(it, user.getSecondaryEmailIsVerified()) }
                v.showSecondaryEmail(true)
            } else {
                // v.showPrimaryEmail(false)
                v.showSecondaryEmail(false)
            }

            user.mobilenumber?.let {
                it.value?.let { value ->
                    v.setMobileNumber(value, it.verified)
                }
            }
            v.setNewsletter(user.newsletter)
            v.setSaveEnabled(false)
        }
    }

    override fun save(closeView: Boolean) {
        currentUser?.let { user ->
            this.closeView = closeView
            runAction { v ->
                v.showProgress(true)
                v.setSaveEnabled(false)

                Timber.e("Attempting to save currentUser id: ${user.id}")
                user.setPrimaryEmail(v.getPrimaryEmail())
                // secondary email only apply to verified users
                if (user.verified)
                    user.setSecondaryEmail(v.getSecondaryEmail())

                user.newsletter = v.getNewsletter()
                user.name = v.getName()
                user.mobilenumber?.let {
                    it.value = v.getMobileNumber()
                }.guard {
                    user.mobilenumber = ContactPoint(v.getMobileNumber(), false)
                }
                user.newsletter = v.getNewsletter()
                appState.state?.currentUser = currentUser

                // save currentUser on the server
                updateUserInteractor.input = UpdateUserInteractor.Input(user)
                updateUserInteractor.run()
            }
        }
    }

    override fun onSaveUser(user: User, numberOfUsers: Int) {
        Timber.e("User saved ${user.id}")
        runAction { v ->
            v.setSaveEnabled(false)
            v.showProgress(false)
            v.showToast(Translation.profile.yourInfoWasSaved)
            refresh()
            // if(closeView)
            //    v.onDone()
        }
    }

    override fun onSaveUserError(error: ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }

    override fun onUpdateProfile() {
        currentUser?.let { user ->
            // save currentUser locally
            saveUserInteractor.input = SaveUserInteractor.Input(user)
            saveUserInteractor.run()
        }
    }

    override fun onUpdateProfileError(error: ViewError) {
        Timber.e(error.message)
        runAction { v ->
            v.setSaveEnabled(true)
            v.showProgress(false)
            v.showToast(Translation.profile.failedToSaveProfile)
            v.onDone()
        }
    }

    override fun onGetUser(user: User) {
        showUser(user)
    }

    override fun onGetUserError(error: ViewError) {
        runAction { v ->
            v.showErrorDialog(error)
        }
    }
}