package dk.eboks.app.presentation.ui.components.channels.opening

import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.interactors.channel.GetChannelInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.APIConstants
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.util.getType
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ChannelOpeningComponentPresenter @Inject constructor(val appState: AppStateManager, val getChannelInteractor: GetChannelInteractor) :
        ChannelOpeningComponentContract.Presenter,
        BasePresenterImpl<ChannelOpeningComponentContract.View>(),
        GetChannelInteractor.Output {


    var channelId : Int = 0

    override fun setup(channelId: Int) {
        this.channelId = channelId
        refreshChannel()
    }

    override fun refreshChannel() {
        getChannelInteractor.output = this
        getChannelInteractor.input = GetChannelInteractor.Input(channelId)
        getChannelInteractor.run()
        runAction { v ->
            v.showProgress(true)
        }
    }

    override fun install(channel: Channel) {
        runAction { v ->
            //v.showProgress(true)
            v.showVerifyDrawer(channel)
        }
    }

    override fun open(channel: Channel) {
        //storebox channels id 1 - 3
        //ekey channels id 101 - 103

        when (channel.getType()) {
            "channel" -> {
                runAction { v ->
                    v.openChannelContent() }
            }
            "storebox" -> {
                runAction { v ->
                    v.openStoreBoxContent() }
            }
            "ekey" -> {

            }
        }
//        if (channel.isStorebox()) {
//            runAction { v -> v.openStoreBoxContent() }
//        } else {
//            runAction { v -> v.openChannelContent() }
//        }
    }

    override fun onGetChannel(channel: Channel) {
        Timber.e("got the channel object: $channel")
        runAction { v -> v.showProgress(false) }
        if (channel.installed) {
            runAction { v -> v.goToWebView(channel) }
        } else {
            when (channel.status?.type) {
                APIConstants.CHANNEL_STATUS_AVAILABLE -> {
                    runAction { v -> v.showInstallState(channel) }
                }
                APIConstants.CHANNEL_STATUS_REQUIRES_VERIFIED -> {
                    runAction { v ->
                        // TODO this should trigger verification
                        /*
                        Config.getLoginProvider("nemid")?.let {
                            v.showVerifyState(channel, it)
                        }
                        */
                    }
                }
                APIConstants.CHANNEL_STATUS_REQUIRES_HIGHER_SEC_LEVEL -> {
                    runAction { v -> v.showDisabledState(channel) }
                }
                APIConstants.CHANNEL_STATUS_REQUIRES_HIGHER_SEC_LEVEL2 -> {
                    // TODO Deal with me
                }
                APIConstants.CHANNEL_STATUS_REQUIRES_NEW_VERSION -> {
                    // TODO Deal with me
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
}