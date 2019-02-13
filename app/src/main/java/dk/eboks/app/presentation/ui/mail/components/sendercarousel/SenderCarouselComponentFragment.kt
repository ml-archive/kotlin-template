package dk.eboks.app.presentation.ui.mail.components.sendercarousel

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.login.components.verification.VerificationComponentFragment
import dk.eboks.app.presentation.ui.mail.screens.list.MailListActivity
import dk.eboks.app.presentation.ui.mail.screens.overview.MailOverviewActivity
import dk.eboks.app.presentation.ui.senders.screens.list.SenderAllListActivity
import dk.eboks.app.util.getWorkaroundUrl
import dk.eboks.app.util.visible
import kotlinx.android.synthetic.main.fragment_sender_carousel_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class SenderCarouselComponentFragment : BaseFragment(), SenderCarouselComponentContract.View {

    @Inject
    lateinit var presenter: SenderCarouselComponentContract.Presenter

    var senders: MutableList<Sender> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sender_carousel_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupRecyclerView()

        sendersShowAllTv.setOnClickListener {
            startActivity(Intent(context, SenderAllListActivity::class.java))
        }
        if (!BuildConfig.ENABLE_SENDERS) {
            addMoreSendersBtn.visibility = View.GONE
        }

        emptyStateBtn.setOnClickListener {
            MailOverviewActivity.refreshOnResume = true
            VerificationComponentFragment.verificationSucceeded = false
            getBaseActivity()?.openComponentDrawer(VerificationComponentFragment::class.java)
        }
    }

    fun setupRecyclerView() {
        sendersRv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context,
            RecyclerView.HORIZONTAL,
            false
        )
        sendersRv.adapter = HorizontalSendersAdapter()
    }

    override fun showSenders(senders: List<Sender>) {
        this.senders.clear()
        this.senders.addAll(senders)
        sendersRv.adapter?.notifyDataSetChanged()
    }

    override fun showProgress(show: Boolean) {
        progressFl.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showEmpty(show: Boolean, verified: Boolean) {
        if (verified) {
            sendersListEmptyUnverifiedLl.visible = (false)
            sendersListEmptyLl.visible = (show)
        } else {
            sendersListEmptyLl.visible = (false)
            sendersListEmptyUnverifiedLl.visible = (show)
        }
        sendersListLl.visible = (!show)
    }

    override fun showEmpty(show: Boolean) {
        sendersListEmptyLl.visibility = if (show) View.VISIBLE else View.GONE
        sendersListLl.visibility = if (!show) View.VISIBLE else View.GONE
    }

    inner class HorizontalSendersAdapter :
        RecyclerView.Adapter<HorizontalSendersAdapter.CircularSenderViewHolder>() {

        inner class CircularSenderViewHolder(val root: View) : RecyclerView.ViewHolder(root) {

            val circleIv = root.findViewById<ImageView>(R.id.circleIv)
            val senderNameTv = root.findViewById<TextView>(R.id.senderNameTv)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CircularSenderViewHolder {
            val v = LayoutInflater.from(context)
                .inflate(R.layout.viewholder_circular_sender, parent, false)
            return CircularSenderViewHolder(v)
        }

        override fun getItemCount(): Int {
            return senders.size
        }

        override fun onBindViewHolder(holder: CircularSenderViewHolder, position: Int) {
            holder.circleIv?.let {
                if (senders[position].logo != null)
                    Glide.with(it.context)
                        .setDefaultRequestOptions(
                            RequestOptions().placeholder(R.drawable.ic_sender_placeholder).error(
                                R.drawable.ic_sender_placeholder
                            )
                        )
                        .load(senders[position].logo?.getWorkaroundUrl())
                        .into(it)
            }
            holder.senderNameTv?.text = senders[position].name
            holder.root.let {
                // it.isSelected = senders[position].messages?.metadata?.unreadCount ?: 0 > 0
                it.isSelected = senders[position].unreadMessageCount > 0
                it.setOnClickListener {

                    val i = Intent(context, MailListActivity::class.java)
                    i.putExtra("sender", senders[position])
                    startActivity(i)
                }
            }
        }
    }
}