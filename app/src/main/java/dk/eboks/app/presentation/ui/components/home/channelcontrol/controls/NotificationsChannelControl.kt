package dk.eboks.app.presentation.ui.components.home.channelcontrol.controls

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.home.Control

class NotificationsChannelControl(channel: Channel, control : Control, view: View, inflater : LayoutInflater, handler: Handler, val formatter: EboksFormatter) : ChannelControl(channel, control, view, inflater, handler) {
    override fun buildView() {
        control.items?.let { items ->
            if(items.isEmpty())
            {
                val v = inflater.inflate(R.layout.viewholder_home_notification_row, rowsContainerLl, false)
                val emptyContainer = v.findViewById<LinearLayout>(R.id.emptyContentContainer)
                val contentContainer = v.findViewById<LinearLayout>(R.id.contentContainer)
                emptyContainer.visibility = View.VISIBLE
                contentContainer.visibility = View.GONE
                rowsContainerLl.addView(v)
            }
            else {
                for (currentItem in items) {
                    val v = inflater.inflate(R.layout.viewholder_home_notification_row, rowsContainerLl, false)
                    val title = v.findViewById<TextView>(R.id.titleTv)
                    val subtitle = v.findViewById<TextView>(R.id.subTitleTv)
                    val date = v.findViewById<TextView>(R.id.dateTv)

                    title.text = currentItem.title
                    subtitle.text = currentItem.description
                    date.text = formatter.formatDateRelative(currentItem)

                    rowsContainerLl.addView(v)
                }
            }
        }
    }
}