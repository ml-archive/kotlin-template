package dk.eboks.app.domain.interactors.encryption

import com.google.gson.Gson
import dk.eboks.app.domain.managers.EncryptionPreferenceManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.LoginInfo
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

class DecryptUserLoginInfoInteractorImpl(
    executor: Executor,
    private val encryptionPreferenceManager: EncryptionPreferenceManager
) :
    BaseInteractor(executor),
    DecryptUserLoginInfoInteractor {

    override var output: DecryptUserLoginInfoInteractor.Output? = null

    override fun execute() {
        Timber.d("execute")

        try {
            val s = encryptionPreferenceManager.getString(LoginInfo.KEY, "")
            Timber.i("loginInfoString: $s")
            output?.onDecryptSuccess(Gson().fromJson(s, LoginInfo::class.java))
        } catch (e: Exception) {
            output?.onDecryptError(
                ViewError(
                    Translation.androidfingerprint.dialogTitle,
                    Translation.androidfingerprint.errorMessage,
                    true,
                    true
                )
            )
        }
    }
}