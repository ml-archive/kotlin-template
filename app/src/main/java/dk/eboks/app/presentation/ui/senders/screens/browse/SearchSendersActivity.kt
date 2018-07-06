package dk.eboks.app.presentation.ui.senders.screens.browse

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.l4digital.fastscroll.FastScroller
import dk.eboks.app.R
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.senders.screens.detail.SenderDetailActivity
import kotlinx.android.synthetic.main.activity_senders_search_senders.*
import javax.inject.Inject

/**
 * Created by Christian on 3/14/2018.
 * @author   Christian
 * @since    3/14/2018.
 */
class SearchSendersActivity : BaseActivity(), BrowseCategoryContract.View {

    @Inject
    lateinit var presenter: BrowseCategoryContract.Presenter

    val senders = ArrayList<Sender>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_senders_search_senders)
        component.inject(this)

        searchSenderTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        searchSenderTb.setNavigationOnClickListener {
            finish()
        }

        searchSenderRv.adapter = SenderAdapter(senders)
        searchSenderRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        searchSenderSv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            private val deBounce = Runnable {
                presenter.searchSenders(searchSenderSv.query.toString().trim())
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                searchSenderSv.removeCallbacks(deBounce)
                searchSenderSv.post(deBounce)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText.isBlank()) {
                    senders.clear()
                    searchSenderRv.adapter?.notifyDataSetChanged()
                } else {
                    searchSenderSv.removeCallbacks(deBounce)
                    searchSenderSv.postDelayed(deBounce, 500)
                }
                return true
            }
        })

        showProgress(false)

        presenter.onViewCreated(this, lifecycle)
    }

    override fun showProgress(show: Boolean) {
        searchSenderPb.visibility = when (show) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }

    override fun showEmpty(show: Boolean) {
        showProgress(false)
    }

    override fun showError(msg: String) {
        showProgress(false)
    }

    override fun showSenders(senders: List<Sender>) {
        this.senders.clear()
        this.senders.addAll(senders)
        searchSenderRv.adapter?.notifyDataSetChanged()
    }

    inner class SenderAdapter(val senders: List<Sender>) : RecyclerView.Adapter<SenderAdapter.SenderViewHolder>(), FastScroller.SectionIndexer {
        override fun getSectionText(position: Int): String {
            return "${senders[position].name.first()}"
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SenderViewHolder {
            return SenderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.viewholder_sender, parent, false))
        }

        override fun onBindViewHolder(holder: SenderViewHolder?, position: Int) {
            holder?.bind(senders[position])
        }

        override fun getItemCount(): Int {
            return senders.size
        }

        inner class SenderViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
            val mainLl = v.findViewById<View>(R.id.senderMainLl)
            val indexTv = v.findViewById<TextView>(R.id.senderIndexTv)
            val nameTv = v.findViewById<TextView>(R.id.senderNameTv)
            val iconIv = v.findViewById<ImageView>(R.id.senderLogoIv)

            init {
                indexTv.visibility = View.GONE
            }

            fun bind(sender: Sender) {
                indexTv.text = "${sender.name.first().toUpperCase()}"
                nameTv.text = sender.name
                Glide.with(v.context)
                        .load(sender.logo?.url)
                        .apply(RequestOptions()
                                .fallback(R.drawable.icon_64_senders_private)
                                .placeholder(R.drawable.icon_64_senders_private)
                        )
                        .into(iconIv)

                mainLl.setOnClickListener {
                    val i = Intent(this@SearchSendersActivity, SenderDetailActivity::class.java )
                    i.putExtra(Sender::class.simpleName, senders[position])
                    startActivity(i)
                }
            }
        }
    }
}