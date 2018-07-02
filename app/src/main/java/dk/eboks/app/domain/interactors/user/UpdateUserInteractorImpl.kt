package dk.eboks.app.domain.interactors.user

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dk.eboks.app.network.Api
import dk.eboks.app.network.repositories.UserRestRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

class UpdateUserInteractorImpl(executor: Executor, val api: Api, val userRestRepo: UserRestRepository) : BaseInteractor(executor), UpdateUserInteractor {
    override var output: UpdateUserInteractor.Output? = null
    override var input: UpdateUserInteractor.Input? = null


    override fun execute() {

        try {
            input?.user?.let {
                val body = JsonObject()
                body.addProperty("name", it.name)
                body.addProperty("mobilenumber", it.mobilenumber?.value)
                body.addProperty("newsletter", it.newsletter)
                val mails = JsonArray()
                mails.add(it.getPrimaryEmail())
                mails.add(it.getSecondaryEmail())
                body.add("emails", mails)
                userRestRepo.updateProfile(body)
            }
            runOnUIThread {
                output?.onUpdateProfile()
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onUpdateProfileError(exceptionToViewError(t, shouldDisplay = false))
                Timber.e(t)
            }
        }

    }
}