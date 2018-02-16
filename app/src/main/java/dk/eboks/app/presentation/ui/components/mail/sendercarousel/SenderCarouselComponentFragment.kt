package dk.eboks.app.presentation.ui.components.mail.sendercarousel

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.models.Sender
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_sender_carousel_component.*
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
        if(showEmptyState)
        {
            sendersListEmptyLl.visibility = View.VISIBLE
            sendersListLl.visibility = View.GONE
        }
        else
        {
            sendersListLl.visibility = View.VISIBLE
            sendersListEmptyLl.visibility = View.GONE
        }
    }

    override fun showError(msg: String) {
        Timber.e(msg)
    }

    override fun showSenders(senders: List<Sender>) {
        this.senders.clear()
        this.senders.addAll(senders)
        sendersRv.adapter.notifyDataSetChanged()
    }

    inner class HorizontalSendersAdapter : RecyclerView.Adapter<HorizontalSendersAdapter.CircularSenderViewHolder>() {

        inner class CircularSenderViewHolder(root : View) : RecyclerView.ViewHolder(root)
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
                Glide.with(context).load(senders[position].logo).into(it)
                it.isSelected = senders[position].unreadCount > 0
            }


        }
    }

}