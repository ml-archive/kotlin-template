package dk.eboks.app.keychain.interactors.authentication

import dk.eboks.app.domain.managers.CryptoManager
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import java.util.concurrent.CountDownLatch

class CheckRSAKeyPresenceInteractorImplTest {

    private val executor = TestExecutor()
    private val cryptoManager = mockk<CryptoManager>()
    private val interactor = CheckRSAKeyPresenceInteractorImpl(executor, cryptoManager)

    @Test
    fun `Test Key Exists`() {
        every { cryptoManager.hasActivation(any()) } returns true
        val latch = CountDownLatch(1)

        interactor.input = CheckRSAKeyPresenceInteractor.Input("")
        interactor.output = object : CheckRSAKeyPresenceInteractor.Output {
            override fun onCheckRSAKeyPresence(keyExists: Boolean) {
                assertTrue(keyExists)
                latch.countDown()
            }

            override fun onCheckRSAKeyPresenceError(error: ViewError) {
                assertTrue(false)
                latch.countDown()
            }
        }
        interactor.execute()
        latch.await()
    }

    @Test
    fun `Test Key Doesn't Exists`() {
        every { cryptoManager.hasActivation(any()) } returns false
        val latch = CountDownLatch(1)

        interactor.input = CheckRSAKeyPresenceInteractor.Input("")
        interactor.output = object : CheckRSAKeyPresenceInteractor.Output {
            override fun onCheckRSAKeyPresence(keyExists: Boolean) {
                assertFalse(keyExists)
                latch.countDown()
            }

            override fun onCheckRSAKeyPresenceError(error: ViewError) {
                assertTrue(false)
                latch.countDown()
            }
        }
        interactor.execute()
        latch.await()
    }

    @Test
    fun `Test Fail`() {
        every { cryptoManager.hasActivation(any()) } throws IOException()
        val latch = CountDownLatch(1)

        interactor.input = CheckRSAKeyPresenceInteractor.Input("")
        interactor.output = object : CheckRSAKeyPresenceInteractor.Output {
            override fun onCheckRSAKeyPresence(keyExists: Boolean) {
                assertTrue(false)
                latch.countDown()
            }

            override fun onCheckRSAKeyPresenceError(error: ViewError) {
                assertTrue(true)
                latch.countDown()
            }
        }
        interactor.execute()
        latch.await()
    }
}