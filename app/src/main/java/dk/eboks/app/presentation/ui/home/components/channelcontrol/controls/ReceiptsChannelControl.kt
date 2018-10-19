package dk.eboks.app.presentation.ui.home.components.channelcontrol.controls

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.home.Control
import dk.eboks.app.util.guard

class ReceiptsChannelControl(channel: Channel, control : Control, view: View, inflater : LayoutInflater, handler: Handler, val formatter: EboksFormatter) : ChannelControl(channel, control, view, inflater, handler) {
    lateinit var nameContainer : LinearLayout
    lateinit var amountDateContainer : LinearLayout
    lateinit var soloName : TextView
    lateinit var soloAmount : TextView
    lateinit var name : TextView
    lateinit var address : TextView
    lateinit var amount : TextView
    lateinit var date  : TextView

    override fun buildView() {
        control.items?.let { items ->
            for (row in items) {
                val v = inflater.inflate(R.layout.viewholder_home_reciept_row, rowsContainerLl, false)

                nameContainer = v.findViewById<LinearLayout>(R.id.nameSubTitleContainerLl)
                amountDateContainer = v.findViewById<LinearLayout>(R.id.amountDateContainerLl)
                soloName = v.findViewById<TextView>(R.id.soloTitleTv)
                soloAmount = v.findViewById<TextView>(R.id.soloAmountTv)
                name = v.findViewById<TextView>(R.id.titleTv)
                address = v.findViewById<TextView>(R.id.subTitleTv)
                amount = v.findViewById<TextView>(R.id.amountTv)
                date = v.findViewById<TextView>(R.id.dateTv)


                if (row.date == null) {
                    soloAmount.text = formatter.formatPrice(row)
                    soloAmount.visibility = View.VISIBLE
                    amountDateContainer.visibility = View.GONE
                } else {
                    amount.text = formatter.formatPrice(row)
                    date.text = formatter.formatDateRelative(row)
                }

                row.amount?.let {
                    if(it == 0.0) {
                        amount.text = ""
                        date.text = ""
                    }
                }.guard {
                    amount.text = ""
                    date.text = ""
                }

                if (row.description == null) {
                    soloName.text = row.title
                    soloName.visibility = View.VISIBLE
                    nameContainer.visibility = View.GONE
                } else {
                    name.text = row.title
                    address.text = row.description
                }
                rowsContainerLl.addView(v)
            }
        }
    }
}