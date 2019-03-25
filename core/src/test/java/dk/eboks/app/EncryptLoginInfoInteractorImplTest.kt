package dk.eboks.app

import dk.eboks.app.domain.interactors.encryption.EncryptUserLoginInfoInteractor
import dk.eboks.app.domain.interactors.encryption.EncryptUserLoginInfoInteractorImpl
import dk.eboks.app.domain.managers.EncryptionPreferenceManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.LoginInfo
import dk.eboks.app.domain.models.login.LoginInfoType
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.lang.Exception
import java.util.concurrent.CountDownLatch

class EncryptLoginInfoInteractorImplTest {

    private val executor = TestExecutor()
    private val encryptionPreferenceManager: EncryptionPreferenceManager = mockk(relaxUnitFun = true)

    private val interactor = EncryptUserLoginInfoInteractorImpl(executor, encryptionPreferenceManager)

    @Test
    fun `Test Login Info Encryption Success`() {
        val latch = CountDownLatch(1)
        val loginInfo = LoginInfo(
                type = LoginInfoType.SOCIAL_SECURITY,
                socialSecurity = "123421",
                password = "password",
                actvationCode = "activate!")

        interactor.input = EncryptUserLoginInfoInteractor.Input(loginInfo)
        interactor.output = object : EncryptUserLoginInfoInteractor.Output {
            override fun onSuccess() {
                assert(true)
                latch.countDown()
            }

            override fun onError(e: ViewError) {
                assert(false)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

    @Test
    fun `Test Login Info Encryption Failure`() {

        every { encryptionPreferenceManager.setString(any(), any()) } throws Exception()

        val latch = CountDownLatch(1)
        val loginInfo = LoginInfo(
                type = LoginInfoType.SOCIAL_SECURITY,
                socialSecurity = "123421",
                password = "password",
                actvationCode = "activate!")

        interactor.input = EncryptUserLoginInfoInteractor.Input(loginInfo)
        interactor.output = object : EncryptUserLoginInfoInteractor.Output {
            override fun onSuccess() {
                assert(false)
                latch.countDown()
            }

            override fun onError(e: ViewError) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }
}