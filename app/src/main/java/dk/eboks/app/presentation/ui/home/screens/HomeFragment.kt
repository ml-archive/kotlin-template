package dk.eboks.app.presentation.ui.home.screens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.home.Control
import dk.eboks.app.domain.models.home.ItemType
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.channels.screens.content.ChannelContentActivity
import dk.eboks.app.presentation.ui.home.components.channelcontrol.ChannelControlComponentFragment
import dk.eboks.app.presentation.ui.home.components.channelcontrol.controls.ChannelControl
import dk.eboks.app.presentation.ui.home.components.channelcontrol.controls.FilesChannelControl
import dk.eboks.app.presentation.ui.home.components.channelcontrol.controls.ImagesChannelControl
import dk.eboks.app.presentation.ui.home.components.channelcontrol.controls.MessagesChannelControl
import dk.eboks.app.presentation.ui.home.components.channelcontrol.controls.NewsChannelControl
import dk.eboks.app.presentation.ui.home.components.channelcontrol.controls.NotificationsChannelControl
import dk.eboks.app.presentation.ui.home.components.channelcontrol.controls.ReceiptsChannelControl
import dk.eboks.app.presentation.ui.login.components.verification.VerificationComponentFragment
import dk.eboks.app.presentation.ui.main.MainNavigator
import dk.eboks.app.presentation.ui.main.Section
import dk.eboks.app.presentation.ui.message.screens.opening.MessageOpeningActivity
import dk.eboks.app.presentation.ui.profile.screens.ProfileActivity
import dk.eboks.app.util.Starter
import dk.eboks.app.util.views
import dk.eboks.app.util.visible
import kotlinx.android.synthetic.main.fragment_channel_control_component.*
import kotlinx.android.synthetic.main.fragment_folder_preview_component.*
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber
import javax.inject.Inject

class HomeFragment : BaseFragment(), HomeContract.View, ChannelsAdapter.Callback,
    MessageAdapter.Callback {

    @Inject lateinit var presenter: HomeContract.Presenter
    @Inject lateinit var eboksFormatter: EboksFormatter
    private lateinit var messageAdapter: MessageAdapter
    private val channelControlMap = mutableMapOf<Int, ChannelControl>()
    private var doneRefreshingFolderPreview = false
    private var doneRefreshingChannelControls = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home, menu)
    }

    override fun onResume() {
        super.onResume()
        presenter.continueFetching = true
        if (ChannelControlComponentFragment.refreshOnResume) {
            ChannelControlComponentFragment.refreshOnResume = false
            presenter.refresh()
        }
    }

    override fun onPause() {

        Timber.d("TESTING ONPAUSE")
        presenter.continueFetching = false
        super.onPause()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_profile) {
            startActivity(Intent(context ?: return false, ProfileActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageAdapter = MessageAdapter(eboksFormatter, this)
        presenter.onViewCreated(this, lifecycle)
        refreshSrl.setOnRefreshListener {
            doneRefreshingChannelControls = false
            doneRefreshingFolderPreview = false
        }
    }

    override fun addFolderPreview(folder: Folder) {
    }

    override fun addChannelControl() {
    }

    override fun showMailsHeader(show: Boolean) {
        mailHeaderFl.visible = show
    }

    override fun showChannelControlsHeader(show: Boolean) {
        channelsHeaderFl.visible = show
    }

    override fun showFolderProgress(show: Boolean) {
        progressFolderFl.visible = show
    }

    override fun onRefreshFolderDone() {
        doneRefreshingFolderPreview = true
        updateRefreshStatus()
    }

    override fun onRefreshChannelDone() {
        doneRefreshingChannelControls = true
        updateRefreshStatus()
    }

    override fun showFolder(messages: List<Message>, verifiedUser: Boolean) {
        if (messages.isEmpty()) {
            showEmptyState(true, verifiedUser)
            return
        } else {
            showEmptyState(false, verifiedUser)
        }
    }

    override fun showChannelProgress(show: Boolean) {
        progressChannelFl.visible = show
    }

    override fun setupChannels(channels: List<Channel>) {
        channelsContentLL.removeAllViews()
        channelControlMap.clear()

        for (i in 0 until channels.size) {
            val currentChannel = channels[i]

            // setting the header
            val v = inflator.inflate(R.layout.viewholder_home_card_header, channelsContentLL, false)
            val logoIv = v.findViewById<ImageView>(R.id.logoIv)
            val headerTv = v.findViewById<TextView>(R.id.headerTv)
            val cardView = v.findViewById<androidx.cardview.widget.CardView>(R.id.channelItemCv)
            cardView?.setOnClickListener {
                activity?.run {
                    Starter().activity(ChannelContentActivity::class.java)
                        .putExtra("openDirectly", true)
                        .putExtra(Channel::class.java.simpleName, currentChannel)
                        .start()
                }
            }
            headerTv.text = currentChannel.name

            logoIv?.let {
                currentChannel.logo?.let { logo ->
                    Glide.with(context ?: return).load(logo.url).into(it)
                }
            }

            v.tag = currentChannel.id
            headerTv.text = currentChannel.name
            channelsContentLL.addView(v)
        }
        if (channels.isEmpty()) {
            showChannelControlsHeader(false)
            teaserLl.visible = (false)
            emptyStateChannelLl.visibility = View.VISIBLE
            // bottomChannelBtn.isEnabled = (emailCount > 0)
            bottomChannelBtn.isEnabled = true
            bottomChannelHeaderTv.text = Translation.home.bottomChannelHeaderNoChannels
            bottomChannelTextTv.text = Translation.home.bottomChannelTextNoChannels
            bottomChannelBtn.visibility = View.VISIBLE
            bottomChannelHeaderTv.visibility = View.VISIBLE
            bottomChannelTextTv.visibility = View.VISIBLE
            bottomChannelBtn.setOnClickListener {
                (activity as? MainNavigator)?.showMainSection(Section.Channels)
            }
        } else {
            emptyStateChannelLl.visibility = View.GONE
            bottomChannelBtn.visibility = View.GONE
            bottomChannelHeaderTv.visibility = View.GONE
            bottomChannelTextTv.visibility = View.GONE
            teaserChannelBtn.setOnClickListener {
                (activity as? MainNavigator)?.showMainSection(Section.Channels)
            }
            teaserLl.visible = (true)
        }
    }

    override fun updateControl(channel: Channel, control: Control) {
        findControlView(channel.id)?.let { view ->
            if (channelControlMap.containsKey(channel.id)) // already instantiated, we're updating
            {
                Timber.e("Already instantiated should update")
            } else // widget not yet instantiated, do that and more
            {
                val cc = instantiateChannelControl(channel, control, view)
                cc?.let {
                    it.buildView()
                    channelControlMap[channel.id] = it
                    it.showProgress(false)
                }
            }
        }
    }

    private fun instantiateChannelControl(
        channel: Channel,
        control: Control,
        view: View
    ): ChannelControl? {
        when (control.type) {
            ItemType.RECEIPTS -> {
                return ReceiptsChannelControl(
                    channel,
                    control,
                    view,
                    inflator,
                    mainHandler,
                    eboksFormatter
                )
            }
            ItemType.NEWS -> {
                return NewsChannelControl(
                    channel,
                    control,
                    view,
                    inflator,
                    mainHandler,
                    eboksFormatter
                )
            }
            ItemType.IMAGES -> {
                return ImagesChannelControl(
                    channel,
                    control,
                    view,
                    inflator,
                    mainHandler,
                    eboksFormatter
                )
            }
            ItemType.NOTIFICATIONS -> {
                return NotificationsChannelControl(
                    channel,
                    control,
                    view,
                    inflator,
                    mainHandler,
                    eboksFormatter
                )
            }
            ItemType.MESSAGES -> {
                return MessagesChannelControl(
                    channel,
                    control,
                    view,
                    inflator,
                    mainHandler,
                    eboksFormatter
                )
            }
            ItemType.FILES -> {
                return FilesChannelControl(
                    channel,
                    control,
                    view,
                    inflator,
                    mainHandler,
                    eboksFormatter
                )
            }
        }
        return null
    }

    private fun findControlView(channelId: Int): View? {
        for (v in channelsContentLL.views) {
            if (v.tag as? Int == channelId) {
                return v
            }
        }
        return null
    }

    override fun setControl(channel: Channel, text: String) {
        // find the view associated with the channel
        findControlView(channel.id)?.let { view ->
            Timber.e("Setting empty view for channel id ${channel.id}")
            val logoIv = view.findViewById<ImageView>(R.id.logoIv)
            val errorTv = view.findViewById<TextView>(R.id.errorTextTv)
            val progressPb = view.findViewById<ProgressBar>(R.id.progressPb)
            progressPb.visible = (false)
            logoIv.visible = (true)
            errorTv.text = text
            errorTv.visible = (true)
            // channelsContentLL.removeView(view)
        }
    }

    private fun showEmptyState(show: Boolean, verifiedUser: Boolean) {
        emptyStateLl.visible = show
        mailListContentLL.visible = !show

        // show the header in the parent activity if said activity is HomeActivity
        showMailsHeader(!show)

        if (show) {
            if (verifiedUser) {
                emptyStateBtn.text = Translation.home.messagesEmptyButton
                emptyStateHeaderTv.text = Translation.home.messagesEmptyTitle
                emptyStateTextTv.text = Translation.home.messagesEmptyMessage
                emptyStateBtn.setOnClickListener {
                    (activity as? MainNavigator)?.showMainSection(Section.Mail)
                }
            } else {
                emptyStateBtn.text = Translation.home.messagesUnverifiedButton
                emptyStateHeaderTv.text = Translation.home.messagesUnverifiedTitle
                emptyStateTextTv.text = Translation.home.messagesUnverifiedMessage
                emptyStateBtn.setOnClickListener {
                    HomeActivity.refreshOnResume = true
                    VerificationComponentFragment.verificationSucceeded = false
                    getBaseActivity()?.openComponentDrawer(VerificationComponentFragment::class.java)
                }
            }
        }
    }

    private fun updateRefreshStatus() {
        if (doneRefreshingChannelControls && doneRefreshingFolderPreview) {
            if (refreshSrl.isRefreshing)
                refreshSrl.isRefreshing = false
        }
    }

    override fun onChannelClick(channel: Channel) {
        activity?.run {
            Starter().activity(ChannelContentActivity::class.java)
                .putExtra("openDirectly", true)
                .putExtra(Channel::class.java.simpleName, channel)
                .start()
        }
    }

    override fun onMessageClick(message: Message) {
        activity?.run {
            Starter()
                .activity(MessageOpeningActivity::class.java)
                .putExtra(Message::class.java.simpleName, message)
                .start()
        }
    }
}
