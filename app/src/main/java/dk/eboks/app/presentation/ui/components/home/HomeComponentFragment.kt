package dk.eboks.app.presentation.ui.components.home

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Image
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.home.Control
import dk.eboks.app.domain.models.home.Item
import dk.eboks.app.domain.models.home.ItemType
import dk.eboks.app.domain.models.shared.Currency
import dk.eboks.app.domain.models.shared.Status
import dk.eboks.app.presentation.base.BaseFragment
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.fragment_home_overview_mail_component.*
import kotlinx.android.synthetic.main.viewholder_home_reciept_row.*
import kotlinx.android.synthetic.main.viewholder_message.*
import java.util.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class HomeComponentFragment : BaseFragment(), HomeComponentContract.View {

    @Inject
    lateinit var presenter: HomeComponentContract.Presenter
    @Inject
    lateinit var formatter: EboksFormatter

    //mock data
    var emailCount = 3
    var channelCount = 1
    var verifiedUser = true
    var messages: MutableList<dk.eboks.app.domain.models.message.Message> = ArrayList()
    var channels: MutableList<dk.eboks.app.domain.models.home.Control> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_home_overview_mail_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        NStack.translate()
        // create mocks
        createMockChannels()
        createMockMails(emailCount)
        // setup mocks
        setupViews()
    }

    private fun setupViews() {
        //  This is just a semi mock setup function to test ui

        if (verifiedUser) {
            showMails()
        } else {
            showEmptyState()
        }
        setupBottomView()
    }

    private fun showEmptyState() {
        emptyStateLl.visibility = View.VISIBLE
        emailContainerLl.visibility = View.GONE
        if(channelCount>0) {
            emptyStateImageIv.visibility = View.GONE
        }
    }

    private fun setupBottomView() {
        if (channelCount == 0) {
            channelsHeaderFL.visibility = View.GONE
            bottomChannelBtn.isEnabled = verifiedUser
            bottomChannelHeaderTv.text = Translation.home.bottomChannelHeaderNoChannels
            bottomChannelTextTv.text = Translation.home.bottomChannelTextNoChannels
        } else {
            bottomChannelBtn.isEnabled = false
            bottomChannelHeaderTv.text = Translation.home.bottomChannelHeaderChannels
            bottomChannelTextTv.text = Translation.home.bottomChannelTextChannels

            // setting up channels - reciept
            for(i in 1..channels.size){
                var currentChannel = channels[i - 1]
                val v = inflator.inflate(R.layout.viewholder_home_receipt, channelsContainerLl, false)

                val logoIv = v.findViewById<ImageView>(R.id.logoIv)
                val headerTv = v.findViewById<TextView>(R.id.headerTv)
                val rowsContainerLl = v.findViewById<LinearLayout>(R.id.rowsContainerLl)

                //todo set the logo
                headerTv.text = currentChannel.id

                for (row in currentChannel.items){
                    val v = inflator.inflate(R.layout.viewholder_home_reciept_row, rowsContainerLl, false)

                    val nameContainer = v.findViewById<LinearLayout>(R.id.nameSubTitleContainerLl)
                    val amountDateContainer = v.findViewById<LinearLayout>(R.id.amountDateContainerLl)
                    val soloName = v.findViewById<TextView>(R.id.soloTitleTv)
                    val soloAmount = v.findViewById<TextView>(R.id.soloAmountTv)
                    val name = v.findViewById<TextView>(R.id.titleTv)
                    val adress = v.findViewById<TextView>(R.id.subTitleTv)
                    val amount = v.findViewById<TextView>(R.id.amountTv)
                    val date = v.findViewById<TextView>(R.id.dateTv)

                    var value = row.amount?.value.toString()
                    if (row.date == null){
                        //Todo need to format the string to use comma seperator
                        //todo need to format the date correctly
                        soloAmount.text = value
                        soloAmount.visibility = View.VISIBLE
                        amountDateContainer.visibility = View.GONE
                    } else {
                        amount.text = value
                        date.text = row.date.toString()
                    }

                    if(row.description == null){
                        soloName.text = row.id
                        soloName.visibility = View.VISIBLE
                        nameContainer.visibility = View.GONE
                    } else {
                        name.text = row.id
                        adress.text = row.description
                    }




                    rowsContainerLl.addView(v)
                    rowsContainerLl.requestLayout()
                }

                channelsContentLL.addView(v)
                channelsContentLL.requestLayout()
            }
        }
    }

    private fun createMockChannels() {
        // receipt
        var items: ArrayList<Item> = ArrayList()
        items.add(Item("ID-receipt", "Title-reciept","Description-reciept",Date(),Currency(111.01,"DKK"), null,null, null))
        items.add(Item("ID-receipt2", "Title-reciept2",null,null,Currency(222.02,"DK2"), null,null, null))
        channels.add(Control("control receipts",ItemType.RECEIPTS,items))

        var items2: ArrayList<Item> = ArrayList()
        items2.add(Item("ID-receipt3", "Title-reciept3","Description-reciept3",Date(),Currency(333.03,"DK3"), null,null, null))
        items2.add(Item("ID-receipt4", "Title-reciept4",null,null,Currency(444.04,"DK4"), null,null, null))
        channels.add(Control("control receipts2",ItemType.RECEIPTS,items2))
        // news
        // image
        // notification
        //files
        // message

    }

    private fun showMails() {
        if (emailCount == 0) {
            emptyStateLl.visibility = View.VISIBLE
            emailContainerLl.visibility = View.GONE
            emptyStateBtn.isEnabled = verifiedUser
            emptyStateImageIv.visibility = View.GONE
            emptyStateBtn.text = Translation.home.messagesEmptyButton
            emptyStateHeaderTv.text = Translation.home.messagesEmptyTitle
            emptyStateTextTv.text = Translation.home.messagesEmptyMessage
        } else {
            emptyStateLl.visibility = View.GONE
            emailContainerLl.visibility = View.VISIBLE
            mailListContentLL.removeAllViews()


            var showCount = 3 // Not allowed to show more than 3
            if (messages.size < showCount) {
                showCount = messages.size
            }

            for (i in 1..showCount) {
                val v = inflator.inflate(R.layout.viewholder_message, mailListContentLL, false)
                var currentMessage = messages[i - 1]
                val circleIv = v.findViewById<ImageView>(R.id.circleIv)
                val titleTv = v.findViewById<TextView>(R.id.titleTv)
                val subTitleTv = v.findViewById<TextView>(R.id.subTitleTv)
                val urgentTv = v.findViewById<TextView>(R.id.urgentTv)
                val dateTv = v.findViewById<TextView>(R.id.dateTv)
                val dividerV = v.findViewById<View>(R.id.dividerV)
                val rootLl = v.findViewById<LinearLayout>(R.id.rootLl)
                titleTv.text = currentMessage.id
                subTitleTv.text = currentMessage.subject
                dateTv.text = formatter.formatDate(currentMessage)
                rootLl.setBackgroundColor(resources.getColor(R.color.white))
                if (i == showCount) {
                    dividerV.visibility = View.GONE
                }
                mailListContentLL.addView(v)
                mailListContentLL.requestLayout()

                if (messages.size > 3) {
                    showBtn.isEnabled = true
                    showBtn.text = Translation.home.messagesSectionHeaderButtonNewMessagesSuffix.replace("[value]", messages.size.toString())
                }

            }
        }
    }

    fun createMockMails(emailCount: Int) {
        for (i in 1..emailCount) {
            messages.add(dk.eboks.app.domain.models.message.Message("id" + i, "subject" + i, Date(), false, null, null, null, null, null, null, 0, null, null, null, null, null, "note string"))
        }
    }
}