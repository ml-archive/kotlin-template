package dk.eboks.app.presentation.ui.senders.components

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.sender.SenderGroup
import dk.eboks.app.util.inflate
import kotlinx.android.synthetic.main.viewholder_title_subtitle.view.*
import java.lang.ref.WeakReference

class SenderGroupAdapter(callback: Callback? = null) :
    RecyclerView.Adapter<SenderGroupAdapter.ViewHolder>() {

    private val callbackWeakReference = WeakReference(callback)

    interface Callback {
        fun onGroupClick(group: SenderGroup)
    }

    private val list = mutableListOf<SenderGroup>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.viewholder_title_subtitle))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setData(groupList: List<SenderGroup>) {
        list.clear()
        list += groupList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(group: SenderGroup) {
            itemView.run {
                titleTv.text = group.name
                subTv.text = when (group.registered) {
                    false -> Translation.senderdetails.registeredTypeNo
                    true -> Translation.senderdetails.registeredTypeYes
                    else -> ""
                }
                setOnClickListener {
                    callbackWeakReference.get()?.onGroupClick(group)
                }
            }
        }
    }
}