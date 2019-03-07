package dk.eboks.app.presentation.ui.channels.components.content.ekey

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.ekey.BaseEkey
import dk.eboks.app.domain.models.channel.ekey.Ekey
import dk.eboks.app.domain.models.channel.ekey.Login
import dk.eboks.app.domain.models.channel.ekey.Note
import dk.eboks.app.domain.models.channel.ekey.Pin
import dk.eboks.app.util.inflate
import kotlinx.android.synthetic.main.item_ekey.view.*
import kotlinx.android.synthetic.main.item_header.view.*
import timber.log.Timber

class BetterEkeyAdapter(
    private val keyList: List<ListItem>,
    private val ekeyClickListener: BetterEkeyAdapter.EkeyClickListener? = null
) : RecyclerView.Adapter<BetterEkeyAdapter.EKeyHolder>() {

    var onActionEvent: ((BaseEkey) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return when (keyList[position]) {
            is ListItem.Header -> {
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
            HeaderViewHolder.identifier -> HeaderViewHolder(parent.inflate(R.layout.item_header))
            else -> EKeyViewHolder(parent.inflate(R.layout.item_ekey))
        }
    }

    override fun onBindViewHolder(holder: EKeyHolder, position: Int) {
        holder.bind(keyList[position])
    }

    abstract class EKeyHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: ListItem)
    }

    class HeaderViewHolder(itemView: View) : EKeyHolder(itemView) {
        companion object {
            const val identifier = 42762461 // random identifier
        }

        override fun bind(item: ListItem) {
            itemView.titleHeaderTv.text = (item as ListItem.Header).text
        }
    }

    interface EkeyClickListener {
        fun onEkeyClicked(ekey: BaseEkey)
    }

    inner class EKeyViewHolder(itemView: View) : EKeyHolder(itemView) {

        init {
            (itemView as? SwipeLayout)?.run {
                showMode = SwipeLayout.ShowMode.PullOut
                addDrag(SwipeLayout.DragEdge.Left, containerMarkAsRead)
            }
        }

        override fun bind(item: ListItem) {
            (itemView as? SwipeLayout)?.run {
                val eKey = item as ListItem.EkeyItem

                isLeftSwipeEnabled = false
                isRightSwipeEnabled = eKey.data.eKeyType != "Ekey"
                Timber.d("${eKey.data.name} - $isLeftSwipeEnabled")
                containerMarkAsRead.setOnClickListener {
                    onActionEvent?.invoke(item.data)
                }

                contentContainer.setOnClickListener {
                    // todo clicked
                    Timber.i(item.toString())
                    ekeyClickListener?.onEkeyClicked(eKey.data)
                }
                when (eKey.data) {
                    is Note -> {
                        headingTv.text = eKey.data.name
                        subHeadingTv.text = Translation.ekey.overviewNote
                        logoIv.setImageResource(R.drawable.icon_48_edit_white)
                    }
                    is Pin -> {
                        headingTv.text = eKey.data.name
                        subHeadingTv.text = Translation.ekey.pinCode
                        logoIv.setImageResource(R.drawable.icon_48_pincode_white)
                    }
                    is Login -> {
                        headingTv.text = eKey.data.name
                        subHeadingTv.text = (eKey.data as Login).username
                        logoIv.setImageResource(R.drawable.icon_48_lock_white)
                    }
                    is Ekey -> {
                        headingTv.text = eKey.data.name
                        subHeadingTv.text = Translation.ekey.overviewEkey
                        logoIv.setImageResource(R.drawable.icon_48_lock_white)
                    }
                }
            }
        }
    }
}