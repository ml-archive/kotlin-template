package dk.eboks.app.presentation.ui.components.home

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
    var emailCount = 2
    var channelCount = 0
    var verifiedUser = true
    var messages: MutableList<dk.eboks.app.domain.models.message.Message> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_home_overview_mail_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupEmailState()
    }

    private fun setupEmailState() {
        //  This is just a semi mock setup function to test ui
        // 1. non-verified user with 0 channels
        // 2. non-verified user with 1+ channels
        // 3. Verified user with 0 mails
        // 4. Verified user  with 1-3 mails
        // 5. Verified user  with 4+ mails



        // 1
        if (!verifiedUser && channelCount == 0) {
            emptyStateLl.visibility = View.VISIBLE
            emailContainerLl.visibility = View.GONE
        }

        // 2
        if (!verifiedUser && channelCount > 0) {
            emptyStateLl.visibility = View.VISIBLE
            emailContainerLl.visibility = View.GONE
            imageIv.visibility = View.GONE
        }

        // 3
        if (verifiedUser && emailCount == 0) {
            emptyStateLl.visibility = View.VISIBLE
            emailContainerLl.visibility = View.GONE
            imageIv.visibility = View.GONE
            headerTv.text = "_There's no new messages for you"
            emptyStateBtn.text = "_see all mail"
        }

        // 4
        if (verifiedUser && emailCount < 4 && emailCount > 1) {
            emptyStateLl.visibility = View.GONE
            emailContainerLl.visibility = View.VISIBLE
            mailListContentLL.removeAllViews()

            createMockMails(emailCount)

            for (i in 1..messages.size) {
                val v = inflator.inflate(R.layout.viewholder_message, mailListContentLL, false)
                var currentMessage = messages[i-1]
                val circleIv = v.findViewById<ImageView>(R.id.circleIv)
                val titleTv = v.findViewById<TextView>(R.id.titleTv)
                val subTitleTv = v.findViewById<TextView>(R.id.subTitleTv)
                val urgentTv = v.findViewById<TextView>(R.id.urgentTv)
                val dateTv = v.findViewById<TextView>(R.id.dateTv)
                titleTv.text = currentMessage.id
                subTitleTv.text = currentMessage.subject
                dateTv.text = formatter.formatDate(currentMessage)
                mailListContentLL.addView(v)
                mailListContentLL.requestLayout()
            }
        }

        if (verifiedUser && emailCount > 4) {
            emptyStateLl.visibility = View.GONE
            emailContainerLl.visibility = View.VISIBLE
        }

    }

    fun createMockMails(emailCount: Int){
        for (i in 1..emailCount){
            messages.add(dk.eboks.app.domain.models.message.Message("id"+i,"subject"+i, Date(),false,null,null,null,null,null,null,0,null,null,null,null,null,"note string"))
        }
    }
}