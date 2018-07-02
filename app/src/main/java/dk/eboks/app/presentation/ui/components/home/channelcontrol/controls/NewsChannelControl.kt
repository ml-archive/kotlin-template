package dk.eboks.app.presentation.ui.components.home.channelcontrol.controls

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.home.Control

class NewsChannelControl(channel: Channel, control : Control, view: View, inflater : LayoutInflater, handler: Handler, val formatter: EboksFormatter) : ChannelControl(channel, control, view, inflater, handler) {

    lateinit var title : TextView
    lateinit var image : ImageView
    lateinit var date : TextView

    override fun buildView() {
        control.items?.let { items ->
            for (row in items) {
                val v = inflater.inflate(R.layout.viewholder_home_news_row, rowsContainerLl, false)
                title = v.findViewById(R.id.titleTv)
                image = v.findViewById(R.id.imageIv)
                date = v.findViewById(R.id.dateTv)
                title.text = row.title
                date.text = formatter.formatDateRelative(row)
                row.image?.url?.let {
                    var reqoptions = RequestOptions()
                    reqoptions = reqoptions.transform(RoundedCorners(8))

                    Glide.with(view.context)
                            .load(row.image?.url)
                            .apply(reqoptions)
                            .into(image)
                }
                rowsContainerLl.addView(v)
            }
        }
    }
}