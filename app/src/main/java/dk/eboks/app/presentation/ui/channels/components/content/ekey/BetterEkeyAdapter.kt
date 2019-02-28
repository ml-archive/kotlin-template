package dk.eboks.app.presentation.ui.channels.components.content.ekey

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.ekey.BaseEkey
import dk.eboks.app.domain.models.channel.ekey.Ekey
import dk.eboks.app.domain.models.channel.ekey.Login
import dk.eboks.app.domain.models.channel.ekey.Note
import dk.eboks.app.domain.models.channel.ekey.Pin
import timber.log.Timber

class BetterEkeyAdapter(
    private val keyList: List<ListItem>,
    val ekeyclicklistener: BetterEkeyAdapter.Ekeyclicklistener? = null
) : RecyclerView.Adapter<BetterEkeyAdapter.EKeyHolder>() {

    var onActionEvent: ((BaseEkey) -> Unit)? = null

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
                HeaderViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_header,
                        parent,
                        false
                    )
                )
            }
            else -> {
                EKeyViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_ekey,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: EKeyHolder, position: Int) {
        holder.bind(keyList[position])
    }

    abstract class EKeyHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
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
        fun onEkeyClicked(ekey: BaseEkey)
    }

    inner class EKeyViewHolder(val root: View) : EKeyHolder(root) {
        private val swipeLayout = root as SwipeLayout
        private val markAsReadContainer = root.findViewById<ViewGroup>(R.id.containerMarkAsRead)

        private var content = root.findViewById<FrameLayout>(R.id.contentContainer)
        private var logoIv = root.findViewById<ImageView>(R.id.logoIv)
        private var headingTv = root.findViewById<TextView>(R.id.headingTv)
        private var subHeadingTv = root.findViewById<TextView>(R.id.subHeadingTv)

        init {
            swipeLayout.showMode = SwipeLayout.ShowMode.PullOut
            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, markAsReadContainer)
        }

        override fun bind(item: ListItem) {
            val eKey = item as EkeyItem

            swipeLayout.isLeftSwipeEnabled = false
            swipeLayout.isRightSwipeEnabled = eKey.data.eKeyType != "Ekey"
            Timber.d("${eKey.data.name} - ${swipeLayout.isLeftSwipeEnabled}")
            markAsReadContainer.setOnClickListener {
                onActionEvent?.invoke(item.data)
            }

            content.setOnClickListener {
                // todo clicked
                Timber.i(item.toString())
                ekeyclicklistener?.onEkeyClicked(eKey.data)
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