package dk.eboks.app.presentation.ui.components.channels.content.ekey

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.models.channel.ekey.*

class EkeyAdapter : RecyclerView.Adapter<EkeyAdapter.EkeyViewHolder>() {

    var keyList: MutableList<EkeyListItem> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EkeyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_ekey_row, parent, false
        )
        return EkeyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return keyList.size
    }


    override fun onBindViewHolder(holder: EkeyViewHolder?, position: Int) {

        val last = (position == keyList.size)
        holder?.bind(keyList[position], last)

    }

    inner class EkeyViewHolder(val root: View) : RecyclerView.ViewHolder(root) {

        var logo = root.findViewById<ImageView>(R.id.logoIv)
        var header = root.findViewById<TextView>(R.id.headerTv)
        var subHeader = root.findViewById<TextView>(R.id.subHeaderTv)
        var headerContainer = root.findViewById<FrameLayout>(R.id.headerContainerLl)
        var rowContainer = root.findViewById<LinearLayout>(R.id.rowContainerLl)
        var titleHeader = root.findViewById<TextView>(R.id.titleHeaderTv)
        var divider = root.findViewById<View>(R.id.rowDivider)


        fun bind(currentKey: EkeyListItem, last: Boolean) {

            when (currentKey) {
                is EkeyListItem.Data -> {
                    headerContainer.visibility = View.GONE
                    rowContainer.visibility = View.VISIBLE
                    if(last){divider.visibility = View.GONE}

                    root.setOnClickListener {
                        //todo clicked
                        println(currentKey.toString())
                    }
                    when (currentKey.data) {
                        is Note -> {
                            header.text = currentKey.data.name
                            subHeader.text = "_note"
                            logo.setImageResource(R.drawable.icon_48_edit_white)
                        }
                        is Pin -> {
                            header.text = currentKey.data.name
                            subHeader.text = "_Pin"
                            logo.setImageResource(R.drawable.icon_48_payment_white)

                        }
                        is Login -> {
                            header.text = currentKey.data.name
                            subHeader.text = currentKey.data.username
                            logo.setImageResource(R.drawable.icon_48_lock_white)
                        }
                    }
                }
                is EkeyListItem.Header -> {

                    headerContainer.visibility = View.VISIBLE
                    rowContainer.visibility = View.GONE
                    titleHeader.text = currentKey.type

                }

            }
        }
    }
}