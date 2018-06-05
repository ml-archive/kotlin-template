package dk.eboks.app.presentation.ui.components.channels.settings

import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.storebox.StoreboxCreditCard
import dk.eboks.app.domain.models.channel.storebox.StoreboxProfile
import dk.eboks.app.domain.models.shared.Link
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.channels.content.storebox.StoreboxAddCardActivity
import dk.eboks.app.util.Starter
import dk.eboks.app.util.setVisible
import kotlinx.android.synthetic.main.fragment_channel_settings_component.*
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

        showProgress(true)

        setup()
    }

    override fun onResume() {
        super.onResume()
        if(didShowCardView)
        {
            presenter.getCreditCards()
        }
    }


    private fun setup() {
        pinSliderSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            //todo do something when the slider is checked
            Timber.d("_pin slider checked$isChecked")
        }

        if (isStorebox) {
            setupStorebox()
        } else {

        }
    }

    private fun setupStorebox() {
        pinContainerLl.visibility = View.VISIBLE

        //hide row divider if notifications container is also hidden
        notificationContainerLl.visibility = View.GONE
        rowDivider.visibility = notificationContainerLl.visibility

        optionalSwitchContainerLl.visibility = View.VISIBLE
        creditCardContainerLl.visibility = View.VISIBLE
        creditcardRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        creditcardRv.adapter = adapter

        addCardLl.setOnClickListener {
            //todo something happends when you click add card
            Timber.v("_add card clicked")
            presenter.getStoreboxCardLink()
        }

        removeChannelBtn.setOnClickListener {
            showRemoveChannelDialog()
        }

        presenter.getStoreboxProfile()
    }

    private fun showRemoveChannelDialog() {
        AlertDialog.Builder(context)
                .setTitle(Translation.channelsettingsstoreboxadditions.removeChannelTitle)
                .setMessage(Translation.channelsettingsstoreboxadditions.removeChannelMessage)
                .setPositiveButton(Translation.channelsettingsstoreboxadditions.deleteCardAlertButton.toUpperCase()) { dialog, which ->
                    //todo send API call to remove channel
                    Toast.makeText(
                            context,
                            "_Positive openActionTv clicked.",
                            Toast.LENGTH_SHORT
                    ).show()
                }
                .setNegativeButton(Translation.channelsettingsstoreboxadditions.deleteCardCancelButton) { dialog, which ->
                    Toast.makeText(
                            context,
                            "_Negative openActionTv clicked",
                            Toast.LENGTH_SHORT
                    ).show()
                }
                .create()
                .show()
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