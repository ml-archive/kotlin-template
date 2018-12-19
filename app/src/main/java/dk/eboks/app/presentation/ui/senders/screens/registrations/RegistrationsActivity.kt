package dk.eboks.app.presentation.ui.senders.screens.registrations

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Registrations
import dk.eboks.app.domain.models.sender.Segment
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.senders.screens.detail.SenderDetailActivity
import dk.eboks.app.presentation.ui.senders.screens.segment.SegmentDetailActivity
import kotlinx.android.synthetic.main.activity_senders_registrations.*
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

/**
 * Created by Christian on 3/28/2018.
 * @author   Christian
 * @since    3/28/2018.
 */
class RegistrationsActivity : BaseActivity(), RegistrationsContract.View {

    val registeredSenders = ArrayList<Sender>()
    var adapter: SenderAdapter

    @Inject
    lateinit var presenter: RegistrationsContract.Presenter

    init {
        adapter = SenderAdapter(registeredSenders)
    }

    override fun showErrorDialog(error: ViewError) {
        registrationsPb.visibility = View.GONE
        super.showErrorDialog(error)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_senders_registrations)

        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            finish()
        }
        mainTb.title = Translation.senders.registrations
    }

    fun populate() {
        adapter.senders.forEachIndexed { index, _ ->
            val vh = adapter.onCreateViewHolder(registrationsLl, 0)
            adapter.onBindViewHolder(vh, index)
            registrationsLl.addView(vh.v)
        }
    }

    override fun showRegistrations(registrations: Registrations) {
        registrationsPb.visibility = View.GONE
        registrationsLl.removeAllViews()

        if (0 != registrations.public.type) {
            val vh = adapter.onCreateViewHolder(registrationsLl, 0)

            vh.nameTv.text = Translation.senderdetails.publicAuthoritiesHeader
            vh.iconIv.setImageResource(R.drawable.icon_72_senders_public)

            vh.mainLl.setOnClickListener {
                val i = Intent(this@RegistrationsActivity, SegmentDetailActivity::class.java)
                i.putExtra(Segment::class.simpleName, Segment(0, name = Translation.senderdetails.publicAuthoritiesHeader, type = "public", registered = 1)) // TODO!!! Check if segment-1 is indeed the public authorities, else, we need the API response to include that!
                startActivity(i)
            }

            val margin = resources.getDimensionPixelSize(R.dimen.margin_normal)
            val lp = vh.v.layoutParams as LinearLayout.LayoutParams
            lp.bottomMargin = margin
            vh.v.layoutParams = lp
            registrationsLl.addView(vh.v)

            registeredSenders.clear()
            registeredSenders.addAll(registrations.senders)
            adapter.notifyDataSetChanged()

            populate()
        }
    }

    inner class SenderAdapter(val senders: List<Sender>) : androidx.recyclerview.widget.RecyclerView.Adapter<SenderAdapter.SenderViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SenderViewHolder {
            return SenderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.viewholder_sender, parent, false))
        }

        override fun onBindViewHolder(holder: SenderViewHolder, position: Int) {
            holder.bind(senders[position])
        }

        override fun getItemCount(): Int {
            return senders.size
        }

        inner class SenderViewHolder(val v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {
            val mainLl = v.findViewById<View>(R.id.senderMainLl)
            val indexTv = v.findViewById<TextView>(R.id.senderIndexTv)
            val nameTv = v.findViewById<TextView>(R.id.senderNameTv)
            val iconIv = v.findViewById<ImageView>(R.id.senderLogoIv)

            init {
                indexTv.visibility = View.GONE // feature removed
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
                    val i = Intent(this@RegistrationsActivity, SenderDetailActivity::class.java)
                    i.putExtra(Sender::class.simpleName, sender)
                    startActivity(i)
                }
            }
        }
    }
}