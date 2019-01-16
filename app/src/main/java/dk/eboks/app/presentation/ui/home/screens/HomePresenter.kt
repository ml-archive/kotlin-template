package dk.eboks.app.presentation.ui.home.screens

import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.interactors.channel.GetChannelHomeContentInteractor
import dk.eboks.app.domain.interactors.message.GetMessagesInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.home.HomeContent
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.ui.home.components.channelcontrol.RefreshChannelControlDoneEvent
import dk.eboks.app.presentation.ui.home.components.folderpreview.RefreshFolderPreviewDoneEvent
import dk.nodes.arch.presentation.base.BasePresenterImpl
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class HomePresenter @Inject constructor(
    private val appState: AppStateManager,
    private val getChannelHomeContentInteractor: GetChannelHomeContentInteractor,
    private val getMessagesInteractor: GetMessagesInteractor
) : HomeContract.Presenter, BasePresenterImpl<HomeContract.View>(), GetChannelHomeContentInteractor.Output, GetMessagesInteractor.Output {
    override fun refresh() {

    }

    override var continueFetching: Boolean = false

    init {
    }

    override fun setup() {
        runAction { v ->
            v.addFolderPreview(Folder(type = FolderType.HIGHLIGHTS))
            v.addChannelControl()
        }
    }

    override fun onGetMessages(messages: List<Message>) {
        runAction { v->
            EventBus.getDefault().post(RefreshFolderPreviewDoneEvent())
            v.showFolderProgress(false)
            v.showFolder(messages, appState.state?.currentUser?.verified ?: false)
        }
    }

    override fun onGetMessagesError(error: ViewError) {
        runAction { v->
            EventBus.getDefault().post(RefreshFolderPreviewDoneEvent())
            v.showFolderProgress(false)
            v.showErrorDialog(error)
        }
    }

    override fun onGetChannelHomeContent(channel: Channel, content: HomeContent) {
        runAction { v -> v.updateControl(channel, content.control) }
    }

    override fun onGetChannelHomeContentDone() {
        EventBus.getDefault().post(RefreshChannelControlDoneEvent())
    }

    override fun onGetInstalledChannelList(channels: MutableList<Channel>) {
        runAction { v ->
            v.showChannelProgress(false)
            v.setupChannels(channels)
        }
    }

    override fun onGetInstalledChannelListError(error: ViewError) {
        Timber.e("onGetInstalledChannelListError")
        EventBus.getDefault().post(RefreshChannelControlDoneEvent())
        if (BuildConfig.DEBUG) //TODO Temp until backend is fixed
            runAction { v->v.showErrorDialog(error) }
    }

    override fun onGetChannelHomeContentError(channel: Channel) {
        Timber.e("onGetChannelHomeContentError")
        runAction { v -> v.setControl(channel, Translation.home.errorContentFetch) }
    }

    override fun onGetChannelHomeContentEmpty(channel: Channel) {
        Timber.e("onGetChannelHomeContentEmpty")
        runAction { v -> v.setControl(channel, Translation.home.noContentText) }
    }

    override fun continueGetChannelHomeContent(): Boolean {
        return continueFetching
    }


}