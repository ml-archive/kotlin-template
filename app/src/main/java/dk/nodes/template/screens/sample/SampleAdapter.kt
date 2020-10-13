package dk.nodes.template.screens.sample

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dk.nodes.template.core.entities.Post
import dk.nodes.template.presentation.R
import dk.nodes.utils.android.view.inflate
import kotlinx.android.synthetic.main.row_sample.view.*

class SampleAdapter : RecyclerView.Adapter<SampleAdapter.ViewHolder>() {

    private val list = mutableListOf<dk.nodes.template.core.entities.Post>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.row_sample))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setData(list: List<dk.nodes.template.core.entities.Post>) {
        val diff = DiffUtil.calculateDiff(PostDiff(this.list, list))
        this.list.clear()
        this.list += list
        diff.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(post: dk.nodes.template.core.entities.Post) {
            itemView.run {
                titleTv.text = post.title
                bodyTv.text = post.body
            }
        }
    }

    private inner class PostDiff(
            private val oldList: List<dk.nodes.template.core.entities.Post>,
            private val newList: List<dk.nodes.template.core.entities.Post>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return newList[newItemPosition].id == oldList[oldItemPosition].id
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return newList[newItemPosition] == oldList[oldItemPosition]
        }
    }
}
