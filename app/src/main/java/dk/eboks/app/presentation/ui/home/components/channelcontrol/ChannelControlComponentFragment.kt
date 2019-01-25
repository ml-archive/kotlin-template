package dk.eboks.app.presentation.ui.home.components.channelcontrol

import android.os.Bundle
import android.view.LayoutInflater
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
import dk.eboks.app.domain.models.home.Control
import dk.eboks.app.domain.models.home.ItemType
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.channels.screens.content.ChannelContentActivity
import dk.eboks.app.presentation.ui.home.components.channelcontrol.controls.ChannelControl
import dk.eboks.app.presentation.ui.home.components.channelcontrol.controls.FilesChannelControl
import dk.eboks.app.presentation.ui.home.components.channelcontrol.controls.ImagesChannelControl
import dk.eboks.app.presentation.ui.home.components.channelcontrol.controls.MessagesChannelControl
import dk.eboks.app.presentation.ui.home.components.channelcontrol.controls.NewsChannelControl
import dk.eboks.app.presentation.ui.home.components.channelcontrol.controls.NotificationsChannelControl
import dk.eboks.app.presentation.ui.home.components.channelcontrol.controls.ReceiptsChannelControl
import dk.eboks.app.presentation.ui.home.screens.HomeActivity
import dk.eboks.app.presentation.ui.navigation.components.NavBarComponentFragment
import dk.eboks.app.util.Starter
import dk.eboks.app.util.visible
import dk.eboks.app.util.views
import kotlinx.android.synthetic.main.fragment_channel_control_component.*
import kotlinx.android.synthetic.main.fragment_login_component.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ChannelControlComponentFragment : BaseFragment(), ChannelControlComponentContract.View {

    @Inject
    lateinit var presenter: ChannelControlComponentContract.Presenter

    @Inject
    lateinit var eboksFormatter: EboksFormatter

    val channelControlMap: MutableMap<Int, ChannelControl> = HashMap()

    // TODO channel controls empty state is dependent on info from the folderpreview in the top (for retarded design reasons)
    // find a clean way of getting this info to the right place (both run a the same time so maybe a countdown latch)
    var emailCount = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_channel_control_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        presenter.setup()
    }

    override fun onResume() {
        super.onResume()
        presenter.continueFetching = true
        EventBus.getDefault().register(this)
        if (refreshOnResume) {
            refreshOnResume = false
            presenter.refresh()
        }
    }

    override fun onPause() {
        EventBus.getDefault().unregister(this)
        Timber.d("TESTING ONPAUSE")
        presenter.continueFetching = false
        super.onPause()
    }

    override fun setupChannels(channels: MutableList<Channel>) {
        channelsContentLL.removeAllViews()
        channelControlMap.clear()

        for (i in 0..channels.size - 1) {
            val currentChannel = channels[i]

            //setting the header
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
            headerTv.text = "${currentChannel.name}"
            channelsContentLL.addView(v)
        }
        setupBottomView(channels)
    }

    private fun setupBottomView(channels: List<Channel>) {
        if (channels.size == 0) {
            (activity as HomeActivity).showChannelControlsHeader(false)
            teaserLl.visible = (false)
            emptyStateChannelLl.visibility = View.VISIBLE
            //bottomChannelBtn.isEnabled = (emailCount > 0)
            bottomChannelBtn.isEnabled = true
            bottomChannelHeaderTv.text = Translation.home.bottomChannelHeaderNoChannels
            bottomChannelTextTv.text = Translation.home.bottomChannelTextNoChannels
            bottomChannelBtn.visibility = View.VISIBLE
            bottomChannelHeaderTv.visibility = View.VISIBLE
            bottomChannelTextTv.visibility = View.VISIBLE
            bottomChannelBtn.setOnClickListener {
                NavBarComponentFragment.gotoChannels(activity ?: return@setOnClickListener)
            }
        } else {
            emptyStateChannelLl.visibility = View.GONE
            bottomChannelBtn.visibility = View.GONE
            bottomChannelHeaderTv.visibility = View.GONE
            bottomChannelTextTv.visibility = View.GONE
            teaserChannelBtn.setOnClickListener {
                NavBarComponentFragment.gotoChannels(activity ?: return@setOnClickListener)
            }
            teaserLl.visible = (true)

        }
    }

    override fun showProgress(show: Boolean) {
        progressChannelFl.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun findControlView(channelId: Int): View? {
        for (v in channelsContentLL.views) {
            if (v.tag as Int == channelId) {
                return v
            }
        }
        return null
    }

    override fun updateControl(channel: Channel, control: Control) {
        // find the view associated with the channel
        findControlView(channel.id)?.let { view ->
            if (channelControlMap.containsKey(channel.id)) // already instantiated, we're updating
            {
                Timber.e("Already instantiated should update")
            } else    // widget not yet instantiated, do that and more
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
            //channelsContentLL.removeView(view)
        }
    }

    fun instantiateChannelControl(channel: Channel, control: Control, view: View): ChannelControl? {
        when (control.type) {
            ItemType.RECEIPTS -> {
                return ReceiptsChannelControl(channel, control, view, inflator, mainHandler, eboksFormatter)
            }
            ItemType.NEWS -> {
                return NewsChannelControl(channel, control, view, inflator, mainHandler, eboksFormatter)
            }
            ItemType.IMAGES -> {
                return ImagesChannelControl(channel, control, view, inflator, mainHandler, eboksFormatter)
            }
            ItemType.NOTIFICATIONS -> {
                return NotificationsChannelControl(channel, control, view, inflator, mainHandler, eboksFormatter)
            }
            ItemType.MESSAGES -> {
                return MessagesChannelControl(channel, control, view, inflator, mainHandler, eboksFormatter)
            }
            ItemType.FILES -> {
                return FilesChannelControl(channel, control, view, inflator, mainHandler, eboksFormatter)
            }
        }
        return null
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: RefreshChannelControlEvent) {
        presenter.refresh()
    }

    companion object {
        var refreshOnResume = false
    }
}