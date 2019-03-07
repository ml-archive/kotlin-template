package dk.eboks.app.presentation.ui.message.components.detail.reply

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.mail.presentation.ui.message.components.detail.reply.ReplyButtonComponentContract
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.message.screens.reply.ReplyFormActivity
import dk.eboks.app.util.Starter
import kotlinx.android.synthetic.main.fragment_reply_button_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ReplyButtonComponentFragment : BaseFragment(), ReplyButtonComponentContract.View {

    @Inject
    lateinit var presenter: ReplyButtonComponentContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reply_button_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        arguments?.getParcelable<Message>(Message::class.java.simpleName)?.let { msg ->
            replyBtn.setOnClickListener {
                presenter.reply(msg)
            }
        }
    }

    override fun showReplyForm(msg: Message) {
        activity?.run {
            Starter()
                .activity(ReplyFormActivity::class.java)
                .putExtra(Message::class.java.simpleName, msg)
                .start()
        }
    }
}