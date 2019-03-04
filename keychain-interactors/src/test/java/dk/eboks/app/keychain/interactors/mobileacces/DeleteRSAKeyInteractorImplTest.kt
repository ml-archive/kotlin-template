package dk.eboks.app.keychain.interactors.mobileacces

import dk.eboks.app.domain.managers.CryptoManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.keychain.interactors.mobileacces.DeleteRSAKeyInteractor.Output
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.util.concurrent.CountDownLatch

class DeleteRSAKeyInteractorImplTest {
    private val executor = TestExecutor()
    private val cryptoManager = mockk<CryptoManager>()
    private val interactor = DeleteRSAKeyInteractorImpl(executor, cryptoManager)

    @Test
    fun `Test success`() {
        val latch = CountDownLatch(1)
        every { cryptoManager.deleteAllActivations() } returns Unit
        interactor.output = object : Output {
            override fun onDeleteRSAKeySuccess() {
                latch.countDown()
            }

            override fun onDeleteRSAKeyError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test error`() {
        val latch = CountDownLatch(1)
        every { cryptoManager.deleteAllActivations() } throws Exception()
        interactor.output = object : Output {
            override fun onDeleteRSAKeySuccess() {
                assert(false)
                latch.countDown()
            }

            override fun onDeleteRSAKeyError(error: ViewError) {
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }
}