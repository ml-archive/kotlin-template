package dk.eboks.app.presentation.ui.components.channels.opening

import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.interactors.channel.GetChannelInteractor
import dk.eboks.app.domain.interactors.storebox.CreateStoreboxInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.APIConstants
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.util.areAllRequirementsVerified
import dk.eboks.app.util.getType
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ChannelOpeningComponentPresenter @Inject constructor(val appState: AppStateManager, val getChannelInteractor: GetChannelInteractor, val createStoreboxInteractor: CreateStoreboxInteractor) :
        ChannelOpeningComponentContract.Presenter,
        BasePresenterImpl<ChannelOpeningComponentContract.View>(),
        GetChannelInteractor.Output,
        CreateStoreboxInteractor.Output
{
    var channelId : Int = 0
    var channel: Channel? = null

    init {
        getChannelInteractor.output = this
        createStoreboxInteractor.output = this
    }

    override fun setup(channelId: Int) {
        this.channelId = channelId
    }

    override fun refreshChannel() {
        Timber.e("refreshChannel")
        runAction { v ->
            v.showProgress(true)
        }
        getChannelInteractor.input = GetChannelInteractor.Input(channelId)
        getChannelInteractor.run()
    }

    override fun install(channel: Channel) {
        runAction { v ->
            //v.showProgress(true)
            // do we have any requirements, if so are they all verified? if not show the requirements drawer
            if(!channel.areAllRequirementsVerified())
            {
                v.showRequirementsDrawer(channel)
            }
            else
            {
                when (channel.getType()) {
                    "channel" -> {
                        // TODO not implemented
                    }
                    "storebox" -> {
                        createStoreboxInteractor.run()
                    }
                    "ekey" -> {
                        // TODO not implemented
                    }
                }
            }
        }
    }

    override fun open(channel: Channel) {
        //storebox channels id 1 - 3
        //ekey channels id 101 - 103

        when (channel.getType()) {
            "channel" -> {
                runAction { v ->
                    v.openChannelContent()
                }
            }
            "storebox" -> {
                runAction { v ->
                    v.openStoreBoxContent()
                }
            }
            "ekey" -> {

            }
        }
    }

    override fun onGetChannel(channel: Channel) {
        Timber.e("got the channel object: $channel")

        this.channel = channel

        // TODO remove me

        channel.requirements?.forEach { req ->
            req.verified = true
        }
        channel.installed = true

        // if channel is already installed we just open it
        if (channel.installed) {
            open(channel)
        }
        else    // else do the whole channel install shebang
        {
            runAction { v -> v.showProgress(false) }
            when (channel.status?.type) {
                APIConstants.CHANNEL_STATUS_AVAILABLE -> {
                    runAction { v -> v.showInstallState(channel) }
                }
                APIConstants.CHANNEL_STATUS_REQUIRES_VERIFIED -> {
                    runAction { v ->
                        // TODO this should trigger verification and use alternate providers for NOSE editions
                        Config.getLoginProvider("nemid")?.let {
                            v.showVerifyState(channel, it)
                        }
                    }
                }
                APIConstants.CHANNEL_STATUS_REQUIRES_HIGHER_SEC_LEVEL -> {
                    runAction { v -> v.showInstallState(channel) }
                }
                APIConstants.CHANNEL_STATUS_REQUIRES_HIGHER_SEC_LEVEL2 -> {
                    // TODO Deal with me
                    runAction { v -> v.showInstallState(channel) }
                }
                APIConstants.CHANNEL_STATUS_REQUIRES_NEW_VERSION -> {
                    // TODO Deal with me
                    runAction { v -> v.showDisabledState(channel) }
                }
                APIConstants.CHANNEL_STATUS_NOT_SUPPORTED -> {
                    runAction { v -> v.showDisabledState(channel) }
                }
                else -> {
                    runAction { v -> v.showDisabledState(channel) }
                }

            }
        }
    }

    override fun onGetChannelError(error: ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }

    override fun onStoreboxAccountCreated() {
        Timber.e("Debug account created")
        channel?.let { open(it) }
    }

    override fun onStoreboxAccountCreatedError(error: ViewError) {
        runAction { v->v.showErrorDialog(error) }
    }
}