package dk.eboks.app.presentation.ui.components.channels.mainscreen

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.pasta.fragment.PastaComponentContract
import dk.eboks.app.presentation.base.BaseFragment
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ChannelMainScreenComponentFragment : BaseFragment(), ChannelMainScreenComponentContract.View {

    var cards: MutableList<ChannelCards> = ArrayList()

    @Inject
    lateinit var presenter : ChannelMainScreenComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_mainscreen_channel_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    override fun setupTranslations() {

    }


    inner class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

        inner class MessageViewHolder(val root : View) : RecyclerView.ViewHolder(root)
        {
            val backgroundIv = root.findViewById<ImageView>(R.id.backgroundIv)
            val backgroundColorLl = root.findViewById<LinearLayout>(R.id.backgroundColorLl)
            val headlineTv = root.findViewById<TextView>(R.id.headerTv)
            val iconIv = root.findViewById<ImageView>(R.id.iconIv)
            val nameTv = root.findViewById<TextView>(R.id.nameTv)
            val button = root.findViewById<Button>(R.id.button)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            val v = LayoutInflater.from(context).inflate(R.layout.viewholder_channel_cards, parent, false)
            val vh = MessageViewHolder(v)
            return vh
        }

        override fun getItemCount(): Int {
            return cards.size
        }

        override fun onBindViewHolder(holder: MessageViewHolder?, position: Int) {
            if(messages[position].sender != null) {
                holder?.circleIv?.let {

                    Glide.with(context).load(messages[position].sender?.logo).into(it)
                    it.isSelected = messages[position].unread
                }
            }
            holder?.titleTv?.text = messages[position].sender?.name
            holder?.subTitleTv?.text = messages[position].name
            holder?.root?.setOnClickListener {
                Timber.e("supposed to launch")
                presenter.setCurrentMessage(messages[position])
                startActivity(Intent(context, MessageSheetActivity::class.java))
            }
            holder?.root?.setOnLongClickListener {
                Timber.e("supposed to launch")
                presenter.setCurrentMessage(messages[position])
                startActivity(Intent(context, MessageActivity::class.java))
                true
            }
        }
    }
}