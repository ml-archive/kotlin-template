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

class ReceiptsChannelControl(
    channel: Channel,
    control: Control,
    view: View,
    inflater: LayoutInflater,
    handler: Handler,
    val formatter: EboksFormatter
) : ChannelControl(channel, control, view, inflater, handler) {
    private lateinit var nameContainer: LinearLayout
    private lateinit var amountDateContainer: LinearLayout
    private lateinit var soloName: TextView
    private lateinit var soloAmount: TextView
    lateinit var name: TextView
    private lateinit var address: TextView
    private lateinit var amount: TextView
    lateinit var date: TextView

    override fun buildView() {
        control.items?.let { items ->
            for (row in items) {
                val v =
                    inflater.inflate(R.layout.viewholder_home_reciept_row, rowsContainerLl, false)

                nameContainer = v.findViewById(R.id.nameSubTitleContainerLl)
                amountDateContainer = v.findViewById(R.id.amountDateContainerLl)
                soloName = v.findViewById(R.id.soloTitleTv)
                soloAmount = v.findViewById(R.id.soloAmountTv)
                name = v.findViewById(R.id.titleTv)
                address = v.findViewById(R.id.subTitleTv)
                amount = v.findViewById(R.id.amountTv)
                date = v.findViewById(R.id.dateTv)

                if (row.date == null) {
                    soloAmount.text = formatter.formatPrice(row)
                    soloAmount.visibility = View.VISIBLE
                    amountDateContainer.visibility = View.GONE
                } else {
                    amount.text = formatter.formatPrice(row)
                    date.text = formatter.formatDateRelative(row)
                }

                row.amount?.let {
                    if (it == 0.0) {
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