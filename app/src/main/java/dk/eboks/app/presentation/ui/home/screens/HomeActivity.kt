package dk.eboks.app.presentation.ui.home.screens

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.home.Control
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.home.components.channelcontrol.ChannelControlComponentFragment
import dk.eboks.app.presentation.ui.home.components.channelcontrol.RefreshChannelControlDoneEvent
import dk.eboks.app.presentation.ui.home.components.channelcontrol.RefreshChannelControlEvent
import dk.eboks.app.presentation.ui.home.components.folderpreview.FolderPreviewComponentFragment
import dk.eboks.app.presentation.ui.home.components.folderpreview.RefreshFolderPreviewDoneEvent
import dk.eboks.app.presentation.ui.home.components.folderpreview.RefreshFolderPreviewEvent
import dk.eboks.app.presentation.ui.mail.screens.list.MailListActivity
import dk.eboks.app.presentation.ui.profile.screens.ProfileActivity
import dk.eboks.app.util.Starter
import dk.eboks.app.util.putArg
import dk.eboks.app.util.visible
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.include_toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

class HomeActivity : BaseActivity(), HomeContract.View {

    @Inject lateinit var presenter: HomeContract.Presenter

    private var folderPreviewFragment: FolderPreviewComponentFragment? = null
    private val channelControlFragment: ChannelControlComponentFragment
        get() = findFragment() ?: ChannelControlComponentFragment()
    private var doneRefreshingFolderPreview = false
    override fun onRefreshChannelDone() {
    }

    private var doneRefreshingChannelControls = false

    val onLanguageChange: (Locale) -> Unit = { locale ->
        Timber.e("Locale changed to locale")
        updateTranslation()
    }

    companion object {
        var refreshOnResume = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupTopBar()
        updateTranslation()

        refreshSrl.setOnRefreshListener {
            // fire event to signal ChannelControlComponent and FolderPreviewComponent to refresh
            doneRefreshingChannelControls = false
            doneRefreshingFolderPreview = false
            EventBus.getDefault().post(RefreshFolderPreviewEvent())
            EventBus.getDefault().post(RefreshChannelControlEvent())
        }

        showBtn.setOnClickListener {
            Starter().activity(MailListActivity::class.java)
                .putExtra("folder", Folder(type = FolderType.HIGHLIGHTS)).start()
        }
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
        if (refreshOnResume) {
            refreshOnResume = false
            mainHandler.postDelayed({
                // fire event to signal ChannelControlComponent and FolderPreviewComponent to refresh
                doneRefreshingChannelControls = false
                doneRefreshingFolderPreview = false
                EventBus.getDefault().post(RefreshFolderPreviewEvent())
                EventBus.getDefault().post(RefreshChannelControlEvent())
            }, 200)
        }
    }

    override fun onPause() {
        EventBus.getDefault().unregister(this)
        super.onPause()
    }

    private fun updateTranslation() {
        mainTb.title = Translation.home.windowHeader
    }

    // shamelessly ripped from chnt
    private fun setupTopBar() {
        val menuProfile = mainTb.menu.add(Translation.profile.title)
        menuProfile.setIcon(R.drawable.ic_profile)
        menuProfile.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuProfile.setOnMenuItemClickListener { item: MenuItem ->
            startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
            // overridePendingTransition(R.anim.slide_up, 0)
            true
        }
    }

    override fun getNavigationMenuAction(): Int {
        return R.id.actionHome
    }

    override fun addFolderPreview(folder: Folder) {
        folderPreviewFragment = FolderPreviewComponentFragment().putArg(
            Folder::class.java.simpleName,
            folder
        )
        folderPreviewFragment?.let {
            supportFragmentManager.beginTransaction()
                .add(R.id.folderPreviewFl, it, it::class.java.simpleName).commit()
        }
    }

    override fun addChannelControl() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.channelControlFl,
                channelControlFragment,
                channelControlFragment::class.java.simpleName
            ).commit()
    }

    override fun showMailsHeader(show: Boolean) {
        mailHeaderFl.visible = show
    }

    override fun showChannelControlsHeader(show: Boolean) {
        channelsHeaderFl.visible = show
    }

    private fun updateRefreshStatus() {
        if (doneRefreshingChannelControls && doneRefreshingFolderPreview) {
            if (refreshSrl.isRefreshing)
                refreshSrl.isRefreshing = false
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: RefreshFolderPreviewDoneEvent) {
        doneRefreshingFolderPreview = true
        updateRefreshStatus()
    }

    override fun onRefreshFolderDone() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: RefreshChannelControlDoneEvent) {
        doneRefreshingChannelControls = true
        updateRefreshStatus()
    }

    override fun showFolderProgress(show: Boolean) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun showFolder(messages: List<Message>, verifiedUser: Boolean) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun showChannelProgress(show: Boolean) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun setupChannels(channels: List<Channel>) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun updateControl(channel: Channel, control: Control) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun setControl(channel: Channel, text: String) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
