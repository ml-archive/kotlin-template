package dk.eboks.app.domain.senders.interactors

import dk.eboks.app.domain.repositories.CollectionsRepository
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

/**
 * Created by chnt on 14/03/18.
 * @author chnt
 * @since 14/03/18.
 */
internal class GetCollectionsInteractorImpl @Inject constructor(
    executor: Executor,
    private val collectionsRepository: CollectionsRepository
) : BaseInteractor(executor), GetCollectionsInteractor {
    override var output: GetCollectionsInteractor.Output? = null
    override var input: GetCollectionsInteractor.Input? = null

    override fun execute() {
        try {
            val collections = collectionsRepository.getCollections(input?.cached ?: true)
            runOnUIThread {
                output?.onGetCollections(collections)
            }
        } catch (t: Throwable) {
            runOnUIThread {
                // TODO add function to interface and change presenter accordingly
                // output?.onGetCollectionsError(exceptionToViewError(t))
            }
        }
    }
}