package dk.eboks.app.presentation.ui.components.senders

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.senders.detail.SenderDetailActivity
import kotlinx.android.synthetic.main.fragment_senders_component.*
import timber.log.Timber

/**
 * Created by Christian on 3/20/2018.
 * @author   Christian
 * @since    3/20/2018.
 */
class SenderListComponentFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_senders_component, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sendersListLl.removeAllViews()

        val senders = arguments.getSerializable(CollectionContainer::class.simpleName) as CollectionContainer?
        senders?.let {
            sendersTitleTv.text = it.description?.title

            it.senders?.forEach {
                val v = inflator.inflate(R.layout.viewholder_sender, sendersListLl, false)
                Timber.v("inflate sender: ${it.name}, ${it.logo?.url}")
                val senderNameTv = v.findViewById<TextView>(R.id.senderNameTv)
                val senderRegisterBtn = v.findViewById<Button>(R.id.senderRegisterBtn)
                val senderLogoIv = v.findViewById<ImageView>(R.id.senderLogoIv)

                Glide.with(context)
                        .load(it.logo?.url)
                        .apply(RequestOptions()
                                .fallback(R.drawable.icon_72_senders_private)
                                .placeholder(R.drawable.icon_72_senders_private)
                        )
                        .into(senderLogoIv)
                senderNameTv.text = it.name
                senderRegisterBtn.visibility = View.VISIBLE
                senderRegisterBtn.text = "Nstack register" // TODO
                senderRegisterBtn.setOnClickListener {
                    Toast.makeText(context, "TODO: register", Toast.LENGTH_SHORT).show()
                }
                v.setOnClickListener { _ ->
                    val i = Intent(context, SenderDetailActivity::class.java)
                    i.putExtra(Sender::class.simpleName, it)
                    startActivity(i)
                }
                sendersListLl.addView(v)
            }
            sendersListLl.requestLayout()
        }
    }

}