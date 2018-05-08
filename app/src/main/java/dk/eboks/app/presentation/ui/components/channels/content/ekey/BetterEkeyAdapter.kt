package dk.eboks.app.presentation.ui.components.channels.content.ekey

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.models.channel.ekey.Login
import dk.eboks.app.domain.models.channel.ekey.Note
import dk.eboks.app.domain.models.channel.ekey.Pin
import timber.log.Timber

class BetterEkeyAdapter(val keyList: List<ListItem>) : RecyclerView.Adapter<BetterEkeyAdapter.EKeyHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (keyList[position]) {
            is Header -> {
                R.layout.item_header // layout ref is used as an ID
            }
            else -> {
                R.layout.item_ekey // layout ref is used as an ID
            }
        }
    }

    override fun getItemCount(): Int {
        return keyList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EKeyHolder {
        return when (viewType) {
            R.layout.item_header -> {
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

    abstract inner class EKeyHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: ListItem)
    }

    inner class HeaderViewHolder(val root: View) : EKeyHolder(root) {
        private var title = root.findViewById<TextView>(R.id.titleHeaderTv)

        override fun bind(item: ListItem) {
            title.text = (item as Header).text
        }
    }

    inner class EKeyViewHolder(val root: View) : EKeyHolder(root) {

        private var logo = root.findViewById<ImageView>(R.id.logoIv)
        private var header = root.findViewById<TextView>(R.id.headingTv)
        private var subHeader = root.findViewById<TextView>(R.id.subHeadingTv)


        override fun bind(item: ListItem) {

            val eKey = item as EkeyItem

            root.setOnClickListener {
                //todo clicked
                Timber.i(item.toString())
            }
            when (eKey.data) {
                is Note -> {
                    header.text = eKey.data.name
                    subHeader.text = "_note"
                    logo.setImageResource(R.drawable.icon_48_edit_white)
                }
                is Pin -> {
                    header.text = eKey.data.name
                    subHeader.text = "_Pin"
                    logo.setImageResource(R.drawable.icon_48_payment_white)

                }
                is Login -> {
                    header.text = eKey.data.name
                    subHeader.text = eKey.data.username
                    logo.setImageResource(R.drawable.icon_48_lock_white)
                }
            }
        }

    }
}