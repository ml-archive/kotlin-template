package dk.eboks.app.presentation.ui.components.home

import android.graphics.Color
import android.os.Bundle
import android.support.v4.media.VolumeProviderCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AlertDialogLayout
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Image
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.home.Control
import dk.eboks.app.domain.models.home.Item
import dk.eboks.app.domain.models.home.ItemType
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.shared.Currency
import dk.eboks.app.domain.models.shared.Status
import dk.eboks.app.presentation.base.BaseFragment
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.fragment_home_overview_mail_component.*
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
    var emailCount = 0
    var channelCount = 2
    override var verifiedUser: Boolean = false
    var messages: MutableList<dk.eboks.app.domain.models.message.Message> = ArrayList()
    var channels: MutableList<dk.eboks.app.domain.models.home.Control> = ArrayList()
    var redidValues = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_home_overview_mail_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        NStack.translate()


        // setup mocks
        presenter.setup()
    }

    override fun setupViews() {
        //  This is just a semi mock setup function to test ui
        // create mocks
        createMockChannels(channelCount)
        createMockMails(emailCount)
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
        if (channels.size > 0) {
            emptyStateImageIv.visibility = View.GONE
        }
    }

    private fun setupBottomView() {
        if (channels.size == 0) {
            channelsHeaderFL.visibility = View.GONE
            bottomChannelBtn.isEnabled = (emailCount > 0)
            bottomChannelHeaderTv.text = Translation.home.bottomChannelHeaderNoChannels
            bottomChannelTextTv.text = Translation.home.bottomChannelTextNoChannels
            bottomChannelBtn.visibility = View.VISIBLE
            bottomChannelHeaderTv.visibility = View.VISIBLE
            bottomChannelTextTv.visibility = View.VISIBLE
        } else {
            if (channels.size < 2) {
                bottomChannelBtn.isEnabled = false
                bottomChannelHeaderTv.text = Translation.home.bottomChannelHeaderChannels
                bottomChannelTextTv.text = Translation.home.bottomChannelTextChannels
            } else {
                bottomChannelBtn.visibility = View.GONE
                bottomChannelHeaderTv.visibility = View.GONE
                bottomChannelTextTv.visibility = View.GONE
            }

            for (i in 1..channels.size) {
                var currentChannel = channels[i - 1]

                //setting the header
                val v = inflator.inflate(R.layout.viewholder_home_card_header, channelsContainerLl, false)
                val logoIv = v.findViewById<ImageView>(R.id.logoIv)
                val headerTv = v.findViewById<TextView>(R.id.headerTv)
                val rowsContainerLl = v.findViewById<LinearLayout>(R.id.rowsContainerLl)

                //todo set the logo - is this the correct image ? Most likely we need the logo from the initial api call
                logoIv?.let {
                    if (currentChannel.items?.isNotEmpty() && currentChannel.items?.first()?.Image != null)
                        Glide.with(context).load(currentChannel.items.first().Image?.url).into(it)
                }
                headerTv.text = currentChannel.id

                //inflating the rows based on itemtype
                when (currentChannel.type) {
                    ItemType.RECEIPTS -> {
                        addRecieptCard(currentChannel, rowsContainerLl)
                    }
                    ItemType.NEWS -> {
                        addNewsCard(currentChannel, rowsContainerLl)
                    }
                    ItemType.IMAGES -> {
                        //contrained to only show 1 row
                        addImageCard(currentChannel, rowsContainerLl)

                    }
                    ItemType.NOTIFICATIONS -> {
                        addNotificationCard(currentChannel, rowsContainerLl)

                    }
                    ItemType.MESSAGES -> {
                        addMessageCard(currentChannel, rowsContainerLl)

                    }
                    ItemType.FILES -> {
                        addFilesCard(currentChannel, rowsContainerLl)
                    }
                }

                channelsContentLL.addView(v)
                channelsContentLL.requestLayout()

            }
        }
    }

    private fun addFilesCard(currentChannel: Control, rowsContainerLl: ViewGroup) {
        for (currentItem in currentChannel.items) {
            val v = inflator.inflate(R.layout.viewholder_message, rowsContainerLl, false)
            val title = v.findViewById<TextView>(R.id.titleTv)
            val subtitle = v.findViewById<TextView>(R.id.subTitleTv)
            val date = v.findViewById<TextView>(R.id.dateTv)
            val image = v.findViewById<ImageView>(R.id.circleIv)
            val urgent = v.findViewById<TextView>(R.id.urgentTv)
            val bottomDivider = v.findViewById<View>(R.id.dividerV)
            val topDivider = v.findViewById<View>(R.id.topDividerV)

            bottomDivider.visibility = View.GONE
            topDivider.visibility = View.VISIBLE


            title.text = currentItem.title
            subtitle.text = currentItem.description
            date.text = formatter.formatDateRelative(currentItem)
            image?.let {
                if (currentItem.Image != null)
                    Glide.with(context).load(R.drawable.ic_menu_uploads).into(it)
            }

            rowsContainerLl.addView(v)
            rowsContainerLl.requestLayout()
        }
    }

    private fun addMessageCard(currentChannel: Control, rowsContainerLl: ViewGroup) {
        for (currentItem in currentChannel.items) {
            val v = inflator.inflate(R.layout.viewholder_message, rowsContainerLl, false)
            val title = v.findViewById<TextView>(R.id.titleTv)
            val subtitle = v.findViewById<TextView>(R.id.subTitleTv)
            val date = v.findViewById<TextView>(R.id.dateTv)
            val image = v.findViewById<ImageView>(R.id.circleIv)
            val urgent = v.findViewById<TextView>(R.id.urgentTv)
            val bottomDivider = v.findViewById<View>(R.id.dividerV)
            val topDivider = v.findViewById<View>(R.id.topDividerV)

            bottomDivider.visibility = View.GONE
            topDivider.visibility = View.VISIBLE

            val currentStatus = currentItem.status
            if (currentStatus != null && currentStatus.important) {
                urgent.visibility = View.VISIBLE
                urgent.text = currentStatus.title
            }


            title.text = currentItem.title
            subtitle.text = currentItem.description
            date.text = formatter.formatDateRelative(currentItem)
            image.isSelected = true
            image?.let {
                if (currentItem.Image != null)
                    Glide.with(context).load(currentItem.Image?.url).into(it)
            }

            rowsContainerLl.addView(v)
            rowsContainerLl.requestLayout()
        }
    }

    private fun addNotificationCard(currentChannel: Control, rowsContainerLl: ViewGroup) {

        // empty state
        if (currentChannel.items.isEmpty()) {
            val v = inflator.inflate(R.layout.viewholder_home_notification_row, rowsContainerLl, false)
            val emptyContainer = v.findViewById<LinearLayout>(R.id.emptyContentContainer)
            val contentContainer = v.findViewById<LinearLayout>(R.id.contentContainer)

            emptyContainer.visibility = View.VISIBLE
            contentContainer.visibility = View.GONE

            rowsContainerLl.addView(v)
            rowsContainerLl.requestLayout()
        }

        // non-empty state
        for (currentItem in currentChannel.items) {
            val v = inflator.inflate(R.layout.viewholder_home_notification_row, rowsContainerLl, false)
            val title = v.findViewById<TextView>(R.id.titleTv)
            val subtitle = v.findViewById<TextView>(R.id.subTitleTv)
            val date = v.findViewById<TextView>(R.id.dateTv)

            title.text = currentItem.title
            subtitle.text = currentItem.description
            date.text = formatter.formatDateRelative(currentItem)

            rowsContainerLl.addView(v)
            rowsContainerLl.requestLayout()
        }
    }

    private fun addImageCard(currentChannel: Control, rowsContainerLl: ViewGroup) {
        val v = inflator.inflate(R.layout.viewholder_home_image_row, rowsContainerLl, false)
        val title = v.findViewById<TextView>(R.id.titleTv)
        val image = v.findViewById<ImageView>(R.id.backgroundIv)
        val background = v.findViewById<LinearLayout>(R.id.backgroundColorLl)

        val currentItem = currentChannel.items?.first()
        //todo this color should come from the channels object
        var tintColor = "#bf112233"
        background?.background?.setTint(Color.parseColor(tintColor))

        title.text = currentItem.title
        image?.let {
            if (currentItem.Image != null)
                Glide.with(context).load(currentItem.Image?.url).into(it)
        }

        rowsContainerLl.addView(v)
        rowsContainerLl.requestLayout()
    }

    private fun addNewsCard(currentChannel: Control, rowsContainerLl: ViewGroup) {
        for (currentItem in currentChannel.items) {
            val v = inflator.inflate(R.layout.viewholder_home_news_row, rowsContainerLl, false)
            val title = v.findViewById<TextView>(R.id.titleTv)
            val image = v.findViewById<ImageView>(R.id.imageIv)
            val date = v.findViewById<TextView>(R.id.dateTv)

            title.text = currentItem.title
            date.text = formatter.formatDateRelative(currentItem)
            image?.let {
                if (currentItem.Image != null)
                    Glide.with(context).load(currentItem.Image?.url).into(it)
            }

            rowsContainerLl.addView(v)
            rowsContainerLl.requestLayout()
        }
    }

    private fun addRecieptCard(currentChannel: Control, rowsContainerLl: ViewGroup) {
        for (row in currentChannel.items) {
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
            if (row.date == null) {
                //Todo need to format the string to use comma seperator
                soloAmount.text = value
                soloAmount.visibility = View.VISIBLE
                amountDateContainer.visibility = View.GONE
            } else {
                amount.text = value
                date.text = formatter.formatDateRelative(row)
            }

            if (row.description == null) {
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
    }

    override fun onShake() {
       if(!redidValues){
           redidValues = true
           val alert = AlertDialog.Builder(context)
           var layout = inflator.inflate(R.layout.debug_dialog,null,false)
           var mailEt = layout.findViewById<EditText>(R.id.firstEt)
           var channelEt = layout.findViewById<EditText>(R.id.middleEt)
           var verifiedEt = layout.findViewById<EditText>(R.id.lastEt)

           // Builder
           with (alert) {
               setTitle("Setup Data")
               setMessage("channelmode 0=none, 1=1, 2= all ")

               // Add any  input field here
               mailEt!!.hint="MailCount"
               channelEt!!.hint="channelmode(0,1,2)"
               verifiedEt!!.hint="Verified:1 = true"

               setPositiveButton("OK") {
                   dialog, whichButton ->
                   emailCount = Integer.parseInt(mailEt.text.toString())
                   channelCount = Integer.parseInt(channelEt.text.toString())
                   verifiedUser = (Integer.parseInt(verifiedEt.text.toString())==1)
                   channels.clear()
                   messages.clear()
                   val channelContentLl = view?.findViewById<LinearLayout>(R.id.channelsContentLL)
                   channelContentLl?.removeAllViews()
                   setupViews()
                   dialog.dismiss()

               }

               setNegativeButton("NO") {
                   dialog, whichButton ->
                   dialog.dismiss()
               }
           }

           // Dialog
           val dialog = alert.create()
           dialog.setView(layout)
           dialog.show()
       }
    }

    private fun createMockChannels(showChannelsMode: Int) {

        // showsChannelsMode: 0  = none, 1 = 1, 2 = all

        // shows 1
        if(showChannelsMode == 1){
            var items: ArrayList<Item> = ArrayList()
            items.add(Item("ID-receipt", "Title-reciept", "Description-reciept", Date(), Currency(111.01, "DKK"), null, null, Image("https://picsum.photos/200/?random")))
            items.add(Item("ID-receipt2", "Title-reciept2", null, null, Currency(222.02, "DK2"), null, null, null))
            channels.add(Control("control receipts", ItemType.RECEIPTS, items))

        }
        // shows all
        if (showChannelsMode == 2) {
            // receipt
            var items: ArrayList<Item> = ArrayList()
            items.add(Item("ID-receipt", "Title-reciept", "Description-reciept", Date(), Currency(111.01, "DKK"), null, null, Image("https://picsum.photos/200/?random")))
            items.add(Item("ID-receipt2", "Title-reciept2", null, null, Currency(222.02, "DK2"), null, null, null))
            channels.add(Control("control receipts", ItemType.RECEIPTS, items))

            var items2: ArrayList<Item> = ArrayList()
            items2.add(Item("ID-receipt3", "Title-reciept3", null, Date(), Currency(333.03, "DK3"), null, null, Image("https://picsum.photos/200/?random")))
            items2.add(Item("ID-receipt4", "Title-reciept4", null, null, Currency(444.04, "DK4"), null, null, null))
            channels.add(Control("control receipts2", ItemType.RECEIPTS, items2))

            // news
            var items3: ArrayList<Item> = ArrayList()
            items3.add(Item("ID-news1", "Title-news1", "Description-news1", Date(), null, null, null, Image("https://picsum.photos/200/?random")))
            channels.add(Control("control news1", ItemType.NEWS, items3))

            var items4: ArrayList<Item> = ArrayList()
            items4.add(Item("ID-news2", "Title-news2", "Description-news2", Date(), null, null, null, Image("https://picsum.photos/200/?random")))
            items4.add(Item("ID-news3", "Title-news3", "Description-news3", Date(), null, null, null, Image("https://picsum.photos/200/?random")))
            channels.add(Control("control news2", ItemType.NEWS, items4))

            // image
            var items5: ArrayList<Item> = ArrayList()
            items5.add(Item("ID-image1", "Title-image1", "Description-image1", Date(), null, null, null, Image("https://picsum.photos/200/?random")))
            channels.add(Control("control image1", ItemType.IMAGES, items5))

            // notification
            var items6: ArrayList<Item> = ArrayList()
            items6.add(Item("ID-notification1", "Title-notification1", "Description-notification1", Date(), null, null, null, Image("https://picsum.photos/200/?random")))
            items6.add(Item("ID-notification1", "Title-notification1", "Description-notification1", Date(), null, null, null, Image("https://picsum.photos/200/?random")))
            items6.add(Item("ID-notification1", "Title-notification1", "Description-notification1", Date(), null, null, null, Image("https://picsum.photos/200/?random")))
            channels.add(Control("control notification1", ItemType.NOTIFICATIONS, items6))

            var items7: ArrayList<Item> = ArrayList()
            channels.add(Control("control notification2", ItemType.NOTIFICATIONS, items7))

            // message
            var items8: ArrayList<Item> = ArrayList()
            items8.add(Item("ID-message1", "Title-Message1", "Description-message1", Date(), null, null, null, Image("https://picsum.photos/200/?random")))
            channels.add(Control("control message1", ItemType.MESSAGES, items8))

            var items9: ArrayList<Item> = ArrayList()
            items9.add(Item("ID-message2", "Title-Message2", "Description-message2", Date(), null, Status(true, "important title", "important text", 0, Date()), null, Image("https://picsum.photos/200/?random")))
            items9.add(Item("ID-message2", "Title-Message2", "Description-message2", Date(), null, null, null, Image("https://picsum.photos/200/?random")))
            channels.add(Control("control message1", ItemType.MESSAGES, items9))

            //files
            var items10: ArrayList<Item> = ArrayList()
            items10.add(Item("ID-files1", "Title-Files1", "Description-files1", Date(), null, null, null, Image("https://picsum.photos/200/?random")))
            channels.add(Control("control files1", ItemType.FILES, items10))

            var items11: ArrayList<Item> = ArrayList()
            items11.add(Item("ID-files2", "Title-Files2", "Description-files2", Date(), null, null, null, Image("https://picsum.photos/200/?random")))
            items11.add(Item("ID-files3", "Title-Files3", "Description-files3", Date(), null, null, null, Image("https://picsum.photos/200/?random")))
            items11.add(Item("ID-files4", "Title-Files4", "Description-files4", Date(), null, null, null, Image("https://picsum.photos/200/?random")))
            channels.add(Control("control files2", ItemType.FILES, items11))
        }
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
                //todo set the logo
                circleIv.let {
                    Glide.with(context).load("https://picsum.photos/200/?random").into(it)
                }
                if (currentMessage.unread) {
                    circleIv.isSelected = true
                }

                if (currentMessage.status != null && currentMessage.status!!.important) {
                    urgentTv.visibility = View.VISIBLE
                    urgentTv.text = currentMessage.status?.text
                }
                titleTv.text = currentMessage.id
                subTitleTv.text = currentMessage.subject
                dateTv.text = formatter.formatDateRelative(currentMessage)
                rootLl.setBackgroundColor(resources.getColor(R.color.white))
                if (i == showCount) {
                    dividerV.visibility = View.GONE
                }
                mailListContentLL.addView(v)
                mailListContentLL.requestLayout()

                // for this sprint it should not show the 3+ mail button, it should always show all
//                if (messages.size > 3) {
//                    showBtn.isEnabled = true
//                    showBtn.text = Translation.home.messagesSectionHeaderButtonNewMessagesSuffix.replace("[value]", messages.size.toString())
//                }

            }
        }
    }

    fun createMockMails(emailCount: Int) {
        for (i in 1..emailCount) {
            val random = Random()
            var unread = (random.nextInt(i) == 0)

            var randomStatus = Status(false, "important title", "important text", 0, Date())
            if (random.nextInt(i) == 0) {
                randomStatus.important = true
            }
            messages.add(dk.eboks.app.domain.models.message.Message("id" + i, "subject" + i, Date(), unread, null, null, null, null, null, null, 0, null, null, null, null, randomStatus, "note string"))
        }
    }



}