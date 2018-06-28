package dk.eboks.app.domain.interactors.user

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.network.Api
import dk.eboks.app.network.repositories.SignupRestRepository
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by bison on 24-06-2017.
 */
class CreateUserInteractorImpl(executor: Executor, val userManager: UserManager, private val api: Api) : BaseInteractor(executor), CreateUserInteractor {
    override var output: CreateUserInteractor.Output? = null
    override var input: CreateUserInteractor.Input? = null

    override fun execute() {
        try {
            input?.user?.let { user ->
                input?.password?.let { password ->
                    //todo find which of the missing fields should be included
                    val body = JsonObject()
                    body.addProperty("name", user.name)
                    body.addProperty("identity", user.identity)
                    body.addProperty("password", password)
                    body.addProperty("identityType", "P")
                    body.addProperty("nationality", "DK")
                    //body.addProperty("mobilenumber", "31674031")
                    //body.addProperty("newsletter", false)
                    val mails = JsonArray()
                    mails.add(user.getPrimaryEmail())
                    body.add("emails", mails)

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
}