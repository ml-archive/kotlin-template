package dk.eboks.app.presentation.ui.senders.screens.registrations

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.senders.screens.detail.SenderDetailActivity
import kotlinx.android.synthetic.main.activity_senders_pending.*
import timber.log.Timber
import javax.inject.Inject
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * Created by Christian on 3/28/2018.
 * @author   Christian
 * @since    3/28/2018.
 */
class PendingActivity : BaseActivity(), PendingContract.View {

    private val registeredSenders = ArrayList<Sender>()
//    var adapter: PendingAdapter

    @Inject
    lateinit var presenter: PendingContract.Presenter

//    init {
//        adapter = PendingAdapter(registeredSenders)
//    }

    override fun showErrorDialog(error: ViewError) {
        pendingPb.visibility = View.GONE
        super.showErrorDialog(error)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_senders_pending)

        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            finish()
        }
        mainTb.title = Translation.senders.registrations
    }

    override fun showPendingRegistrations(registrations: List<CollectionContainer>) {
        Timber.d("showPendingRegistrations ${registrations.size}")
        pendingPb.visibility = View.GONE
        pendingLl.removeAllViews()

        for (c in registrations) {
            c.senders?.let {
                val adapter = PendingAdapter(it)
                adapter.notifyDataSetChanged()

                adapter.senders.forEachIndexed { index, _ ->
                    val vh = adapter.onCreateViewHolder(pendingLl, 0)
                    adapter.onBindViewHolder(vh, index)
                    pendingLl.addView(vh.v)
                }
            }
        }
    }

    override fun showRegistrationSuccess() {
        Timber.d("showRegistrationSuccess - YAY!")
    }

    inner class PendingAdapter(val senders: List<Sender>) : RecyclerView.Adapter<PendingAdapter.SenderViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SenderViewHolder {
            return SenderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.viewholder_pending_sender, parent, false))
        }

        override fun onBindViewHolder(holder: SenderViewHolder?, position: Int) {
            holder?.bind(senders[position])
        }

        override fun getItemCount(): Int {
            return senders.size
        }

        inner class SenderViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
            val mainLl = v.findViewById<View>(R.id.pendingMainLl)
            val iconIv = v.findViewById<ImageView>(R.id.pendingLogoIv)
            val nameTv = v.findViewById<TextView>(R.id.pendingNameTv)
            val approveBtn = v.findViewById<Button>(R.id.pendingRegisterBtn)
            val closeBtn = v.findViewById<ImageButton>(R.id.pendingCloseBtn)
            val descriptionTv = v.findViewById<TextView>(R.id.pendingDescriptionTv)


            fun bind(sender: Sender) {
                Glide.with(v.context).load(sender.logo?.url).into(iconIv)
                nameTv.text = sender.name

                descriptionTv.visibility = View.GONE
                sender.description?.let {
                    descriptionTv.text = it.text
                    descriptionTv.visibility = View.VISIBLE
                }

                mainLl.setOnClickListener {
                    val i = Intent(this@PendingActivity, SenderDetailActivity::class.java)
                    i.putExtra(Sender::class.simpleName, sender)
                    startActivity(i)
                }
                approveBtn.setOnClickListener {

                }
                closeBtn.setOnClickListener {

                }
            }
        }
    }
}