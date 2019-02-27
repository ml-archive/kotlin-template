package dk.eboks.app.keychain.interactors.user

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.repositories.SettingsRepository
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

/**
 * Created by bison on 24-06-2017.
 */
internal class CreateUserInteractorImpl @Inject constructor(
    executor: Executor,
    private val userManager: UserManager,
    private val api: Api,
    private val settingsRepository: SettingsRepository,
    private val appConfig: AppConfig
) : BaseInteractor(executor), CreateUserInteractor {
    override var output: CreateUserInteractor.Output? = null
    override var input: CreateUserInteractor.Input? = null

    override fun execute() {
        try {
            input?.user?.let { user ->
                input?.password?.let { password ->
                    // todo find which of the missing fields should be included
                    val body = JsonObject()
                    body.addProperty("name", user.name)
                    body.addProperty("identity", user.identity)
                    body.addProperty("password", password)
                    body.addProperty("identityType", "P")
                    if (appConfig.isSE) body.addProperty("nationality", "SE")
                    if (appConfig.isDK) body.addProperty("nationality", "DK")
                    if (appConfig.isNO) body.addProperty("nationality", "NO")
                    // body.addProperty("mobilenumber", "31674031")
                    // body.addProperty("newsletter", false)
                    val mails = JsonArray()
                    mails.add(user.getPrimaryEmail())
                    body.add("emails", mails)

//                    body.addProperty("device", GsonBuilder().create().toJson(createActivationDevice()))

                    val result = api.createUserProfile(body).execute()
                    result?.let { response ->
                        if (response.isSuccessful) {
                            runOnUIThread {
                                output?.onCreateUser(user)
                            }
                        }
                    }.guard {
                        throw(RuntimeException())
                    }
                }
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onCreateUserError(exceptionToViewError(t))
            }
        }
    }

//    private fun createActivationDevice(): ActivationDevice {
//
//        val deviceId = settingsRepository.get().deviceId
//        val deviceName = "test device"
//        val os = "Android"
//
//        ActivationDevice(deviceId, deviceName, os, gr)
//    }
}