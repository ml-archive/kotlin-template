package dk.eboks.app.domain.interactors

import android.os.Build
import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.models.request.AppInfo
import dk.eboks.app.domain.models.request.LoginRequest
import dk.eboks.app.domain.models.request.UserInfo
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by bison on 24-06-2017.
 */
class LoginInteractorImpl(executor: Executor, val api: Api) : BaseInteractor(executor), LoginInteractor {
    override var output : LoginInteractor.Output? = null
    override var input : LoginInteractor.Input? = null

    override fun execute() {
        // we don't use input in this example but we could:
        input?.let {
            // do something with unwrapped input
        }
        try {
            Timber.e("Calling mr waders")
            val version = BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE
            val os_version = Build.VERSION.RELEASE
            val device = Build.MODEL

            val arg = LoginRequest(
                    AppInfo(version = version, os = "Android", device = device, osVersion = os_version),
                    UserInfo(identity = "0703151319", identityType = "P", nationality = "DK", pincode = "a12345")
            )

            api.login(arg).blockingGet()
            runOnUIThread {
                output?.onLogin()
            }
        } catch (e: Exception) {
            runOnUIThread {
                output?.onError(e.message ?: "Unknown error")
            }
        }
    }
}