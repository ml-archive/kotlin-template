package dk.eboks.app.presentation.ui.home.components.channelcontrol.controls

import android.graphics.Color
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
import dk.eboks.app.util.guard

class ImagesChannelControl(channel: Channel, control : Control, view: View, inflater : LayoutInflater, handler: Handler, val formatter: EboksFormatter) : ChannelControl(channel, control, view, inflater, handler) {
    override fun buildView() {
        control.items?.let { items ->
                val v = inflater.inflate(R.layout.viewholder_home_image_row, rowsContainerLl, false)
                val title = v.findViewById<TextView>(R.id.titleTv)
                val image = v.findViewById<ImageView>(R.id.backgroundIv)
                val background = v.findViewById<View>(R.id.backgroundColorV)

                val currentItem = items.first()

                channel.background.rgba?.let { background?.background?.setTint(Color.parseColor("#"+it)) }.guard {
                    channel.background.rgb?.let { background?.background?.setTint(Color.parseColor("#"+it)) }
                }

                title.text = currentItem.title
                image?.let {
                    currentItem.image?.url?.let {
                        Glide.with(v.context).load(it).into(image)
                    }
                }
                rowsContainerLl.addView(v)
            }
    }
}