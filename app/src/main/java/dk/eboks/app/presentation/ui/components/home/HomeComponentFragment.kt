package dk.eboks.app.presentation.ui.components.home

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.home.Control
import dk.eboks.app.domain.models.home.ItemType
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.mail.list.MailListActivity
import dk.eboks.app.presentation.ui.screens.mail.overview.MailOverviewActivity
import dk.eboks.app.presentation.ui.screens.message.opening.MessageOpeningActivity
import dk.eboks.app.util.Starter
import dk.eboks.app.util.views
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.fragment_home_overview_mail_component.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class HomeComponentFragment : BaseFragment(), HomeComponentContract.View {

    @Inject
    lateinit var presenter: HomeComponentContract.Presenter
    @Inject
    lateinit var formatter: EboksFormatter

    var emailCount = 0
    var channelCount = 0
    override var verifiedUser: Boolean = false

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
            presenter.refresh()
        }

        showBtn.setOnClickListener {
            activity.Starter().activity(MailListActivity::class.java).putExtra("folder", Folder(type = FolderType.HIGHLIGHTS)).start()
        }

        presenter.setup()
    }

    override fun showRefreshProgress(show: Boolean) {
        refreshSrl.isRefreshing = show
    }

    override fun setupChannels(channels: List<Channel>) {
        channelsContentLL.removeAllViews()
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
                val v = inflator.inflate(R.layout.viewholder_home_message, rowsContainerLl, false)
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
                val v = inflator.inflate(R.layout.viewholder_home_message, rowsContainerLl, false)
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
        mailListContentLL.removeAllViews()
        emailCount = messages.size
        if (messages.size == 0) {
            //empty states
            emptyStateLl.visibility = View.VISIBLE
            emailContainerLl.visibility = View.GONE
            if (channelCount > 0) {
                emptyStateImageIv.visibility = View.GONE
            }
            if (verifiedUser) {
                emptyStateBtn.text = Translation.home.messagesEmptyButton
                emptyStateHeaderTv.text = Translation.home.messagesEmptyTitle
                emptyStateTextTv.text = Translation.home.messagesEmptyMessage
                emptyStateBtn.setOnClickListener {
                    startActivity(Intent(context, MailOverviewActivity::class.java))
                }
            }
        } else {
            // not empty
            emptyStateLl.visibility = View.GONE
            emailContainerLl.visibility = View.VISIBLE
            mailListContentLL.removeAllViews()


            var showCount = 3 // Not allowed to show more than 3
            if (messages.size < showCount) {
                showCount = messages.size
            }

            for (i in 0..showCount - 1) {
                val v = inflator.inflate(R.layout.viewholder_home_message, mailListContentLL, false)
                val currentMessage = messages[i]
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
                    dateTv.setTypeface(null,Typeface.BOLD)
                    titleTv.setTypeface(null,Typeface.BOLD)
                } else {
                    dateTv.setTypeface(null,Typeface.NORMAL)
                    titleTv.setTypeface(null,Typeface.NORMAL)
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
                    activity.Starter()
                            .activity(MessageOpeningActivity::class.java)
                            .putExtra(Message::class.java.simpleName, currentMessage)
                            .start()
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
}