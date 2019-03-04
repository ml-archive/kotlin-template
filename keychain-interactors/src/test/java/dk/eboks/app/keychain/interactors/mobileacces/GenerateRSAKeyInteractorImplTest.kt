package dk.eboks.app.keychain.interactors.mobileacces

import dk.eboks.app.domain.managers.CryptoManager
import dk.eboks.app.domain.models.local.DeviceActivation
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.security.PublicKey
import java.util.concurrent.CountDownLatch

class GenerateRSAKeyInteractorImplTest {

    private val executor = TestExecutor()
    private val cryptoManager = mockk<CryptoManager>()
    private val publicKey = mockk<PublicKey>()
    private val interactor = GenerateRSAKeyInteractorImpl(executor, cryptoManager)
    private val rsaReturn = "rsa"

    @Before
    fun setup() {
        every { cryptoManager.generateRSAKey(any()) } just Runs
        every { cryptoManager.getActivation(any()) } returns DeviceActivation(publicKey = publicKey)
        every { publicKey.encoded } returns "encoded".toByteArray()
        every { cryptoManager.getPublicKeyAsString(any()) }.returns(rsaReturn)
    }

    @After
    fun tearDown() {
        interactor.input = null
        interactor.output = null
    }

    @Test
    fun `Test success`() {

        val latch = CountDownLatch(1)
        interactor.input = GenerateRSAKeyInteractor.Input("userId")
        interactor.output = object : GenerateRSAKeyInteractor.Output {
            override fun onGenerateRSAKeySuccess(RSAKey: String) {
                Assert.assertEquals(RSAKey, rsaReturn)
                latch.countDown()
            }

            override fun onGenerateRSAKeyError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test encode error`() {
        every { cryptoManager.generateRSAKey(any()) } throws Exception()
        interactor.input = GenerateRSAKeyInteractor.Input("userId")
        val latch = CountDownLatch(1)
        interactor.output = object : GenerateRSAKeyInteractor.Output {
            override fun onGenerateRSAKeySuccess(RSAKey: String) {
                assert(false)
                latch.countDown()
            }

            override fun onGenerateRSAKeyError(error: ViewError) {
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test no args error`() {
        val latch = CountDownLatch(1)
        interactor.output = object : GenerateRSAKeyInteractor.Output {
            override fun onGenerateRSAKeySuccess(RSAKey: String) {
                assert(false)
                latch.countDown()
            }

            override fun onGenerateRSAKeyError(error: ViewError) {
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }

    @Test
    fun `Test rsa key error`() {
        every { publicKey.encoded } returns null
        val latch = CountDownLatch(1)
        interactor.output = object : GenerateRSAKeyInteractor.Output {
            override fun onGenerateRSAKeySuccess(RSAKey: String) {
                assert(false)
                latch.countDown()
            }

            override fun onGenerateRSAKeyError(error: ViewError) {
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()
    }
}