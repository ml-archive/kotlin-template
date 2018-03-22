package dk.eboks.app.domain.interactors.channel

import dk.eboks.app.domain.exceptions.InteractorException
import dk.eboks.app.domain.repositories.ChannelsRepository
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by bison on 01/02/18.
 */
class GetChannelInteractorImpl(executor: Executor, val channelsRepository: ChannelsRepository) : BaseInteractor(executor), GetChannelInteractor {
    override var output: GetChannelInteractor.Output? = null
    override var input: GetChannelInteractor.Input? = null

    override fun execute() {
        try {
            input?.let {
                val channel = channelsRepository.getChannel(it.id)
                Timber.e("Got channel $channel")

                runOnUIThread {
                    output?.onGetChannel(channel)
                }
            }.guard {
                throw(InteractorException("bad args"))
            }
        } catch (t: Throwable) {
            runOnUIThread {
                val ve = exceptionToViewError(t)
                ve.shouldCloseView = true
                output?.onGetChannelError(ve)
            }
        }
    }
}