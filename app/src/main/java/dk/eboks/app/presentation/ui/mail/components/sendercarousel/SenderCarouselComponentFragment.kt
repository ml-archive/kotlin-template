package dk.eboks.app.presentation.ui.mail.components.sendercarousel

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.mail.presentation.ui.components.sendercarousel.SenderCarouselComponentContract
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.login.components.verification.VerificationComponentFragment
import dk.eboks.app.presentation.ui.mail.screens.list.MailListActivity
import dk.eboks.app.presentation.ui.mail.screens.overview.MailOverviewActivity
import dk.eboks.app.presentation.ui.senders.screens.list.SenderAllListActivity
import dk.eboks.app.util.getWorkaroundUrl
import dk.eboks.app.util.inflate
import dk.eboks.app.util.visible
import kotlinx.android.synthetic.main.fragment_sender_carousel_component.*
import kotlinx.android.synthetic.main.viewholder_circular_sender.view.*
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

    private fun setupRecyclerView() {
        sendersRv.layoutManager = LinearLayoutManager(
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
        progressFl.visible = show
    }

    override fun showEmpty(show: Boolean, verified: Boolean) {
        if (verified) {
            sendersListEmptyUnverifiedLl.visible = false
            sendersListEmptyLl.visible = show
        } else {
            sendersListEmptyLl.visible = false
            sendersListEmptyUnverifiedLl.visible = show
        }
        sendersListLl.visible = (!show)
    }

    override fun showEmpty(show: Boolean) {
        sendersListEmptyLl.visible = show
        sendersListLl.visible = !show
    }

    inner class HorizontalSendersAdapter :
        RecyclerView.Adapter<HorizontalSendersAdapter.CircularSenderViewHolder>() {

        inner class CircularSenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(sender: Sender) {
                itemView.run {
                    circleIv?.let {
                        if (sender.logo != null)
                            Glide.with(it.context)
                                .setDefaultRequestOptions(
                                    RequestOptions.circleCropTransform()
                                        .placeholder(R.drawable.ic_sender_placeholder)
                                        .error(R.drawable.ic_sender_placeholder)
                                )
                                .load(sender.logo?.getWorkaroundUrl())
                                .into(it)
                    }
                    senderNameTv?.text = sender.name

                    // isSelected = senders[position].messages?.metadata?.unreadCount ?: 0 > 0
                    isSelected = sender.unreadMessageCount > 0
                    setOnClickListener {

                        val i = Intent(context, MailListActivity::class.java)
                        i.putExtra("sender", sender)
                        startActivity(i)
                    }
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CircularSenderViewHolder {
            return CircularSenderViewHolder(parent.inflate(R.layout.viewholder_circular_sender))
        }

        override fun getItemCount(): Int {
            return senders.size
        }

        override fun onBindViewHolder(holder: CircularSenderViewHolder, position: Int) {
            holder.bind(senders[position])
        }
    }
}