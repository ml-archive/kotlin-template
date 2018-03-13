package dk.eboks.app.presentation.ui.screens.senders.overview

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.l4digital.fastscroll.FastScrollRecyclerView
import com.l4digital.fastscroll.FastScroller
import dk.eboks.app.R
import dk.eboks.app.domain.models.Sender
import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.util.setBubbleDrawable
import kotlinx.android.synthetic.main.activity_senders_browse_category.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Christian on 3/13/2018.
 * @author   Christian
 * @since    3/13/2018.
 */
class BrowseCategoryActivity : BaseActivity(), BrowseCategoryContract.View {

    @Inject
    lateinit var presenter: BrowseCategoryContract.Presenter

    val categories = ArrayList<Sender>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_senders_browse_category)
        component.inject(this)
        val cat = intent.getSerializableExtra(SenderCategory::class.simpleName) as SenderCategory?
        if (cat == null) {
            showError(Translation.error.errorMessage10100) // TODO find the right message
        } else {
            presenter.loadSenders(cat.id)
            mainTb.title = cat.name
        }

        mainTb.setNavigationIcon(R.drawable.red_navigationbar)
        mainTb.setNavigationOnClickListener {
            finish()
        }

        browseCatRv.adapter = SenderAdapter(categories)
        browseCatRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        presenter.onViewCreated(this, lifecycle)

        browseCatRv.setBubbleDrawable(resources.getDrawable(R.drawable.fastscroll_bubble))
    }

    override fun showSenders(categories: List<Sender>) {
        this.categories.clear()
        this.categories.addAll(
                categories.sortedWith(
                        Comparator { sender1, sender2 ->
                            sender1.name.compareTo(sender2.name)
                        }
                ))
        browseCatRv.adapter.notifyDataSetChanged()
    }

    override fun setupTranslations() {

    }

    override fun showError(msg: String) {
        Timber.e(msg) // errorhandling lol
    }

    inner class SenderAdapter(val senders: List<Sender>) : RecyclerView.Adapter<SenderAdapter.SenderViewHolder>(), FastScroller.SectionIndexer {
        override fun getSectionText(position: Int): String {
            return "${senders[position].name.first()}"
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SenderViewHolder {
            return SenderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.viewholder_sender, parent, false))
        }

        override fun onBindViewHolder(holder: SenderViewHolder?, position: Int) {
            val s = senders[position]
            holder?.bind(s)

            var firstInGroup = true
            try {
                val prev = senders[position - 1]
                if (s.name.startsWith(prev.name.first())) {
                    firstInGroup = false
                }
            }  catch (e : Exception) {}
            holder?.showIndex(firstInGroup)
        }

        override fun getItemCount(): Int {
            return senders.size
        }

        inner class SenderViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
            val indexTv = v.findViewById<TextView>(R.id.senderIndexTv)
            val nameTv = v.findViewById<TextView>(R.id.senderNameTv)
            val iconIv = v.findViewById<ImageView>(R.id.senderLogoIv)

            fun bind(sender: Sender) {
                indexTv.text = "${sender.name.first()}"
                nameTv.text = sender.name
                Glide.with(v.context).load(sender.logo).into(iconIv)
            }

            fun showIndex(show: Boolean) {
                indexTv.visibility = if (show) {
                    View.VISIBLE
                } else {
                    View.INVISIBLE
                }
            }
        }
    }
}
