package dk.eboks.app.domain.interactors.channel

import dk.eboks.app.domain.models.home.HomeContent
import dk.eboks.app.domain.repositories.ChannelsRepository
import dk.eboks.app.presentation.ui.home.components.channelcontrol.ChannelControlComponentFragment
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 01/02/18.
 */
class GetChannelHomeContentInteractorImpl @Inject constructor(
    executor: Executor,
    private val channelsRepository: ChannelsRepository
) : BaseInteractor(executor), GetChannelHomeContentInteractor {
    override var output: GetChannelHomeContentInteractor.Output? = null
    override var input: GetChannelHomeContentInteractor.Input? = null

    private var hadChannels = false
    private val controlCachedMap: MutableMap<Int, Boolean> = HashMap()

    override fun execute() {
        hadChannels = false

        try {
            refreshContent()
            runOnUIThread { output?.onGetChannelHomeContentDone() }
        } catch (t: Throwable) {
            runOnUIThread {
                t.printStackTrace()
                val ve = exceptionToViewError(t)
                output?.onGetInstalledChannelListError(ve)
            }
        }

        /*
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
        */
    }

    private fun emitCachedData() {
        hadChannels = channelsRepository.hasCachedChannelList("pinned")
        val pinnedChannels = channelsRepository.getPinnedChannels(true)
        runOnUIThread {
            output?.onGetInstalledChannelList(pinnedChannels)
        }

        if (pinnedChannels.isNotEmpty()) {
            val channelMap: MutableMap<Int, Deferred<HomeContent>> = HashMap()
            pinnedChannels.forEachIndexed { index, channel ->
                controlCachedMap[index] =
                    channelsRepository.hasCachedChannelControl(channel.id.toLong())
                val d = GlobalScope.async {
                    channelsRepository.getChannelHomeContent(
                        channel.id.toLong(),
                        true
                    )
                }
                channelMap[index] = d
            }

            runBlocking {
                for (entry in channelMap) {
                    entry.value.await()
                    val content = entry.value.getCompleted()
                    runOnUIThread {
                        val channel = pinnedChannels[entry.key]
                        output?.onGetChannelHomeContent(channel, content)
                        // Timber.e("emitCachedData channel ${channel.name}")
                    }
                }
            }
            Timber.e("emitCachedData completed, loaded ${channelMap.size} controls")
        }
    }

    private fun refreshCachedData() {
        // do not load the channels from the network if we just did, then we'll just reuse the cached stuff
        val installedChannels = channelsRepository.getInstalledChannels()

        if (installedChannels.isNotEmpty()) {
            val channelMap: MutableMap<Int, Deferred<HomeContent>> = HashMap()
            installedChannels.forEachIndexed { index, channel ->
                // if we had to fetch the data in emitCachedData() dont load it from the network again
                if (controlCachedMap[index] == true || input?.cached == false) {
                    val d = GlobalScope.async {
                        channelsRepository.getChannelHomeContent(
                            channel.id.toLong(),
                            false
                        )
                    }
                    channelMap[index] = d
                } else // fetch from cache since it was refreshed last go
                {
                    val d = GlobalScope.async {
                        channelsRepository.getChannelHomeContent(
                            channel.id.toLong(),
                            true
                        )
                    }
                    channelMap[index] = d
                }
            }

            // force uncached controls to finish loading
            runBlocking {
                for (entry in channelMap) {
                    entry.value.await()
                }
            }

            // refresh controls in one big swoop the second time around
            runOnUIThread {
                output?.onGetInstalledChannelList(installedChannels)
                for (entry in channelMap) {
                    val channel = installedChannels[entry.key]
                    val content = entry.value.getCompleted()
                    output?.onGetChannelHomeContent(channel, content)
                    // Timber.e("refreshCachedData channel ${channel.name}")
                }
            }
            Timber.e("refreshCachedData completed, loaded ${channelMap.size} controls")
        }
    }

    private fun refreshContent() {
        // do not load the channels from the network if we just did, then we'll just reuse the cached stuff
        val pinnedChannels = channelsRepository.getPinnedChannels(false)
        Timber.e("Got ${pinnedChannels.size} channels")
        runOnUIThread {
            output?.onGetInstalledChannelList(pinnedChannels)
        }

        if (pinnedChannels.isNotEmpty()) {
            // val channelMap : MutableMap<Int, Deferred<HomeContent>> = HashMap()
            runBlocking {
                pinnedChannels.forEach { channel ->
                    // if we had to fetch the data in emitCachedData() dont load it from the network again
                    try {
                        Timber.d("TESTING output == null ? ${output == null}")
                        output?.let {
                            if (!it.continueGetChannelHomeContent()) {
                                Timber.d("TESTING IN IF")
                                ChannelControlComponentFragment.refreshOnResume = true
                                return@runBlocking
                            }
                        }
                        val d = async {
                            channelsRepository.getChannelHomeContent(
                                channel.id.toLong(),
                                false
                            )
                        }
                        d.await()
                        val content = d.getCompleted()
                        runOnUIThread {
                            output?.onGetChannelHomeContent(channel, content)
                            Timber.e("refreshContent tile channel ${channel.name}")
                        }
                    } catch (t: Throwable) {
                        t.printStackTrace()
                        Timber.e("Got 'ception but continuing")
                        if (t is NoSuchElementException) {
                            runOnUIThread {
                                output?.onGetChannelHomeContentEmpty(channel)
                            }
                        } else {
                            runOnUIThread {
                                output?.onGetChannelHomeContentError(channel)
                            }
                        }
                    }
                }
            }
        }
    }
}