package dk.eboks.app.presentation.ui.senders.screens.registrations

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Registrations
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.senders.screens.detail.SenderDetailActivity
import dk.eboks.app.senders.presentation.ui.screens.registrations.RegistrationsContract
import dk.eboks.app.util.inflate
import kotlinx.android.synthetic.main.activity_senders_registrations.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.android.synthetic.main.viewholder_sender.view.*
import javax.inject.Inject

/**
 * Created by Christian on 3/28/2018.
 * @author Christian
 * @since 3/28/2018.
 */
class RegistrationsActivity : BaseActivity(), RegistrationsContract.View {

    private val registeredSenders = ArrayList<Sender>()
    private lateinit var adapter: SenderAdapter

    @Inject
    lateinit var presenter: RegistrationsContract.Presenter

    override fun showErrorDialog(error: ViewError) {
        registrationsPb.visibility = View.GONE
        super.showErrorDialog(error)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_senders_registrations)
        component.inject(this)
        setupToolbar()
        setupRecyclerView()
        presenter.onViewCreated(this, lifecycle)
    }

    private fun setupToolbar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            finish()
        }
        mainTb.title = Translation.senders.registrations
    }

    private fun setupRecyclerView() {
        adapter = SenderAdapter(registeredSenders)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun showRegistrations(registrations: Registrations) {
        registrationsPb.visibility = View.GONE
        // TODO missing api to group senders
//        if (0 != registrations.public?.type) {
//            val vh = adapter.onCreateViewHolder(registrationsLl, 0)
//
//            vh.nameTv.text = Translation.senderdetails.publicAuthoritiesHeader
//            vh.iconIv.setImageResource(R.drawable.icon_72_senders_public)
//
//            vh.mainLl.setOnClickListener {
//                val i = Intent(this@RegistrationsActivity, SegmentDetailActivity::class.java)
//                i.putExtra(Segment::class.simpleName, Segment(0, name = Translation.senderdetails.publicAuthoritiesHeader, type = "public", registered = 1)) // TODO!!! Check if segment-1 is indeed the public authorities, else, we need the API response to include that!
//                startActivity(i)
//            }
//
//            val margin = resources.getDimensionPixelSize(R.dimen.margin_normal)
//            val lp = vh.v.layoutParams as LinearLayout.LayoutParams
//            lp.bottomMargin = margin
//            vh.v.layoutParams = lp
//            registrationsLl.addView(vh.v)

        registeredSenders.clear()
        registeredSenders += registrations.senders
        adapter.notifyDataSetChanged()
//        }
    }

    inner class SenderAdapter(val senders: List<Sender>) :
        RecyclerView.Adapter<SenderAdapter.SenderViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SenderViewHolder {
            return SenderViewHolder(parent.inflate(R.layout.viewholder_sender))
        }

        override fun onBindViewHolder(holder: SenderViewHolder, position: Int) {
            holder.bind(senders[position])
        }

        override fun getItemCount(): Int {
            return senders.size
        }

        inner class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            init {
                itemView.senderIndexTv.visibility = View.GONE // feature removed
            }

            fun bind(sender: Sender) {
                itemView.run {
                    senderIndexTv.text = "${sender.name.first().toUpperCase()}"
                    senderNameTv.text = sender.name
                    Glide.with(context)
                        .load(sender.logo?.url)
                        .apply(
                            RequestOptions()
                                .fallback(R.drawable.icon_64_senders_private)
                                .placeholder(R.drawable.icon_64_senders_private)
                        )
                        .into(senderLogoIv)

                    senderMainLl.setOnClickListener {
                        val i = Intent(this@RegistrationsActivity, SenderDetailActivity::class.java)
                        i.putExtra(Sender::class.simpleName, sender)
                        startActivity(i)
                    }
                }
            }
        }
    }
}