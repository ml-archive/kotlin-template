package dk.eboks.app.domain.interactors.channel

import dk.eboks.app.domain.models.home.HomeContent
import dk.eboks.app.domain.repositories.ChannelsRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
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
            if(input?.cached == true)
                emitCachedData()
            refreshCachedData()
            runOnUIThread { output?.onGetChannelHomeContentDone() }
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
                for(entry in channelMap)
                {
                    entry.value.await()
                    val content = entry.value.getCompleted()
                    runOnUIThread {
                        var channel = pinnedChannels[entry.key]
                        output?.onGetChannelHomeContent(channel, content)
                        //Timber.e("emitCachedData channel ${channel.name}")
                    }
                }
            }
            Timber.e("emitCachedData completed, loaded ${channelMap.size} controls")
        }
    }

    private fun refreshCachedData()
    {
        // do not load the channels from the network if we just did, then we'll just reuse the cached stuff
        val pinnedChannels = channelsRepository.getPinnedChannels(!had_channels)

        if(pinnedChannels.isNotEmpty())
        {
            val channelMap : MutableMap<Int, Deferred<HomeContent>> = HashMap()
            pinnedChannels.forEachIndexed { index, channel ->
                // if we had to fetch the data in emitCachedData() dont load it from the network again
                if(controlCachedMap[index] == true || input?.cached == false) {
                    val d = async { channelsRepository.getChannelHomeContent(channel.id.toLong(), false) }
                    channelMap[index] = d
                }
                else // fetch from cache since it was refreshed last go
                {
                    val d = async { channelsRepository.getChannelHomeContent(channel.id.toLong(), true) }
                    channelMap[index] = d
                }

            }


            // force uncached controls to finish loading
            runBlocking {
                for(entry in channelMap)
                {
                    entry.value.await()
                }
            }

            // refresh controls in one big swoop the second time around
            runOnUIThread {
                output?.onGetPinnedChannelList(pinnedChannels)
                for(entry in channelMap)
                {
                    var channel = pinnedChannels[entry.key]
                    val content = entry.value.getCompleted()
                    output?.onGetChannelHomeContent(channel, content)
                    //Timber.e("refreshCachedData channel ${channel.name}")
                }
            }
            Timber.e("refreshCachedData completed, loaded ${channelMap.size} controls")

        }
    }
}