package dk.eboks.app.presentation.ui.components.mail.sendercarousel

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.senders.detail.SenderDetailActivity
import kotlinx.android.synthetic.main.fragment_sender_carousel_component.*
import kotlinx.android.synthetic.main.viewholder_circular_sender.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class SenderCarouselComponentFragment : BaseFragment(), SenderCarouselComponentContract.View {

    @Inject
    lateinit var presenter : SenderCarouselComponentContract.Presenter

    var senders : MutableList<Sender> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_sender_carousel_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupRecyclerView()
    }

    override fun setupTranslations() {
        sendersHeaderTv.text = Translation.mail.senderHeader
        sendersShowAllTv.text = Translation.mail.showAllSendersButton
        sendersEmptyHeaderTv.text = Translation.mail.sendersEmptyHeader
        sendersEmptyDescTv.text = Translation.mail.sendersEmptyMessage
        addMoreSendersBtn.text = Translation.mail.addMoreSendersButton
    }

    fun setupRecyclerView()
    {
        sendersRv.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        sendersRv.adapter = HorizontalSendersAdapter()
    }


    override fun onShake() {
        showEmpty(showEmptyState)
    }

    override fun showError(msg: String) {
        Timber.e(msg)
    }

    override fun showSenders(senders: List<Sender>) {
        this.senders.clear()
        this.senders.addAll(senders)
        sendersRv.adapter.notifyDataSetChanged()
    }

    override fun showProgress(show: Boolean) {
        progressFl.visibility = if(show) View.VISIBLE else View.GONE
    }

    override fun showEmpty(show: Boolean) {
        sendersListEmptyLl.visibility = if(show) View.VISIBLE else View.GONE
        sendersListLl.visibility = if(!show) View.VISIBLE else View.GONE
    }

    inner class HorizontalSendersAdapter : RecyclerView.Adapter<HorizontalSendersAdapter.CircularSenderViewHolder>() {

        inner class CircularSenderViewHolder(val root : View) : RecyclerView.ViewHolder(root)
        {

            val circleIv = root.findViewById<ImageView>(R.id.circleIv)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircularSenderViewHolder {
            val v = LayoutInflater.from(context).inflate(R.layout.viewholder_circular_sender, parent, false)
            val vh = CircularSenderViewHolder(v)
            return vh
        }

        override fun getItemCount(): Int {
            return senders.size
        }

        override fun onBindViewHolder(holder: CircularSenderViewHolder?, position: Int) {
            holder?.circleIv?.let {
                if(senders[position].logo != null)
                    Glide.with(context).load(senders[position].logo?.url).into(it)
            }
            holder?.root?.let {
                it.isSelected = senders[position].unreadCount > 0
                it.setOnClickListener {
                val i = Intent(context, SenderDetailActivity::class.java )
                i.putExtra(Sender::class.simpleName, senders[position])
                startActivity(i)
            }
            }
        }
    }

}