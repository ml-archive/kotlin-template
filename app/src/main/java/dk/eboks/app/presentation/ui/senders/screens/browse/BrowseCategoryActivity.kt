package dk.eboks.app.presentation.ui.senders.screens.browse

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.l4digital.fastscroll.FastScroller
import dk.eboks.app.R
import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.senders.screens.detail.SenderDetailActivity
import dk.eboks.app.presentation.widgets.DividerItemDecoration
import dk.eboks.app.senders.presentation.ui.screens.browse.BrowseCategoryContract
import dk.eboks.app.util.guard
import androidx.core.view.isInvisible
import dk.eboks.app.util.setBubbleDrawable
import kotlinx.android.synthetic.main.activity_senders_browse_category.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Christian on 3/13/2018.
 * @author Christian
 * @since 3/13/2018.
 */
class BrowseCategoryActivity : BaseActivity(), BrowseCategoryContract.View {

    @Inject
    lateinit var presenter: BrowseCategoryContract.Presenter

    val senders = ArrayList<Sender>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_senders_browse_category)
        component.inject(this)
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            finish()
        }

        browseCatRv.adapter = SenderAdapter(senders)
        browseCatRv.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        browseCatRv.addItemDecoration(
            DividerItemDecoration(
                drawable = ContextCompat.getDrawable(this, R.drawable.shape_divider)!!,
                indentationDp = 72,
                backgroundColor = ContextCompat.getColor(this, R.color.white)
            )
        )

        presenter.onViewCreated(this, lifecycle)
        val cat = intent.getParcelableExtra<SenderCategory>(SenderCategory::class.simpleName)
        if (cat == null) {
            // lets close the view after we informed the user of the error, since we can't initialize it proper without the arguments anyway I take it? :)
            showErrorDialog(
                ViewError(
                    title = Translation.error.genericTitle,
                    message = Translation.error.genericMessage,
                    shouldCloseView = true
                )
            )
        } else {
           // cat.senders?.let(::showSenders).guard {
                presenter.loadSenders(cat.id)
           // }
            mainTb.title = cat.name
        }
        ContextCompat.getDrawable(this, R.drawable.fastscroll_bubble)
            ?.let(browseCatRv::setBubbleDrawable)
    }

    override fun showSenders(senders: List<Sender>) {
        this.senders.clear()
        this.senders.addAll(
            senders.sortedWith(
                Comparator { sender1, sender2 ->
                    sender1.name.toLowerCase().compareTo(sender2.name.toLowerCase())
                }
            ))
        browseCatRv.adapter?.notifyDataSetChanged()
    }

    override fun showProgress(show: Boolean) {
        browseCatPb.visibility = when (show) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }

    override fun showEmpty(show: Boolean) {
    }

    override fun showError(msg: String) {
        Timber.e(msg) // errorhandling lol
    }

    inner class SenderAdapter(val senders: List<Sender>) :
        RecyclerView.Adapter<SenderAdapter.SenderViewHolder>(),
        FastScroller.SectionIndexer {
        override fun getSectionText(position: Int): String {
            return "${senders[position].name.first().toUpperCase()}"
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SenderViewHolder {
            return SenderViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.viewholder_sender,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: SenderViewHolder, position: Int) {
            val s = senders[position]

            // REMOVED FEATURE
            //  determine if we need to show the first-letter
            var firstInGroup = true
            try {
                val prev = senders[position - 1]
                if (s.name.toLowerCase().startsWith(prev.name.toLowerCase().first())) {
                    firstInGroup = false
               }
            }  catch (e : Exception) {}

            holder.bind(s)
           holder.showIndex(firstInGroup)
        }

        override fun getItemCount(): Int {
            return senders.size
        }

        inner class SenderViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            private val mainLl = v.findViewById<View>(R.id.senderMainLl)
            private val indexTv = v.findViewById<TextView>(R.id.senderIndexTv)
            val nameTv = v.findViewById<TextView>(R.id.senderNameTv)
            val iconIv = v.findViewById<ImageView>(R.id.senderLogoIv)

            init {
                // no index here - dammit!
                indexTv.visibility = View.GONE
            }

            fun bind(sender: Sender) {
                indexTv.text = "${sender.name.first().toUpperCase()}"
                nameTv.text = sender.name
                Glide.with(itemView.context)
                    .load(sender.logo?.url)
                    .apply(
                        RequestOptions()
                            .fallback(R.drawable.icon_64_senders_private)
                            .placeholder(R.drawable.icon_64_senders_private)
                    )
                    .into(iconIv)

                mainLl.setOnClickListener {
                    val i = Intent(this@BrowseCategoryActivity, SenderDetailActivity::class.java)
                    i.putExtra(Sender::class.simpleName, senders[position])
                    startActivity(i)
                }
            }

            // this will hide-show the first-letter textview
            fun showIndex(show: Boolean) {
                indexTv.isInvisible = !show
            }
        }
    }
}
