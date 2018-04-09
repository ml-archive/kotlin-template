package dk.eboks.app.presentation.ui.components.home

import dk.eboks.app.domain.interactors.channel.GetChannelHomeContentInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.home.HomeContent
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class HomeComponentPresenter @Inject constructor(val appState: AppStateManager, val getChannelHomeContentInteractor: GetChannelHomeContentInteractor) :
        HomeComponentContract.Presenter,
        BasePresenterImpl<HomeComponentContract.View>(),
        GetChannelHomeContentInteractor.Output
{

    init {
        getChannelHomeContentInteractor.output = this
    }

    override fun setup() {
        /*
        appState.state?.currentUser?.let { user->
            runAction { v->
                v.verifiedUser = user.verified
                v.setupViews()
            }
        }
        */
        getChannelHomeContentInteractor.run()
    }

    override fun onGetPinnedChannelList(channels: MutableList<Channel>) {
        Timber.e("Received list of ${channels.size} pinned channels")
        runAction { v->v.setupChannels(channels) }
    }

    override fun onGetChannelHomeContent(content: HomeContent) {
        Timber.e("Received channel content for channel id ${content.control.id}")
    }

    override fun onGetChannelHomeContentError(error: ViewError) {
        runAction { v->v.showErrorDialog(error) }
    }
}