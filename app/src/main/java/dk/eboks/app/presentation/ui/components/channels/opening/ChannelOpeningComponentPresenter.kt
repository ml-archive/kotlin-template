package dk.eboks.app.presentation.ui.components.channels.opening

import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.config.LoginProvider
import dk.eboks.app.domain.interactors.channel.GetChannelInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.channel.Channel
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ChannelOpeningComponentPresenter @Inject constructor(val appState: AppStateManager, val getChannelInteractor: GetChannelInteractor) :
        ChannelOpeningComponentContract.Presenter,
        BasePresenterImpl<ChannelOpeningComponentContract.View>(),
        GetChannelInteractor.Output
{

    init {
        appState.state?.channelState?.selectedChannel?.let { channel ->
            getChannelInteractor.output = this
            getChannelInteractor.input = GetChannelInteractor.Input(channel.id.toLong())
            getChannelInteractor.run()
            runAction {
                v-> v.showProgress(true)
            }
        }
    }

    override fun install(channel: Channel, provider : LoginProvider) {
        runAction { v->
            //v.showProgress(true)
            v.showVerifyState(channel, provider)
        }
    }

    override fun open(channel: Channel)
    {
        runAction { v->v.openChannelContent() }
    }

    override fun onGetChannel(channel: Channel) {
        runAction { v->v.showProgress(false) }
        if(appState.state?.channelState?.openOrInstallImmediately == false) {
            if (channel.installed) {
                runAction { v -> v.showOpenState(channel) }
            } else
                runAction { v -> v.showInstallState(channel) }
        }
        else
        {
            if (!channel.installed) {
                runAction { v ->
                    Config.getLoginProvider("nemid")?.let {
                        v.showVerifyState(channel, it)
                    }

                }
            } else
                runAction { v -> v.openChannelContent() }
        }
    }

    override fun onGetChannelError(msg: String) {
        runAction { v->v.showProgress(false) }
        // TODO error management
        Timber.e(msg)
    }
}