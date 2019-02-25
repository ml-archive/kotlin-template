package dk.eboks.app.presentation.ui.channels.components.opening

import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.interactors.channel.GetChannelInteractor
import dk.eboks.app.domain.interactors.channel.InstallChannelInteractor
import dk.eboks.app.domain.interactors.storebox.CreateStoreboxInteractor
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
class ChannelOpeningComponentPresenter @Inject constructor(
    private val getChannelInteractor: GetChannelInteractor,
    private val createStoreboxInteractor: CreateStoreboxInteractor,
    private val installChannelInteractor: InstallChannelInteractor,
    private val appConfig: AppConfig
) :
    ChannelOpeningComponentContract.Presenter,
    BasePresenterImpl<ChannelOpeningComponentContract.View>(),
    GetChannelInteractor.Output,
    CreateStoreboxInteractor.Output,
    InstallChannelInteractor.Output {
    var channelId: Int = 0
    var channel: Channel? = null

    init {
        getChannelInteractor.output = this
        createStoreboxInteractor.output = this
        installChannelInteractor.output = this
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
            // v.showChannelProgress(true)
            // do we have any requirements, if so are they all verified? if not show the requirements drawer
            if (!channel.areAllRequirementsVerified()) {
                v.showRequirementsDrawer(channel)
            } else {
                when (channel.getType()) {
                    "channel" -> {
                        installChannelInteractor.input = InstallChannelInteractor.Input(channel.id)
                        runAction { v -> v.showProgress(true) }
                        installChannelInteractor.run()
                    }
                    "storebox" -> {
                        createStoreboxInteractor.run()
                    }
                    "ekey" -> {
                        installChannelInteractor.input = InstallChannelInteractor.Input(channel.id)
                        runAction { v -> v.showProgress(true) }
                        installChannelInteractor.run()
                    }
                }
            }
        }
    }

    override fun open(channel: Channel) {
        // storebox channels id 1 - 3
        // ekey channels id 101 - 103

        when (channel.getType()) {
            "channel" -> {
                runAction { v ->
                    v.openChannelContent(channel)
                }
            }
            "storebox" -> {
                runAction { v ->
                    v.openStoreBoxContent(channel)
                }
            }
            "ekey" -> {
                runAction { v ->
                    v.openEkeyContent(channel)
                }
            }
        }
    }

    override fun onGetChannel(channel: Channel) {
        Timber.e("got the channel object: $channel")

        this.channel = channel

        // if channel is already installed we just open it
        if (channel.installed) {
            open(channel)
        } else // else do the whole channel install shebang
        {
            runAction { v -> v.showProgress(false) }
            when (channel.status?.type) {
                APIConstants.CHANNEL_STATUS_AVAILABLE -> {
                    runAction { v -> v.showInstallState(channel) }
                }
                APIConstants.CHANNEL_STATUS_REQUIRES_VERIFIED -> {
                    runAction { v ->
                        appConfig.verificationProviderId?.let { id ->
                            appConfig.getLoginProvider(id)?.let {
                                v.showVerifyState(channel, it)
                            }
                        }
                    }
                }
                APIConstants.CHANNEL_STATUS_REQUIRES_HIGHER_SEC_LEVEL -> {
                    runAction { v -> v.showDisabledState(channel) }
                }
                APIConstants.CHANNEL_STATUS_REQUIRES_HIGHER_SEC_LEVEL2 -> {
                    // TODO Deal with me
                    runAction { v -> v.showDisabledState(channel) }
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
        runAction { v -> v.showErrorDialog(error) }
    }

    override fun onStoreboxAccountExists() {
        runAction { v -> v.showStoreboxUserAlreadyExists() }
    }

    /**
     * InstallChannelInteractor callbacks
     */
    override fun onInstallChannel() {
        runAction { v -> v.showProgress(false) }
        channel?.let { open(it) }
    }

    override fun onInstallChannelError(error: ViewError) {
        runAction { v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }
}