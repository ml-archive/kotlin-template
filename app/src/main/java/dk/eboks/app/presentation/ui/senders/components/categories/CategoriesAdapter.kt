package dk.eboks.app.presentation.ui.senders.components.categories

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dk.eboks.app.R
import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.util.inflate
import kotlinx.android.synthetic.main.viewholder_title_subtitle.view.*
import java.lang.ref.WeakReference

class CategoriesAdapter(callback: Callback? = null) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    private val callbackWeakReference = WeakReference(callback)

    interface Callback {
        fun onCategoryClick(category: SenderCategory)
    }

    private val list = mutableListOf<SenderCategory>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.viewholder_title_subtitle))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])

    fun setData(categories: List<SenderCategory>) {
        list.clear()
        list += categories
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(category: SenderCategory) {
            itemView.run {
                titleTv.text = category.name
                subTv.text = "${category.numberOfSenders}"
                setOnClickListener {
                    callbackWeakReference.get()?.onCategoryClick(category)
                }
            }
        }
    }
}