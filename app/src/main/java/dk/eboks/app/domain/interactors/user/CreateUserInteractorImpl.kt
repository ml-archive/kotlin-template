package dk.eboks.app.domain.interactors.user

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.network.repositories.SignupRestRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by bison on 24-06-2017.
 */
class CreateUserInteractorImpl(executor: Executor, val userManager: UserManager, val signupRestRepo: SignupRestRepository) : BaseInteractor(executor), CreateUserInteractor {
    override var output: CreateUserInteractor.Output? = null
    override var input: CreateUserInteractor.Input? = null

    override fun execute() {
        try {
            input?.user?.let { user ->
                input?.password?.let { password ->
                    //todo find which of the missing fields should be included
                    val body = JsonObject()
                    body.addProperty("name", user.name)
                    body.addProperty("identity", user.getPrimaryEmail())
                    body.addProperty("password", password)
                    body.addProperty("identityType", "P")
                    body.addProperty("nationality", "DK")
                    val mails = JsonArray()
                    mails.add(user.getPrimaryEmail())
                    body.add("emails", mails)

                    val createUser = signupRestRepo.createUser(body)

                    runOnUIThread {
                        createUser.let {
                            output?.setActivationCode(createUser)
                            output?.onCreateUser(user)
                        }
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