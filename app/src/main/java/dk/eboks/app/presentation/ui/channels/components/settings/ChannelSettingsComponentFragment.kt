package dk.eboks.app.presentation.ui.channels.components.settings

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.channel.ChannelFlags
import dk.eboks.app.domain.models.channel.storebox.StoreboxCreditCard
import dk.eboks.app.domain.models.channel.storebox.StoreboxProfile
import dk.eboks.app.domain.models.shared.Link
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.channels.screens.content.storebox.StoreboxAddCardActivity
import dk.eboks.app.util.Starter
import dk.eboks.app.util.setVisible
import kotlinx.android.synthetic.main.fragment_channel_settings_component.*
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import javax.inject.Inject

class ChannelSettingsComponentFragment : BaseFragment(), ChannelSettingsComponentContract.View {
    @Inject
    lateinit var presenter: ChannelSettingsComponentContract.Presenter
    @Inject
    lateinit var formatter: EboksFormatter

    private val adapter = CreditCardAdapter()
    private var isStorebox = false
    private var didShowCardView = false

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater?.inflate(R.layout.fragment_channel_settings_component, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated")

        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        arguments?.getCharSequence("arguments")?.let {
            isStorebox = (arguments.getCharSequence("arguments") == "storebox")
        }

        arguments?.getSerializable(Channel::class.java.simpleName)?.let { channel->
            presenter.setup((channel as Channel).id)
        }

        // should we open an display add a credit card webview immediately?
        arguments?.getBoolean("openAddCreditCards")?.let { open->
            if(open)
            {
                mainHandler.postDelayed({
                    presenter.getStoreboxCardLink()
                }, 100)
            }

        }
        showProgress(true)
    }

    override fun onResume() {
        super.onResume()
        if(isStorebox) {
            if (didShowCardView) {
                presenter.getCreditCards()
            }
        }
    }

    override fun setupChannel(channel: Channel) {
        channel.supportPinned?.let { supported->
            if(supported)
            {
                pinContainerLl.visibility = View.VISIBLE
                pinSliderSwitch.isChecked = channel.pinned ?: false
                pinSliderSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                    Timber.d("_pin slider checked$isChecked")
                    presenter.updateChannelFlags(channel, ChannelFlags(pinned = isChecked))
                }
            }
            else
            {
                mainSwitchContainerLl.visibility = View.GONE
                pinContainerLl.visibility = View.GONE
            }
        }

        removeChannelBtn.setOnClickListener {
            showRemoveChannelDialog()
        }

        if (isStorebox) {
            setupStorebox()
        } else {
            showProgress(false)
        }
    }

    private fun setupStorebox() {
        pinContainerLl.visibility = View.VISIBLE

        // TODO renable notification settings in a later version
        //hide row divider if notifications container is also hidden
        //notificationContainerLl.visibility = View.GONE
        //rowDivider.visibility = notificationContainerLl.visibility

        storeboxProfileLl.visibility = View.VISIBLE
        creditCardContainerLl.visibility = View.VISIBLE
        creditcardRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        creditcardRv.adapter = adapter

        addCardLl.setOnClickListener {
            //Timber.v("_add card clicked")
            presenter.getStoreboxCardLink()
        }

        presenter.getStoreboxProfile()
    }

    private fun showRemoveChannelDialog() {
        if(isStorebox) {
            AlertDialog.Builder(context)
                    .setTitle(Translation.channelsettingsstoreboxadditions.removeChannelTitle)
                    .setMessage(Translation.channelsettingsstoreboxadditions.removeChannelMessage)
                    .setPositiveButton(Translation.channelsettingsstoreboxadditions.deleteCardAlertButton.toUpperCase()) { dialog, which ->
                        presenter.deleteStoreboxAccountLink()
                    }
                    .setNegativeButton(Translation.channelsettingsstoreboxadditions.deleteCardCancelButton) { dialog, which ->

                    }
                    .create()
                    .show()
        }
        else
        {
            AlertDialog.Builder(context)
                    .setTitle(Translation.channelsettings.confirmDeleteTitle)
                    .setMessage(Translation.channelsettings.confirmDeleteMessageReplaceChannelName.replace("[channelname]", presenter.currentChannel?.name ?: ""))
                    .setPositiveButton(Translation.channelsettings.confirmRemoveButton.toUpperCase()) { dialog, which ->
                        presenter.removeChannel()
                    }
                    .setNegativeButton(Translation.defaultSection.cancel) { dialog, which ->

                    }
                    .create()
                    .show()
        }
    }

    override fun setCreditCards(cards: MutableList<StoreboxCreditCard>) {
        showProgress(false)
        adapter.creditCards.clear()
        adapter.creditCards.addAll(cards)
        adapter.notifyDataSetChanged()

        showEmptyView(cards.isEmpty())
    }

    override fun showProgress(boolean: Boolean) {
        progressBar.setVisible(boolean)

        containerContent.visibility = if (boolean) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }

    override fun showEmptyView(boolean: Boolean) {

    }

    override fun broadcastCloseChannel() {
        EventBus.getDefault().post(CloseChannelEvent())
    }

    override fun showAddCardView(link: Link) {
        didShowCardView = true
        activity.Starter().activity(StoreboxAddCardActivity::class.java).putExtra(Link::class.java.simpleName, link).start()
    }

    override fun setOnlyDigitalReceipts(onlyDigital: Boolean) {
        Timber.d("Setting digital receipts to $onlyDigital")
        optionalSliderSwitch.isChecked = onlyDigital
        optionalSliderSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            presenter.saveStoreboxProfile(StoreboxProfile(isChecked))
        }
    }

    override fun closeView()
    {
        activity.finish()
    }

    inner class CreditCardAdapter : RecyclerView.Adapter<CreditCardAdapter.CreditCardViewHolder>() {
        var creditCards: MutableList<StoreboxCreditCard> = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardViewHolder {
            val v = LayoutInflater.from(context).inflate(
                    R.layout.viewholder_channel_settings_creditcard_row,
                    parent,
                    false
            )
            return CreditCardViewHolder(v)
        }

        override fun getItemCount(): Int {
            return creditCards.size
        }

        override fun onBindViewHolder(holder: CreditCardViewHolder?, position: Int) {
            val currentCard = creditCards[position]
            holder?.bind(currentCard)
        }

        inner class CreditCardViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
            private var creditNumberTv = root.findViewById<TextView>(R.id.creditNumberTv)
            private var deleteIb = root.findViewById<ImageButton>(R.id.deleteIb)

            fun bind(storeboxCreditCard: StoreboxCreditCard) {
                creditNumberTv?.text = storeboxCreditCard.maskedCardNumber

                deleteIb?.setOnClickListener {
                    showDialog(storeboxCreditCard)
                }
            }

            private fun showDialog(storeboxCreditCard: StoreboxCreditCard) {
                AlertDialog.Builder(context)
                        .setTitle(Translation.channelsettingsstoreboxadditions.deleteCardAlertTitle)
                        .setPositiveButton(Translation.channelsettingsstoreboxadditions.deleteCardAlertButton.toUpperCase()) { dialog, which ->
                            showProgress(true)
                            presenter.deleteCreditCard(storeboxCreditCard.id)
                        }
                        .setNegativeButton(Translation.channelsettingsstoreboxadditions.deleteCardCancelButton) { dialog, which ->
                        }
                        .create()
                        .show()
            }
        }
    }

}