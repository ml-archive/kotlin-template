package dk.eboks.app.profile.presentation.ui.components.myinfo

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.ContactPoint
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.profile.interactors.GetUserProfileInteractor
import dk.eboks.app.profile.interactors.SaveUserInteractor
import dk.eboks.app.profile.interactors.UpdateUserInteractor
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

internal class MyInfoComponentPresenter @Inject constructor(
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
        view {
            Timber.e("SHOWING USER ID = ${user.id}")
            setName(user.name)
            user.getPrimaryEmail()
                ?.let { setPrimaryEmail(it, user.getPrimaryEmailIsVerified(), user.verified) }
            if (user.verified) {
                user.getSecondaryEmail()
                    ?.let { setSecondaryEmail(it, user.getSecondaryEmailIsVerified()) }
                showSecondaryEmail(true)
            } else {
                // showPrimaryEmail(false)
                showSecondaryEmail(false)
            }

            user.mobilenumber?.let {
                it.value?.let { value ->
                    setMobileNumber(value, it.verified)
                }
            }
            setNewsletter(user.newsletter)
            setSaveEnabled(false)
        }
    }

    override fun save(closeView: Boolean) {
        currentUser?.let { user ->
            this.closeView = closeView
            view {
                showProgress(true)
                setSaveEnabled(false)

                Timber.e("Attempting to save currentUser id: ${user.id}")
                user.setPrimaryEmail(getPrimaryEmail())
                // secondary email only apply to verified users
                if (user.verified)
                    user.setSecondaryEmail(getSecondaryEmail())

                user.newsletter = getNewsletter()
                user.name = getName()
                user.mobilenumber?.let {
                    it.value = getMobileNumber()
                }.guard {
                    user.mobilenumber = ContactPoint(getMobileNumber(), false)
                }
                user.newsletter = getNewsletter()
                appState.state?.currentUser = currentUser

                // save currentUser on the server
                updateUserInteractor.input = UpdateUserInteractor.Input(user)
                updateUserInteractor.run()
            }
        }
    }

    override fun onSaveUser(user: User, numberOfUsers: Int) {
        Timber.e("User saved ${user.id}")
        view {
            setSaveEnabled(false)
            showProgress(false)
            showToast(Translation.profile.yourInfoWasSaved)
            refresh()
            // if(closeView)
            //    onDone()
        }
    }

    override fun onSaveUserError(error: ViewError) {
        view {
            showProgress(false)
            showErrorDialog(error)
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
        view {
            setSaveEnabled(true)
            showProgress(false)
            showToast(Translation.profile.failedToSaveProfile)
            onDone()
        }
    }

    override fun onGetUser(user: User) {
        showUser(user)
    }

    override fun onGetUserError(error: ViewError) {
        view { showErrorDialog(error) }
    }
}