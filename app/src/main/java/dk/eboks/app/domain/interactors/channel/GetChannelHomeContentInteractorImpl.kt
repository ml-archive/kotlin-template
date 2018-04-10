package dk.eboks.app.domain.interactors.channel

import dk.eboks.app.domain.models.home.HomeContent
import dk.eboks.app.domain.repositories.ChannelsRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import kotlinx.coroutines.experimental.*
import timber.log.Timber

/**
 * Created by bison on 01/02/18.
 */
class GetChannelHomeContentInteractorImpl(executor: Executor, val channelsRepository: ChannelsRepository) : BaseInteractor(executor), GetChannelHomeContentInteractor {
    override var output: GetChannelHomeContentInteractor.Output? = null
    override var input: GetChannelHomeContentInteractor.Input? = null



    override fun execute() {
        try {
            val pinnedChannels = channelsRepository.getPinnedChannels()
            runOnUIThread {
                output?.onGetPinnedChannelList(pinnedChannels)
            }
            if(pinnedChannels.isNotEmpty())
            {
                Timber.e("channel home content loading started for ${pinnedChannels.size} channels")

                val channelMap : MutableMap<Int, Deferred<HomeContent>> = HashMap()
                for (channel in pinnedChannels) {
                    val d = async { channelsRepository.getChannelHomeContent(channel.id.toLong()) }
                    channelMap[channel.id] = d
                }
                //val result : MutableList<Control> = ArrayList()

                runBlocking {
                    launch(CommonPool) {
                        try {
                            for(entry in channelMap)
                            {
                                entry.value.await()
                                val content = entry.value.getCompleted()
                                Timber.e("Got HomeContent $content")
                                runOnUIThread {
                                    output?.onGetChannelHomeContent(entry.key, content)
                                }
                            }
                        }
                        catch(t : Throwable)
                        {
                            t.printStackTrace()
                        }
                    }
                }

                Timber.e("channel home content loading completed, loaded ${channelMap.size} controls")
            }
            else    // there are no pinned channels
            {

            }
        } catch (t: Throwable) {
            runOnUIThread {
                t.printStackTrace()
                val ve = exceptionToViewError(t)
                output?.onGetChannelHomeContentError(ve)
            }
        }
    }

}