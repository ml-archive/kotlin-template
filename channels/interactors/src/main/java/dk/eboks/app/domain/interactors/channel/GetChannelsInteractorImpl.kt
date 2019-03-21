package dk.eboks.app.domain.interactors.channel

import dk.eboks.app.domain.repositories.ChannelsRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 01/02/18.
 */
internal class GetChannelsInteractorImpl @Inject constructor(
    executor: Executor,
    private val channelsRepository: ChannelsRepository
) : BaseInteractor(executor), GetChannelsInteractor {
    override var output: GetChannelsInteractor.Output? = null
    override var input: GetChannelsInteractor.Input? = null

    override fun execute() {
        try {
            val channels = channelsRepository.getChannels(input?.cached ?: true)
            Timber.e("Got channels $channels")
            runOnUIThread {
                output?.onGetChannels(channels)
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onGetChannelsError(exceptionToViewError(t))
            }
        }
    }
}