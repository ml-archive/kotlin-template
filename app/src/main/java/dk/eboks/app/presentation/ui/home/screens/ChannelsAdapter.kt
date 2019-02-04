package dk.eboks.app.presentation.ui.home.screens

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.util.inflate
import kotlinx.android.synthetic.main.viewholder_home_card_header.view.*
import java.lang.ref.WeakReference

class ChannelsAdapter(callback: ChannelsAdapter.Callback? = null) :
    RecyclerView.Adapter<ChannelsAdapter.ViewHolder>() {

    interface Callback {
        fun onChannelClick(channel: Channel)
    }

    private val callbackWeakReference = WeakReference(callback)

    private val list = mutableListOf<Channel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.viewholder_home_card_header))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemId(position: Int): Long {
        return list[position].id.toLong()
    }

    fun setData(channels: List<Channel>) {
        list.clear()
        list += channels
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(channel: Channel) {
            itemView.run {
                setOnClickListener {
                    callbackWeakReference.get()?.onChannelClick(channel)
                }
                headerTv.text = channel.name

                logoIv?.let {
                    channel.logo?.let { logo ->
                        Glide.with(context ?: return).load(logo.url).into(it)
                    }
                }

                tag = channel.id
                headerTv.text = channel.name
            }
        }
    }
}