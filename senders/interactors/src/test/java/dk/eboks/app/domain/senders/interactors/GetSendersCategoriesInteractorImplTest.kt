package dk.eboks.app.domain.senders.interactors

import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.SenderCategoriesRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.util.concurrent.CountDownLatch

class GetSendersCategoriesInteractorImplTest {

    private val executor = TestExecutor()
    private val repository: SenderCategoriesRepository = mockk()

    private val interactor: GetSenderCategoriesInteractor = GetSenderCategoriesInteractorImpl(executor, repository)

    @Test
    fun `Test Sender Categories Loading Success`() {
        val latch = CountDownLatch(1)
        every { repository.getSenderCategories(any()) } returns listOf()

        interactor.input = GetSenderCategoriesInteractor.Input(true)
        interactor.output = object : GetSenderCategoriesInteractor.Output {
            override fun onGetCategories(categories: List<SenderCategory>) {
                assert(true)
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
    fun `Test Sender Categories Loading Error`() {
        val latch = CountDownLatch(1)
        every { repository.getSenderCategories(any()) } throws Exception()

        interactor.input = GetSenderCategoriesInteractor.Input(true)
        interactor.output = object : GetSenderCategoriesInteractor.Output {
            override fun onGetCategories(categories: List<SenderCategory>) {
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