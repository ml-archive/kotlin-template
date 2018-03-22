package dk.eboks.app.presentation.ui.components.senders

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.senders.detail.SenderDetailActivity
import kotlinx.android.synthetic.main.fragment_sender_component.*

/**
 * Created by Christian on 3/20/2018.
 * @author   Christian
 * @since    3/20/2018.
 */
class SenderComponentFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_sender_component, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sender = arguments.getSerializable(Sender::class.simpleName) as Sender?
        sender?.let {
            Glide.with(context).load(sender.logo?.url).into(senderIv)
            senderNameTv.text = it.name
            senderCv.setOnClickListener {
                val i = Intent(context, SenderDetailActivity::class.java)
                i.putExtra(Sender::class.simpleName, sender)
                startActivity(i)
            }
            senderRegisterBtn.text = "Nstack register" // TODO
            senderRegisterBtn.setOnClickListener {
                Toast.makeText(context, "TODO: register", Toast.LENGTH_SHORT).show()
            }
        }
    }

}