package dk.eboks.app.domain.interactors.user

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dk.eboks.app.domain.repositories.UserRepository
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

class UpdateUserInteractorImpl(executor: Executor, val api: Api, val userRestRepo: UserRepository) : BaseInteractor(executor), UpdateUserInteractor {
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
                // only verified users are allowed to edit ANY email
                if(it.verified) {
                    mails.add(it.getPrimaryEmail())
                    it.getSecondaryEmail()?.let { email ->
                        mails.add(email)
                    }
                    body.add("emails", mails)
                }
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