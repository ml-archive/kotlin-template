package dk.eboks.app.domain.interactors.authentication.mobileacces

import android.os.Build
import dk.eboks.app.domain.models.login.ActivationDevice
import dk.eboks.app.domain.repositories.SettingsRepository
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class ActivateDeviceInteractorImpl(executor: Executor, val settingsRepository: SettingsRepository, val api : Api) : BaseInteractor(executor), ActivateDeviceInteractor {
    override var input: ActivateDeviceInteractor.Input? = null
    override var output: ActivateDeviceInteractor.Output? = null

    override fun execute() {
        try {
            input?.let {

                val deviceId = settingsRepository.get().deviceId
                val deviceName = "test device"
                val os = "Android"
                //todo change this back, using hardcoded values to help debug the api
//                val os = "Android " + Build.VERSION.RELEASE
                val key = it.key

//                val keyType = "RSA2048"
                //todo (sikk) this is a temporary requirement for the API and should be removed once it becomes unnecessary
                //see: https://3.basecamp.com/3171131/buckets/2493171/messages/1315281094#__recording_1385562221
                
//                val userResult = api.activateDevice(ActivationDevice(deviceId,deviceName,os,key,keyType)).execute()
                val userResult = api.activateDevice(ActivationDevice(deviceId,deviceName,os,key)).execute()
                    runOnUIThread { output?.onActivateDeviceSuccess() }
                }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onActivateDeviceError(exceptionToViewError(t), input?.key)
            }
        }
    }
}