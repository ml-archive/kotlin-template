package dk.eboks.app.profile.interactors

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.domain.repositories.UserRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.Test
import java.lang.Exception
import java.util.concurrent.CountDownLatch

class UpdateUserInteractorImplTest {

    private val executor = TestExecutor()
    private val repository: UserRepository = mockk(relaxUnitFun = true)

    private val interactor = UpdateUserInteractorImpl(executor, repository)


    @Test
    fun `Update User Test`() {
        val latch = CountDownLatch(1)
        val user = User()

        every {
            repository.updateProfile(
                    match {
                        !it.has("name") ||
                        !it.has("mobilenumber") ||
                        !it.has("newsletter") ||
                        !it.has("emails")
            })
        } throws Exception()

        interactor.input = UpdateUserInteractor.Input(user)
        interactor.output = object : UpdateUserInteractor.Output {
            override fun onUpdateProfile() {
                assert(true)
                latch.countDown()
            }

            override fun onUpdateProfileError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Update User Test Error `() {
        val latch = CountDownLatch(1)
        val user = User()

        every { repository.updateProfile(any()) } throws Exception()

        interactor.input = UpdateUserInteractor.Input(user)
        interactor.output = object : UpdateUserInteractor.Output {
            override fun onUpdateProfile() {
                assert(false)
                latch.countDown()
            }

            override fun onUpdateProfileError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }
}