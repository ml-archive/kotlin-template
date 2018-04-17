package dk.eboks.app.presentation.ui.components.home

import dk.eboks.app.domain.interactors.channel.GetChannelHomeContentInteractor
import dk.eboks.app.domain.interactors.message.GetMessagesInteractor
import dk.eboks.app.domain.interactors.message.OpenMessageInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.home.HomeContent
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class HomeComponentPresenter @Inject constructor(val appState: AppStateManager, val getChannelHomeContentInteractor: GetChannelHomeContentInteractor,
                                                 val getMessagesInteractor: GetMessagesInteractor, val openMessageInteractor: OpenMessageInteractor) :
        HomeComponentContract.Presenter,
        BasePresenterImpl<HomeComponentContract.View>(),
        GetChannelHomeContentInteractor.Output,
        GetMessagesInteractor.Output,
        OpenMessageInteractor.Output {

    init {
        getChannelHomeContentInteractor.output = this
        getMessagesInteractor.output = this
        openMessageInteractor.output = this
    }

    override fun setup() {

        Timber.e("setup() ------------------------------------------------------")
        appState.state?.currentUser?.let { user ->
            runAction { v ->
                v.verifiedUser = user.verified
            }
        }

        getMessagesInteractor.input = GetMessagesInteractor.Input(true, Folder(type = FolderType.HIGHLIGHTS))
        getMessagesInteractor.run()
        //getChannelHomeContentInteractor.input = GetChannelHomeContentInteractor.Input(true)
        getChannelHomeContentInteractor.run()
    }

    override fun refresh() {
        Timber.e("refresh() ----------------------------------------------------")
        getMessagesInteractor.input = GetMessagesInteractor.Input(false, Folder(type = FolderType.HIGHLIGHTS))
        getMessagesInteractor.run()
        getChannelHomeContentInteractor.run()
    }


    override fun openMessage(message: Message) {
        runAction { v ->
            v.showRefreshProgress(false)
            v.showProgress(true)
        }
        openMessageInteractor.input = OpenMessageInteractor.Input(message)
        openMessageInteractor.run()
    }

    override fun onGetPinnedChannelList(channels: MutableList<Channel>) {
        //Timber.e("Received list of ${channels.size} pinned channels")
        runAction { v ->
            v.showRefreshProgress(false)
            v.setupChannels(channels)
        }
    }

    override fun onGetChannelHomeContent(channel: Channel, content: HomeContent) {
        //Timber.e("Received channel content for channel id ${content.control.id}")
        runAction { v ->
            v.showRefreshProgress(false)
            v.setupChannelControl(channel, content.control)
        }
    }

    override fun onGetChannelHomeContentError(error: ViewError) {
        runAction { v ->
            v.showRefreshProgress(false)
            v.showErrorDialog(error)
        }
    }

    // messages
    override fun onGetMessages(messages: List<Message>) {
        Timber.e("onGetMessages")
        runAction { v ->
            v.showRefreshProgress(false)
            v.showHighlights(messages)
        }
    }

    override fun onGetMessagesError(error: ViewError) {
        runAction { v ->
            v.showRefreshProgress(false)
            v.showErrorDialog(error)
        }
    }

    // open message
    override fun onOpenMessageDone() {
        runAction { v ->
            v.showRefreshProgress(false)
            v.showProgress(false) }
    }

    override fun onOpenMessageError(error: ViewError) {
        runAction { v ->
            v.showRefreshProgress(false)
            v.showErrorDialog(error)
            v.showProgress(false)
        }
    }
}