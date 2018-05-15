package dk.eboks.app.presentation.ui.components.home.channelcontrol

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_channel_control_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ChannelControlComponentFragment : BaseFragment(), ChannelControlComponentContract.View {

    @Inject
    lateinit var presenter : ChannelControlComponentContract.Presenter

    var emailCount = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_channel_control_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        presenter.setup()
    }

    override fun setupChannels(channels: MutableList<Channel>) {
        channelsContentLL.removeAllViews()
        for (i in 0..channels.size - 1) {
            val currentChannel = channels[i]
            //channelCount = channels.size

            //setting the header
            val v = inflator.inflate(R.layout.viewholder_home_card_header, channelsContentLL, false)
            val logoIv = v.findViewById<ImageView>(R.id.logoIv)
            val headerTv = v.findViewById<TextView>(R.id.headerTv)
            //val rowsContainerLl = v.findViewById<LinearLayout>(R.id.rowsContainerLl)

            logoIv?.let {
                currentChannel?.logo?.let { logo ->
                    Glide.with(context).load(logo.url).into(it)
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

            bottomChannelBtn.isEnabled = (emailCount > 0)
            bottomChannelHeaderTv.text = Translation.home.bottomChannelHeaderNoChannels
            bottomChannelTextTv.text = Translation.home.bottomChannelTextNoChannels
            bottomChannelBtn.visibility = View.VISIBLE
            bottomChannelHeaderTv.visibility = View.VISIBLE
            bottomChannelTextTv.visibility = View.VISIBLE
        } else {
            if (channels.size < 2) {
                bottomChannelBtn.isEnabled = false
                bottomChannelHeaderTv.text = Translation.home.bottomChannelHeaderChannels
                bottomChannelTextTv.text = Translation.home.bottomChannelTextChannels
            } else {
                bottomChannelBtn.visibility = View.GONE
                bottomChannelHeaderTv.visibility = View.GONE
                bottomChannelTextTv.visibility = View.GONE
            }

        }
    }

    override fun showProgress(show: Boolean) {
        progressFl.visibility = if(show) View.VISIBLE else View.GONE
    }

}