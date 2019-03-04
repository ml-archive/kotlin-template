package dk.eboks.app.keychain.interactors.mobileacces

import dk.eboks.app.domain.managers.CryptoManager
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Test
import java.util.concurrent.CountDownLatch

class DeleteRSAKeyForUserInteractorImplTest {
    private val executor = TestExecutor()
    private val cryptoManager = mockk<CryptoManager>()
    private val interactor = DeleteRSAKeyForUserInteractorImpl(executor, cryptoManager)

    @After
    fun tearDown() {
        interactor.output = null
        interactor.input = null
    }

    @Test
    fun `Test success`() {
        every { cryptoManager.deleteActivation(any()) } returns Unit
        interactor.input = DeleteRSAKeyForUserInteractor.Input("12346789")
        val latch = CountDownLatch(1)
        interactor.output = object : DeleteRSAKeyForUserInteractor.Output {
            override fun onDeleteRSAKeyForUserError(error: ViewError) {
                assert(false)
                latch.countDown()
            }

            override fun onDeleteRSAKeyForUserSuccess() {
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test error`() {
        every { cryptoManager.deleteActivation(any()) } throws Exception()
        interactor.input = DeleteRSAKeyForUserInteractor.Input("12346789")
        val latch = CountDownLatch(1)
        interactor.output = object : DeleteRSAKeyForUserInteractor.Output {
            override fun onDeleteRSAKeyForUserError(error: ViewError) {
                latch.countDown()
            }

            override fun onDeleteRSAKeyForUserSuccess() {
                assert(false)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }
}