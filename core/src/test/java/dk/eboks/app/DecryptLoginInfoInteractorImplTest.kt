package dk.eboks.app

import com.google.gson.Gson
import dk.eboks.app.domain.interactors.encryption.DecryptUserLoginInfoInteractor
import dk.eboks.app.domain.interactors.encryption.DecryptUserLoginInfoInteractorImpl
import dk.eboks.app.domain.managers.EncryptionPreferenceManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.LoginInfo
import dk.eboks.app.domain.models.login.LoginInfoType
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.lang.Exception
import java.util.concurrent.CountDownLatch

class DecryptLoginInfoInteractorImplTest {

    private val executor = TestExecutor()
    private val encryptionPreferenceManager: EncryptionPreferenceManager = mockk(relaxUnitFun = true)

    private val interactor = DecryptUserLoginInfoInteractorImpl(executor, encryptionPreferenceManager)

    @Before
    fun setUp() {
    }

    @Test
    fun `Test Login Info Decryption Success`() {
        val latch = CountDownLatch(1)
        val loginInfo = LoginInfo(LoginInfoType.SOCIAL_SECURITY, "12414", "password", "activate!")
        val gson = Gson().toJson(loginInfo)
        every { encryptionPreferenceManager.getString(any(), any()) } returns gson

        interactor.output = object : DecryptUserLoginInfoInteractor.Output {
            override fun onDecryptSuccess(loginInfo: LoginInfo) {
                assert(true)
                latch.countDown()
            }

            override fun onDecryptError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Test Login Info Decryption Failure`() {
        val latch = CountDownLatch(1)

        every { encryptionPreferenceManager.getString(any(), any()) } throws Exception()

        interactor.output = object : DecryptUserLoginInfoInteractor.Output {
            override fun onDecryptSuccess(loginInfo: LoginInfo) {
                assert(false)
                latch.countDown()
            }

            override fun onDecryptError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }
}