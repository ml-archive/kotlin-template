package dk.eboks.app.mail.domain.interactors.senders

import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.repositories.SenderCategoriesRepository
import dk.eboks.app.domain.repositories.SendersRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch

class GetSendersInteractorImplTest {

    private val executor = TestExecutor()
    private val sendersRepository = mockk<SendersRepository>()
    private val categoryRepository = mockk<SenderCategoriesRepository>()

    private val interactor = GetSendersInteractorImpl(executor, sendersRepository, categoryRepository)

    // Dummy data
    private val cachedSenders = listOf(Sender(1, "cached"))
    private val loadedSenders = listOf(Sender(2, "loaded"))
    private val sendersCategory = SenderCategory(12, "name", 1, loadedSenders)
    private val queriedSenders = listOf(Sender(3, "nametoquery"))

    @Before
    fun setUp() {
        every { sendersRepository.getSenders(true, any()) } returns cachedSenders
        every { sendersRepository.getSenders(false, any()) } returns loadedSenders
        every { sendersRepository.searchSenders(any()) } returns queriedSenders
        every { categoryRepository.getSendersByCategory(any()) } returns sendersCategory
    }

    @Test
    fun `Get Cached Senders Test`() {

        val latch = CountDownLatch(2)

        interactor.input = GetSendersInteractor.Input(userId = 12)
        interactor.output = object : GetSendersInteractor.Output {
            override fun onGetSenders(senders: List<Sender>) {
                assert(true)

                if (latch.count == 2L) {
                    // First time callback called with the cached senders
                    assert(senders.containsAll(cachedSenders))
                    assert(senders.size == cachedSenders.size)
                } else {
                    // Then senders are getting refresh
                    assert(senders.containsAll(loadedSenders))
                    assert(senders.size == loadedSenders.size)
                }

                latch.countDown()
            }

            override fun onGetSendersError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Load  Senders Test`() {

        val latch = CountDownLatch(1)

        interactor.input = GetSendersInteractor.Input(cached = false, userId = 12)
        interactor.output = object : GetSendersInteractor.Output {
            override fun onGetSenders(senders: List<Sender>) {
                assert(true)
                assert(senders.containsAll(loadedSenders))
                assert(senders.size == loadedSenders.size)
                latch.countDown()
            }

            override fun onGetSendersError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Get Senders By Category Test`() {

        val latch = CountDownLatch(1)

        // Input set to load category
        interactor.input = GetSendersInteractor.Input(userId = 12, id = 12)
        interactor.output = object : GetSendersInteractor.Output {
            override fun onGetSenders(senders: List<Sender>) {
                assert(true)
                assert(senders.containsAll(sendersCategory.senders ?: listOf()))
                assert(senders.size == sendersCategory.senders?.size)
                latch.countDown()
            }

            override fun onGetSendersError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Get Senders By Name Test`() {
        val latch = CountDownLatch(1)

        // Input set to load by name
        interactor.input = GetSendersInteractor.Input(userId = 12, name = "name")
        interactor.output = object : GetSendersInteractor.Output {
            override fun onGetSenders(senders: List<Sender>) {
                assert(true)
                assert(senders.containsAll(queriedSenders))
                assert(senders.size == queriedSenders.size)
                latch.countDown()
            }

            override fun onGetSendersError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Get Senders By Name Error Test`() {
        val latch = CountDownLatch(1)

        every { sendersRepository.searchSenders(any()) } throws Exception()

        // Input set to load by name
        interactor.input = GetSendersInteractor.Input(userId = 12, name = "name")
        interactor.output = object : GetSendersInteractor.Output {
            override fun onGetSenders(senders: List<Sender>) {
                assert(false)
                latch.countDown()
            }

            override fun onGetSendersError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Get Senders Category Error Test`() {
        val latch = CountDownLatch(1)

        every { categoryRepository.getSendersByCategory(any()) } throws Exception()

        // Input set to load by name
        interactor.input = GetSendersInteractor.Input(userId = 12, name = "name")
        interactor.output = object : GetSendersInteractor.Output {
            override fun onGetSenders(senders: List<Sender>) {
                assert(false)
                latch.countDown()
            }

            override fun onGetSendersError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Get All Senders Error Test`() {
        val latch = CountDownLatch(1)

        every { sendersRepository.getSenders(any(), any()) } throws Exception()

        // Input set to load by name
        interactor.input = GetSendersInteractor.Input(userId = 12, name = "name")
        interactor.output = object : GetSendersInteractor.Output {
            override fun onGetSenders(senders: List<Sender>) {
                assert(false)
                latch.countDown()
            }

            override fun onGetSendersError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }
}