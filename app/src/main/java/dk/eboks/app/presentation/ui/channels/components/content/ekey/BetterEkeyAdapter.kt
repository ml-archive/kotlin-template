package dk.eboks.app.presentation.ui.channels.components.content.ekey

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.ekey.Ekey
import dk.eboks.app.domain.models.channel.ekey.Login
import dk.eboks.app.domain.models.channel.ekey.Note
import dk.eboks.app.domain.models.channel.ekey.Pin
import timber.log.Timber

class BetterEkeyAdapter(private val keyList: List<ListItem>, val ekeyclicklistener: BetterEkeyAdapter.Ekeyclicklistener? = null) : RecyclerView.Adapter<BetterEkeyAdapter.EKeyHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (keyList[position]) {
            is Header -> {
                HeaderViewHolder.identifier
            }
            else -> {
                super.getItemViewType(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return keyList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EKeyHolder {
        return when (viewType) {
            HeaderViewHolder.identifier -> {
                HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false))
            }
            else -> {
                EKeyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_ekey, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: EKeyHolder?, position: Int) {
        holder?.bind(keyList[position])
    }

    abstract class EKeyHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: ListItem)
    }

    class HeaderViewHolder(val root: View) : EKeyHolder(root) {
        companion object {
            const val identifier = 42762461 // random identifier
        }

        private var titleTv = root.findViewById<TextView>(R.id.titleHeaderTv)

        override fun bind(item: ListItem) {
            titleTv.text = (item as Header).text
        }
    }

    interface Ekeyclicklistener {
        fun onEkeyClicked(ekey: Ekey)
    }

    inner class EKeyViewHolder(val root: View) : EKeyHolder(root) {

        private var logoIv = root.findViewById<ImageView>(R.id.logoIv)
        private var headingTv = root.findViewById<TextView>(R.id.headingTv)
        private var subHeadingTv = root.findViewById<TextView>(R.id.subHeadingTv)

        override fun bind(item: ListItem) {
            val eKey = item as EkeyItem

            root.setOnClickListener {
                //todo clicked
                Timber.i(item.toString())
                ekeyclicklistener?.let{
                    it.onEkeyClicked(eKey.data)
                }
            }
            when (eKey.data) {
                is Note -> {
                    headingTv.text = eKey.data.name
                    subHeadingTv.text = Translation.ekey.overviewNote
                    logoIv.setImageResource(R.drawable.icon_48_edit_white)
                }
                is Pin -> {
                    headingTv.text = eKey.data.name
                    subHeadingTv.text = eKey.data.cardholderName
                    logoIv.setImageResource(R.drawable.icon_48_payment_white)

                }
                is Login -> {
                    headingTv.text = eKey.data.name
                    subHeadingTv.text = eKey.data.username
                    logoIv.setImageResource(R.drawable.icon_48_lock_white)
                }
            }
        }

    }
}