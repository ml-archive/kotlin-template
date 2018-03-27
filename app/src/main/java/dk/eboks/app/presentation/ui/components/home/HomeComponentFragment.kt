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
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home_overview_mail_component.*
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
    var emailCount = 0
    var channelCount = 0
    var verifiedUser = false
    var messages: MutableList<dk.eboks.app.domain.models.message.Message> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_home_overview_mail_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
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
        if (channelCount > 0) {
            emptyStateImageIv.visibility = View.GONE
        }
    }

    private fun setupBottomView() {
        if (channelCount == 0) {
            channelsHeaderFL.visibility = View.GONE
            bottomChannelBtn.isEnabled = verifiedUser
            bottomChannelHeaderTv.text = "_Channels"
            bottomChannelTextTv.text = "_You haven’t added any channels yet. Channels give you relevant information and actions right at your fingertips."
        } else {
            bottomChannelBtn.isEnabled = false
            bottomChannelHeaderTv.text = "_Theres more to see!"
            bottomChannelTextTv.text = "_You can add as many channels as you would like, to give you the best overview of your services etc.."
        }
    }

    private fun showMails() {
        if (emailCount == 0) {
            emptyStateLl.visibility = View.VISIBLE
            emailContainerLl.visibility = View.GONE
            emptyStateBtn.isEnabled = verifiedUser
            emptyStateImageIv.visibility = View.GONE
            emptyStateBtn.text = "_See all mail"
            emptyStateHeaderTv.text = "_There’s no new messages for you"
            bottomChannelTextTv.text = "_Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor"
        } else {
            emptyStateLl.visibility = View.GONE
            emailContainerLl.visibility = View.VISIBLE
            mailListContentLL.removeAllViews()

            createMockMails(emailCount)
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
                    val param = dividerV.layoutParams as LinearLayout.LayoutParams
                    param.marginStart = 0
                    dividerV.layoutParams = param
                }
                mailListContentLL.addView(v)
                mailListContentLL.requestLayout()

                if (messages.size > 3) {
                    showBtn.isEnabled = true
                    showBtn.text = "_" + messages.size + " new messages"
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