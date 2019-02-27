package dk.eboks.app.presentation.ui.home.components.channelcontrol.controls

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.home.Control
import java.util.Observable

abstract class ChannelControl(
    val channel: Channel,
    val control: Control,
    val view: View,
    val inflater: LayoutInflater,
    val handler: Handler
) : Observable() {
    val logoIv: ImageView = view.findViewById(R.id.logoIv)
    val progressPb: ProgressBar = view.findViewById(R.id.progressPb)
    val headerTv: TextView = view.findViewById(R.id.headerTv)
    val rowsContainerLl: LinearLayout = view.findViewById(R.id.rowsContainerLl)

    fun showProgress(show: Boolean) {
        logoIv.visibility = if (show) View.GONE else View.VISIBLE
        progressPb.visibility = if (!show) View.GONE else View.VISIBLE
    }

    abstract fun buildView()
}