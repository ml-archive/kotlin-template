package dk.eboks.app.presentation.ui.components.home.channelcontrol.controls

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.home.Control

class MessagesChannelControl(channel: Channel, control : Control, view: View, inflater : LayoutInflater, handler: Handler, val formatter: EboksFormatter) : ChannelControl(channel, control, view, inflater, handler) {

    
    override fun buildView() {
        control.items?.let { items ->
            for (currentItem in items) {
                val v = inflater.inflate(R.layout.viewholder_home_message, rowsContainerLl, false)
                val title = v.findViewById<TextView>(R.id.titleTv)
                val subtitle = v.findViewById<TextView>(R.id.subTitleTv)
                val date = v.findViewById<TextView>(R.id.dateTv)
                val image = v.findViewById<ImageView>(R.id.circleIv)
                val urgent = v.findViewById<TextView>(R.id.urgentTv)
                val bottomDivider = v.findViewById<View>(R.id.dividerV)
                val topDivider = v.findViewById<View>(R.id.topDividerV)

                bottomDivider.visibility = View.GONE
                topDivider.visibility = View.VISIBLE

                val currentStatus = currentItem.status
                if (currentStatus != null && currentStatus.important) {
                    urgent.visibility = View.VISIBLE
                    urgent.text = currentStatus.title
                }


                title.text = currentItem.title
                subtitle.text = currentItem.description
                date.text = formatter.formatDateRelative(currentItem)
                image.isSelected = true
                image?.let {
                    if (currentItem.image != null)
                        Glide.with(v.context).load(currentItem.image?.url).into(it)
                }

                rowsContainerLl.addView(v)
            }
        }
    }
}