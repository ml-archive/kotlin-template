package dk.eboks.app.presentation.ui.components.home

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Image
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.home.Control
import dk.eboks.app.domain.models.home.Item
import dk.eboks.app.domain.models.home.ItemType
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.shared.Currency
import dk.eboks.app.domain.models.shared.Status
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.mail.overview.MailOverviewActivity
import dk.eboks.app.util.views
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.fragment_home_overview_mail_component.*
import kotlinx.android.synthetic.main.viewholder_home_card_header.view.*
import timber.log.Timber
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
    var channelCount = 0
    override var verifiedUser: Boolean = false
    //var messages: MutableList<dk.eboks.app.domain.models.message.Message> = ArrayList()
    //var channels: MutableList<dk.eboks.app.domain.models.home.Control> = ArrayList()
    //var redidValues = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_home_overview_mail_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        NStack.translate()
        refreshSrl.setOnRefreshListener {
            var stilLoading = false
            for (v in channelsContentLL.views) {
                if (v.progressPb.visibility == View.VISIBLE) {
                    stilLoading = true
                }
            }
            if (!stilLoading) {
                mailListContentLL.removeAllViews()
                channelsContentLL.removeAllViews()
                presenter.refresh()
            }
        }

        presenter.setup()
    }

    override fun showRefreshProgress(show: Boolean) {
        refreshSrl.isRefreshing = show
    }

    override fun setupChannels(channels: List<Channel>) {
        for (i in 0..channels.size - 1) {
            val currentChannel = channels[i]
            channelCount = channels.size

            //setting the header
            val v = inflator.inflate(R.layout.viewholder_home_card_header, channelsContainerLl, false)
            val logoIv = v.findViewById<ImageView>(R.id.logoIv)
            val headerTv = v.findViewById<TextView>(R.id.headerTv)
            //val rowsContainerLl = v.findViewById<LinearLayout>(R.id.rowsContainerLl)

            logoIv?.let {
                currentChannel?.logo?.let { logo ->
                    Glide.with(context).load(logo.url).into(it)
                }
            }

            v.tag = currentChannel.id
            headerTv.text = "${currentChannel.name}"
            channelsContentLL.addView(v)
        }
        setupBottomView(channels)
    }

    override fun setupChannelControl(channel: Channel, control: Control) {
        for (v in channelsContentLL.views) {
            if (v.tag == channel.id) {
                Timber.e("Found control for channel id ${channel.id} instantiating content")
                val rowsContainerLl = v.findViewById<LinearLayout>(R.id.rowsContainerLl)
                val logoIv = v.findViewById<ImageView>(R.id.logoIv)
                val progressPb = v.findViewById<ProgressBar>(R.id.progressPb)
                progressPb.visibility = View.INVISIBLE
                logoIv.visibility = View.VISIBLE
                when (control.type) {
                    ItemType.RECEIPTS -> {
                        addReceiptCard(control, rowsContainerLl)
                    }
                    ItemType.NEWS -> {
                        addNewsCard(control, rowsContainerLl)
                    }
                    ItemType.IMAGES -> {
                        //contrained to only show 1 row
                        var backgroundColor = channel?.background?.rgba
                        addImageCard(control, rowsContainerLl, backgroundColor)

                    }
                    ItemType.NOTIFICATIONS -> {
                        addNotificationCard(control, rowsContainerLl)

                    }
                    ItemType.MESSAGES -> {
                        addMessageCard(control, rowsContainerLl)

                    }
                    ItemType.FILES -> {
                        addFilesCard(control, rowsContainerLl)
                    }
                }
                return
            }
        }
    }

    private fun addFilesCard(currentChannel: Control, rowsContainerLl: ViewGroup) {
        currentChannel.items?.let { items ->
            for (currentItem in items) {
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
                    if (currentItem.image != null)
                        Glide.with(context).load(R.drawable.ic_menu_uploads).into(it)
                }

                rowsContainerLl.addView(v)
                rowsContainerLl.requestLayout()
            }
        }
    }

    private fun addMessageCard(currentChannel: Control, rowsContainerLl: ViewGroup) {
        currentChannel.items?.let { items ->
            for (currentItem in items) {
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
                    if (currentItem.image != null)
                        Glide.with(context).load(currentItem.image?.url).into(it)
                }

                rowsContainerLl.addView(v)
                rowsContainerLl.requestLayout()
            }
        }
    }

    private fun addNotificationCard(currentChannel: Control, rowsContainerLl: ViewGroup) {

        // empty state
        if (currentChannel.items?.isEmpty() != false) {
            val v = inflator.inflate(R.layout.viewholder_home_notification_row, rowsContainerLl, false)
            val emptyContainer = v.findViewById<LinearLayout>(R.id.emptyContentContainer)
            val contentContainer = v.findViewById<LinearLayout>(R.id.contentContainer)

            emptyContainer.visibility = View.VISIBLE
            contentContainer.visibility = View.GONE

            rowsContainerLl.addView(v)
            rowsContainerLl.requestLayout()
        }

        // non-empty state
        currentChannel.items?.let { items ->
            for (currentItem in items) {
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
    }

    private fun addImageCard(currentChannel: Control, rowsContainerLl: ViewGroup, backgroundColor: String?) {
        val v = inflator.inflate(R.layout.viewholder_home_image_row, rowsContainerLl, false)
        val title = v.findViewById<TextView>(R.id.titleTv)
        val image = v.findViewById<ImageView>(R.id.backgroundIv)
        val background = v.findViewById<LinearLayout>(R.id.backgroundColorLl)

        val currentItem = currentChannel.items?.first()
        //todo this color should come from the channels object
        backgroundColor.let {
            background?.background?.setTint(Color.parseColor(it))
        }
        title.text = currentItem?.title
        image?.let {
            currentItem?.image?.let {
                Glide.with(context).load(currentItem.image?.url).into(image)
            }
        }

        rowsContainerLl.addView(v)
        rowsContainerLl.requestLayout()
    }

    private fun addNewsCard(currentChannel: Control, rowsContainerLl: ViewGroup) {
        currentChannel.items?.let { items ->
            for (currentItem in items) {
                val v = inflator.inflate(R.layout.viewholder_home_news_row, rowsContainerLl, false)
                val title = v.findViewById<TextView>(R.id.titleTv)
                val image = v.findViewById<ImageView>(R.id.imageIv)
                val date = v.findViewById<TextView>(R.id.dateTv)

                title.text = currentItem.title
                date.text = formatter.formatDateRelative(currentItem)
                image?.let {
                    if (currentItem.image != null) {
                        var reqoptions = RequestOptions()
                        reqoptions = reqoptions.transform(RoundedCorners(8))

                        Glide.with(context)
                                .load(currentItem.image?.url)
                                .apply(reqoptions)
                                .into(it)
                    }
                }

                rowsContainerLl.addView(v)
                rowsContainerLl.requestLayout()
            }
        }
    }

    private fun addReceiptCard(currentChannel: Control, rowsContainerLl: ViewGroup) {
        currentChannel.items?.let { items ->
            for (row in items) {
                val v = inflator.inflate(R.layout.viewholder_home_reciept_row, rowsContainerLl, false)

                val nameContainer = v.findViewById<LinearLayout>(R.id.nameSubTitleContainerLl)
                val amountDateContainer = v.findViewById<LinearLayout>(R.id.amountDateContainerLl)
                val soloName = v.findViewById<TextView>(R.id.soloTitleTv)
                val soloAmount = v.findViewById<TextView>(R.id.soloAmountTv)
                val name = v.findViewById<TextView>(R.id.titleTv)
                val adress = v.findViewById<TextView>(R.id.subTitleTv)
                val amount = v.findViewById<TextView>(R.id.amountTv)
                val date = v.findViewById<TextView>(R.id.dateTv)

                val value = row.amount?.value.toString()
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
                    name.text = row.title
                    adress.text = row.description
                }

                rowsContainerLl.addView(v)
                rowsContainerLl.requestLayout()
            }
        }
    }

    override fun showHighlights(messages: List<Message>) {
        //Timber.e("Got them highlights $messages")
        emailCount = messages.size
        if (messages.size == 0) {
            emptyStateLl.visibility = View.VISIBLE
            emailContainerLl.visibility = View.GONE
            if(channelCount>0) {
                emptyStateImageIv.visibility = View.GONE
            }
            if(verifiedUser) {
                emptyStateBtn.text = Translation.home.messagesEmptyButton
                emptyStateHeaderTv.text = Translation.home.messagesEmptyTitle
                emptyStateTextTv.text = Translation.home.messagesEmptyMessage
                emptyStateBtn.setOnClickListener {
                    startActivity(Intent(context, MailOverviewActivity::class.java))
                }
            }
        } else {
            emptyStateLl.visibility = View.GONE
            emailContainerLl.visibility = View.VISIBLE
            mailListContentLL.removeAllViews()


            var showCount = 3 // Not allowed to show more than 3
            if (messages.size < showCount) {
                showCount = messages.size
            }

            for (i in 0..showCount - 1) {
                val v = inflator.inflate(R.layout.viewholder_message, mailListContentLL, false)
                var currentMessage = messages[i]
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
                titleTv.text = currentMessage.sender?.name ?: ""
                subTitleTv.text = currentMessage.subject
                dateTv.text = formatter.formatDateRelative(currentMessage)
                if (i == showCount) {
                    dividerV.visibility = View.GONE
                }

                rootLl?.setOnClickListener {
                    presenter.openMessage(currentMessage)
                }
                mailListContentLL.addView(v)

            }
        }
    }

    override fun showProgress(show: Boolean) {
        contentLl.visibility = if (!show) View.VISIBLE else View.GONE
        progressFl.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun setupBottomView(channels: List<Channel>) {
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

        }
    }

// TODO Old crap that might be reusable


    /*
    override fun onShake() {
       if(!redidValues){
           redidValues = true
           val alert = AlertDialog.Builder(context)
           val layout = inflator.inflate(R.layout.debug_dialog,null,false)
           val mailEt = layout.findViewById<EditText>(R.id.firstEt)
           val channelEt = layout.findViewById<EditText>(R.id.middleEt)
           val verifiedEt = layout.findViewById<EditText>(R.id.lastEt)

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
    */

/*
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
    */
}