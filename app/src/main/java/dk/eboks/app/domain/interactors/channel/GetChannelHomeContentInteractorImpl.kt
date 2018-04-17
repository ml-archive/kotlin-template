package dk.eboks.app.domain.interactors.channel

import dk.eboks.app.domain.models.channel.Channel
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

    var had_channels = false
    var controlCachedMap : MutableMap<Int, Boolean> = HashMap()


    override fun execute() {
        had_channels = false
        controlCachedMap.clear()
        try {
            emitCachedData()
            //refreshCachedData()
        } catch (t: Throwable) {
            runOnUIThread {
                t.printStackTrace()
                val ve = exceptionToViewError(t)
                output?.onGetChannelHomeContentError(ve)
            }
        }
    }

    private fun emitCachedData()
    {
        had_channels = channelsRepository.hasCachedChannelList("pinned")
        val pinnedChannels = channelsRepository.getPinnedChannels(true)
        runOnUIThread {
            output?.onGetPinnedChannelList(pinnedChannels)
        }

        if(pinnedChannels.isNotEmpty())
        {
            val channelMap : MutableMap<Int, Deferred<HomeContent>> = HashMap()
            pinnedChannels.forEachIndexed { index, channel ->
                controlCachedMap[index] = channelsRepository.hasCachedChannelControl(channel.id.toLong())
                val d = async { channelsRepository.getChannelHomeContent(channel.id.toLong(), true) }
                channelMap[index] = d
            }

            runBlocking {
                launch(CommonPool) {
                    try {
                        for(entry in channelMap)
                        {
                            entry.value.await()
                            val content = entry.value.getCompleted()
                            runOnUIThread {
                                var channel = pinnedChannels[entry.key]
                                output?.onGetChannelHomeContent(channel, content)
                                Timber.e("emitCachedData channel ${channel.name}")
                            }
                        }
                    }
                    catch(t : Throwable)
                    {
                        t.printStackTrace()
                    }
                }
            }
            Timber.e("emitCachedData completed, loaded ${channelMap.size} controls")
        }
    }

    private fun refreshCachedData()
    {
        // do not load the channels from the network if we just did
        val pinnedChannels = channelsRepository.getPinnedChannels(!had_channels)
        runOnUIThread {
            output?.onGetPinnedChannelList(pinnedChannels)
        }
        if(pinnedChannels.isNotEmpty())
        {
            val channelMap : MutableMap<Int, Deferred<HomeContent>> = HashMap()
            pinnedChannels.forEachIndexed { index, channel ->
                // don't reload channel control from network if we just did
                if(controlCachedMap[index] == true) {
                    val d = async { channelsRepository.getChannelHomeContent(channel.id.toLong(), false) }
                    channelMap[index] = d
                }
            }

            runBlocking {
                launch(CommonPool) {
                    try {
                        for(entry in channelMap)
                        {
                            entry.value.await()
                            val content = entry.value.getCompleted()
                            runOnUIThread {
                                var channel = pinnedChannels[entry.key]
                                output?.onGetChannelHomeContent(channel, content)
                                Timber.e("refreshCachedData channel ${channel.name}")
                            }
                        }
                    }
                    catch(t : Throwable)
                    {
                        t.printStackTrace()
                    }
                }
            }
            Timber.e("refreshCachedData completed, loaded ${channelMap.size} controls")
        }
    }
}