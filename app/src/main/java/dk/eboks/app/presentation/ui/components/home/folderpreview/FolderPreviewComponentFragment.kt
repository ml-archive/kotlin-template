package dk.eboks.app.presentation.ui.components.home.folderpreview

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.verification.VerificationComponentFragment
import dk.eboks.app.presentation.ui.screens.channels.overview.ChannelOverviewActivity
import dk.eboks.app.presentation.ui.screens.home.HomeActivity
import dk.eboks.app.presentation.ui.screens.mail.overview.MailOverviewActivity
import dk.eboks.app.presentation.ui.screens.message.opening.MessageOpeningActivity
import dk.eboks.app.util.Starter
import dk.eboks.app.util.guard
import kotlinx.android.synthetic.main.fragment_folder_preview_component.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class FolderPreviewComponentFragment : BaseFragment(), FolderPreviewComponentContract.View {

    @Inject
    lateinit var presenter : FolderPreviewComponentContract.Presenter

    @Inject
    lateinit var formatter: EboksFormatter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_folder_preview_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        (arguments?.getSerializable(Folder::class.java.simpleName) as Folder)?.let { folder->
            presenter.setup(folder)
        }.guard {
            showEmptyState(true, false)
        }
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        EventBus.getDefault().unregister(this)
        super.onPause()
    }

    override fun showEmptyState(show: Boolean, verifiedUser : Boolean) {
        emptyStateLl.visibility = if(show) View.VISIBLE else View.GONE
        mailListContentLL.visibility = if(!show) View.VISIBLE else View.GONE

        // show the header in the parent activity if said activity is HomeActivity
        if(activity is HomeActivity)
        {
            (activity as HomeActivity).showMailsHeader(!show)
        }
        if(show)
        {
            if(verifiedUser)
            {
                emptyStateBtn.text = Translation.home.messagesEmptyButton
                emptyStateHeaderTv.text = Translation.home.messagesEmptyTitle
                emptyStateTextTv.text = Translation.home.messagesEmptyMessage
                emptyStateBtn.setOnClickListener {
                    startActivity(Intent(context, MailOverviewActivity::class.java))
                }
            }
            else
            {
                emptyStateBtn.text = Translation.home.messagesUnverifiedButton
                emptyStateHeaderTv.text = Translation.home.messagesUnverifiedTitle
                emptyStateTextTv.text = Translation.home.messagesUnverifiedMessage
                emptyStateBtn.setOnClickListener {
                    HomeActivity.refreshOnResume = true
                    getBaseActivity()?.openComponentDrawer(VerificationComponentFragment::class.java)
                }
            }
        }
    }

    override fun showFolder(messages: List<Message>, verifiedUser : Boolean) {

        if(messages.isEmpty())
        {
            showEmptyState(true, verifiedUser)
            return
        }

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

            currentMessage.sender?.logo?.let {
                Glide.with(context).load(it.url).into(circleIv)
            }

            if (currentMessage.unread) {
                circleIv.isSelected = true
                dateTv.setTypeface(null, Typeface.BOLD)
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

    override fun showProgress(show: Boolean) {
        progressFl.visibility = if(show) View.VISIBLE else View.GONE
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: RefreshFolderPreviewEvent) {
        presenter.refresh(false)
    }
}