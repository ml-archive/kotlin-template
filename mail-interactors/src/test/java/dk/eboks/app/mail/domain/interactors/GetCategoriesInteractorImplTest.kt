package dk.eboks.app.mail.domain.interactors

import dk.eboks.app.domain.managers.*
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.domain.repositories.MailCategoriesRepository
import dk.eboks.app.mail.domain.interactors.categories.GetCategoriesInteractor
import dk.eboks.app.mail.domain.interactors.categories.GetMailCategoriesInteractorImpl
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.TestExecutor
import dk.nodes.arch.domain.interactor.Interactor
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Test
import java.lang.Exception
import java.util.concurrent.CountDownLatch

class GetCategoriesInteractorImplTest {
    private val executor = TestExecutor()
    private val foldersRepositoryMail = mockk<MailCategoriesRepository>()
    private val interactor = GetMailCategoriesInteractorImpl(
            executor,
            foldersRepositoryMail
    )


    @After
    fun tearDown() {
        interactor.output = null
        interactor.input = null
    }



    @Test
    fun `Test Get Categories`() {
        val user = User(id = 12)
        every { foldersRepositoryMail.getMailCategories(any(), any()) } returns listOf(Folder(), Folder())

        val latch =  CountDownLatch(1)

        interactor.input = GetCategoriesInteractor.Input(false, user.id)
        interactor.output = object : GetCategoriesInteractor.Output {

            override fun onGetCategories(folders: List<Folder>) {
                assert(folders.size == 2)
                latch.countDown()
            }

            override fun onGetCategoriesError(error: ViewError) {
                assert(false)
                latch.countDown()
            }

        }

        interactor.run()
        latch.await()

    }


    @Test
    fun `Test Get Categories Error`() {
        val user = User(id = 12)
        val latch = CountDownLatch(1)
        every { foldersRepositoryMail.getMailCategories(any(), any()) } throws Exception()

        interactor.input = GetCategoriesInteractor.Input(false, userId = user.id)

        interactor.output = object : GetCategoriesInteractor.Output {
            override fun onGetCategories(folders: List<Folder>) {
                assert(false)
                latch.countDown()
            }

            override fun onGetCategoriesError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }


}