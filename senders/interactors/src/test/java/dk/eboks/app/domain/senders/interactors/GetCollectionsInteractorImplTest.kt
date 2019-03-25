package dk.eboks.app.domain.senders.interactors

import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.repositories.CollectionsRepository
import dk.nodes.arch.domain.executor.TestExecutor
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.util.concurrent.CountDownLatch

class GetCollectionsInteractorImplTest {

    private val executor = TestExecutor()
    private val collectionsRepository: CollectionsRepository = mockk(relaxUnitFun = true)

    private val interactor: GetCollectionsInteractor = GetCollectionsInteractorImpl(executor, collectionsRepository)

    @Test
    fun `Test Collections Load Success`() {
        val latch = CountDownLatch(1)
        every { collectionsRepository.getCollections(any()) } returns listOf()

        interactor.input = GetCollectionsInteractor.Input(true)
        interactor.output = object : GetCollectionsInteractor.Output {
            override fun onGetCollections(collections: List<CollectionContainer>) {
                assert(true)
                latch.countDown()
            }
        }

        interactor.run()
        latch.await()
    }

}