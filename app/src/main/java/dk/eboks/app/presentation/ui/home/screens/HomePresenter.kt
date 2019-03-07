package dk.eboks.app.presentation.ui.home.screens

import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.interactors.channel.GetChannelHomeContentInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.home.HomeContent
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.mail.domain.interactors.message.GetMessagesInteractor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class HomePresenter @Inject constructor(
    private val appState: AppStateManager,
    private val getChannelHomeContentInteractor: GetChannelHomeContentInteractor,
    private val getMessagesInteractor: GetMessagesInteractor
) : HomeContract.Presenter, BasePresenterImpl<HomeContract.View>(),
    GetChannelHomeContentInteractor.Output, GetMessagesInteractor.Output {
    override var continueFetching: Boolean = false

    init {
        getChannelHomeContentInteractor.output = this
        getMessagesInteractor.output = this
        fetch(true)
    }

    private fun fetch(cached: Boolean) {
        getChannelHomeContentInteractor.input = GetChannelHomeContentInteractor.Input(cached)
        getMessagesInteractor.input =
            GetMessagesInteractor.Input(cached, folder = Folder(type = FolderType.HIGHLIGHTS))
        getMessagesInteractor.run()
        getChannelHomeContentInteractor.run()
    }

    override fun refresh() {
        fetch(false)
    }

    override fun onGetMessages(messages: List<Message>) {
        view {
            onRefreshFolderDone()
            showFolderProgress(false)
            showFolder(messages, appState.state?.currentUser?.verified ?: false)
        }
    }

    override fun onGetMessagesError(error: ViewError) {
        view {
            onRefreshFolderDone()
            showFolderProgress(false)
            showErrorDialog(error)
        }
    }

    override fun onGetChannelHomeContent(channel: Channel, content: HomeContent) {
        view { updateControl(channel, content.control) }
    }

    override fun onGetChannelHomeContentDone() {
        view { onRefreshChannelDone() }
    }

    override fun onGetInstalledChannelList(channels: List<Channel>) {
        view {
            showChannelProgress(false)
            setupChannels(channels)
        }
    }

    override fun onGetInstalledChannelListError(error: ViewError) {
        Timber.e("onGetInstalledChannelListError")
        view {
            if (BuildConfig.DEBUG) // TODO Temp until backend is fixed
                showErrorDialog(error)
            onRefreshChannelDone()
        }
    }

    override fun onGetChannelHomeContentError(channel: Channel) {
        Timber.e("onGetChannelHomeContentError")
        view { setControl(channel, Translation.home.errorContentFetch) }
    }

    override fun onGetChannelHomeContentEmpty(channel: Channel) {
        Timber.d("onGetChannelHomeContentEmpty")
        view { setControl(channel, Translation.home.noContentText) }
    }

    override fun continueGetChannelHomeContent(): Boolean {
        return continueFetching
    }
}