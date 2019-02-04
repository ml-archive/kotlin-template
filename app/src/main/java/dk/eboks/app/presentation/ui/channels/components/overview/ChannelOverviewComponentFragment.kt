package dk.eboks.app.presentation.ui.channels.components.overview

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.channel.ChannelColor
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.channels.screens.content.ChannelContentActivity
import dk.eboks.app.util.Starter
import dk.eboks.app.util.inflate
import dk.eboks.app.util.visible
import kotlinx.android.synthetic.main.fragment_channel_list_component.*
import kotlinx.android.synthetic.main.viewholder_channel_cards.view.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ChannelOverviewComponentFragment : BaseFragment(), ChannelOverviewComponentContract.View {

    var cards: MutableList<Channel> = ArrayList()

    @Inject
    lateinit var presenter: ChannelOverviewComponentContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_channel_list_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        setupRecyclerView()
        refreshSrl.setOnRefreshListener {
            presenter.refresh(false) // manually initiated refresh should never emit cached data
        }

        presenter.setup()
    }

    override fun showProgress(show: Boolean) {
        progressFl.visible = (show)
        refreshSrl.visible = (!show)
        if (!show) {
            refreshSrl.isRefreshing = false
        }
    }

    private fun setupRecyclerView() {
        channelRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        channelRv.adapter = ChannelAdapter()
    }

    override fun showChannelOpening(channel: Channel) {
        // storebox channels id 1 - 3
        // ekey channels id 101 - 103

        activity?.run {
            Starter().activity(ChannelContentActivity::class.java)
                .putExtra(Channel::class.java.simpleName, channel).start()
        }

        // addFragmentOnTop(R.id.containerFl, ChannelOpeningComponentFragment().putArg(Channel::class.java.simpleName, channel), false)

        /*
        when (channel.getType()) {
            "channel"  -> {
                //activity.Starter().activity(ChannelContentActivity::class.java).putExtra(Channel::class.java.simpleName, channel).start()
            }
            "storebox" -> {
                // TODO SWITCH BACK TO NORMAL ONCE API IS WORKING
                //activity.Starter().activity(StoreboxContentActivity::class.java).putExtra(Channel::class.java.simpleName, channel).start()
            }
            "ekey"     -> {
                if (channel.installed) {
                    //todo should check if we have mastervault then go straight to activity
                    //activity.Starter().activity(EkeyContentActivity::class.java).putExtra(Channel::class.java.simpleName, channel).start()
                } else {
                    //todo api call to get vault
                    //todo should maybe move EkeyPinComponentFragment to its own activity ?
                    //activity.Starter().activity(EkeyContentActivity::class.java).putExtra(Channel::class.java.simpleName, channel).putExtra("showPin",true).start()
                }
            }
        }
        */
    }

    inner class ChannelAdapter : RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
            return ChannelViewHolder(parent.inflate(R.layout.viewholder_channel_cards))
        }

        override fun getItemCount(): Int {
            return cards.size
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
            holder.bind(cards[position])
        }

        inner class ChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(channel: Channel) {
                itemView.run {
                    if (channel.id == -1) {
                        headerTv.visibility = View.VISIBLE
                        cardContainerCv.visibility = View.GONE
                        headerTv.text = Translation.channels.channelsHeader
                    } else {
                        headerTv.visibility = View.GONE
                        cardContainerCv.visibility = View.VISIBLE

//                holder.backgroundColorV?.background?.setTint(channel.background.color)
                        backgroundColorV?.background?.let { d ->
                            d.clearColorFilter()
                            d.setColorFilter(
                                channel.background.color,
                                PorterDuff.Mode.MULTIPLY
                            )
                        }
                        Glide.with(context ?: return)
                            .load(channel.image?.url)
                            .apply(RequestOptions().transform(RoundedCorners(15)))
                            .into(backgroundIv)
                        backgroundIv.alpha = 0.2f

                        if (channel.logo != null) {
                            logoIv?.let {
                                Glide.with(context ?: return).load(channel.logo?.url)
                                    .into(it)
                            }
                        }

                        headlineTv?.text = channel.payoff

                        nameTv.text = channel.name

                        if (channel.installed) {
                            openActionTv.text = Translation.channels.open
                        } else {
                            openActionTv.text = Translation.channels.install
                        }

                        cardContainerCv.isClickable = true
                        cardContainerCv.setOnTouchListener { v, event ->
                            if (event.action == MotionEvent.ACTION_DOWN) {
                                v.animate()
                                    .scaleX(0.98f)
                                    .scaleY(0.95f)
                                    .setDuration(60)
                                    .setInterpolator(LinearOutSlowInInterpolator())
                                    .start()
                            }
                            if (event.action == MotionEvent.ACTION_UP) {
                                v.animate()
                                    .scaleX(1.00f)
                                    .scaleY(1.00f)
                                    .setDuration(120)
                                    .setInterpolator(FastOutSlowInInterpolator())
                                    .start()
                            }
                            if (event.action == MotionEvent.ACTION_CANCEL) {
                                v.animate()
                                    .scaleX(1.00f)
                                    .scaleY(1.00f)
                                    .setDuration(450)
                                    .setInterpolator(FastOutSlowInInterpolator())
                                    .start()
                            }
                            return@setOnTouchListener v.onTouchEvent(event)
                        }

                        cardContainerCv.setOnClickListener { v ->
                            presenter.openChannel(channel)
                        }
                    }
                    invalidate()
                }
            }
        }
    }

    override fun showChannels(channels: List<Channel>) {

        // adding header card added to the top of the list
        cards.clear()
        cards.add(Channel(-1, "", "", null, null, null, null, ChannelColor(), null, false, null))
        // addings channels
        for (channel in channels) {
            cards.add(channel)
        }
        channelRv.adapter?.notifyDataSetChanged()
    }
}