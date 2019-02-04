package dk.eboks.app.domain.interactors.encryption

import com.google.gson.Gson
import dk.eboks.app.domain.managers.EncryptionPreferenceManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.LoginInfo
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

class EncryptUserLoginInfoInteractorImpl(
    executor: Executor,
    private val encryptionPreferenceManager: EncryptionPreferenceManager
) :
    BaseInteractor(executor),
    EncryptUserLoginInfoInteractor {

    override var input: EncryptUserLoginInfoInteractor.Input? = null
    override var output: EncryptUserLoginInfoInteractor.Output? = null

    override fun execute() {
        Timber.d("execute")

        val loginInfo = input?.loginInfo ?: return
        val loginInfoString = Gson().toJson(loginInfo)

        Timber.d("loginInfoString: %s", loginInfoString)

        try {
            encryptionPreferenceManager.setString(LoginInfo.KEY, loginInfoString)
            runOnUIThread {
                output?.onSuccess()
            }
        } catch (e: Exception) {
            val viewError = ViewError(
                Translation.androidfingerprint.dialogTitle,
                Translation.androidfingerprint.errorMessage,
                true,
                true
            )
            runOnUIThread {
                output?.onError(viewError)
            }
        }
    }
}