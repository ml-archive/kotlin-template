package dk.eboks.app.presentation.ui.components.channels.settings

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_channel_settings_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ChannelSettingsComponentFragment : BaseFragment(), ChannelSettingsComponentContract.View {

    @Inject
    lateinit var presenter: ChannelSettingsComponentContract.Presenter
    @Inject
    lateinit var formatter: EboksFormatter

    var isStorebox = false
    // mock stuff
    var creditcards: MutableList<String> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_channel_settings_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        arguments?.getCharSequence("arguments")?.let {
                isStorebox = (arguments.getCharSequence("arguments") == "storebox")
            }

        setup()

    }

    private fun setup() {
        createMocks(3)

        pinSliderSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            //todo do something when the slider is checked
            var temp = "_pin slider checked" + isChecked
            println(temp)
        }

        if (isStorebox) { setupStorebox() } else {

        }
    }

    private fun setupStorebox() {
        pinContainerLl.visibility = View.VISIBLE

        //hide rowdivider if notifications container is also hidden
        notificationContainerLl.visibility = View.GONE
        rowDivider.visibility = notificationContainerLl.visibility

        optionalSwitchContainerLl.visibility = View.VISIBLE
        creditCardContainerLl.visibility = View.VISIBLE
        creditcardRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        creditcardRv.adapter = CreditcardAdapter()

        optionalSliderSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            //todo do something when the slider is checked
            var temp = "_optional slider checked" + isChecked
            println(temp)
        }

        addCardFl.setOnClickListener {
            //todo something happends when you click add card
            var temp = "_add card clicked"
            println(temp)
        }

        removeChannelBtn.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
                    .setTitle(Translation.channelsettingsstoreboxadditions.removeChannelTitle)
                    .setMessage(Translation.channelsettingsstoreboxadditions.removeChannelMessage)
                    .setPositiveButton(Translation.channelsettingsstoreboxadditions.deleteCardAlertButton.toUpperCase()) { dialog, which ->
                        //todo send API call to remove channel
                        Toast.makeText(context, "_Positive button clicked.", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton(Translation.channelsettingsstoreboxadditions.deleteCardCancelButton) { dialog, which ->
                        Toast.makeText(context, "_Negative button clicked", Toast.LENGTH_SHORT).show()
                    }
                    .create()
                    .show()
        }
    }

    private fun createMocks(numberOfMocks: Int) {
        for (i in 1..numberOfMocks) {
            creditcards.add("_XXXX XXXX XXXX " + i + i + i + i)
        }
    }

    inner class CreditcardAdapter : RecyclerView.Adapter<CreditcardAdapter.CreditcardViewHolder>() {

        inner class CreditcardViewHolder(val root: View) : RecyclerView.ViewHolder(root) {

            var creditNumberTv = root.findViewById<TextView>(R.id.creditNumberTv)
            var deleteIb = root.findViewById<ImageButton>(R.id.deleteIb)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditcardViewHolder {
            val v = LayoutInflater.from(context).inflate(R.layout.viewholder_channel_settings_creditcard_row, parent, false)
            val vh = CreditcardViewHolder(v)
            return vh
        }

        override fun getItemCount(): Int {
            return creditcards.size
        }

        override fun onBindViewHolder(holder: CreditcardViewHolder?, position: Int) {
            var currentCard = creditcards[position]
            holder?.creditNumberTv?.text = currentCard

            holder?.deleteIb?.setOnClickListener {

                val dialog = AlertDialog.Builder(context)
                        .setTitle(Translation.channelsettingsstoreboxadditions.deleteCardAlertTitle)
                        .setPositiveButton(Translation.channelsettingsstoreboxadditions.deleteCardAlertButton.toUpperCase()) { dialog, which ->
                            //todo send API call to remove card
                            Toast.makeText(context, "_Positive button clicked.", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton(Translation.channelsettingsstoreboxadditions.deleteCardCancelButton) { dialog, which ->
                            Toast.makeText(context, "_Negative button clicked", Toast.LENGTH_SHORT).show()
                        }
                        .create()
                        .show()
            }
        }

    }

}