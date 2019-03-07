package dk.eboks.app.mail.domain.interactors.message

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.StorageInfo
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.util.concurrent.CountDownLatch

class GetStorageInteractorImplTest {

    private val executor = TestExecutor()
    private val repository = mockk<MessagesRepository>()

    private val interactor = GetStorageInteractorImpl(executor, repository)


    @Test
    fun `Get Storage Test`() {
        val latch = CountDownLatch(1)
        val info = StorageInfo(12, 5)
        every { repository.getStorageInfo() } returns info

        interactor.output = object : GetStorageInteractor.Output {
            override fun onGetStorage(storageInfo: StorageInfo) {
                assert(storageInfo == info)
                assert(true)
                latch.countDown()
            }

            override fun onGetStorageError(error: ViewError) {
                assert(false)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()

    }

    @Test
    fun `Get Storage Error Test`() {
        val latch = CountDownLatch(1)
        every { repository.getStorageInfo() } throws Exception()

        interactor.output = object : GetStorageInteractor.Output {
            override fun onGetStorage(storageInfo: StorageInfo) {
                assert(false)
                latch.countDown()
            }

            override fun onGetStorageError(error: ViewError) {
                assert(true)
                latch.countDown()
            }
        }
        interactor.run()
        latch.await()

    }

}