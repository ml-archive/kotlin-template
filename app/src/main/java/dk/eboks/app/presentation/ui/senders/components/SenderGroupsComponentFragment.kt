package dk.eboks.app.presentation.ui.senders.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.R.id.listComponentContentLl
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.models.sender.SenderGroup
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.senders.components.register.RegisterGroupComponentFragment
import kotlinx.android.synthetic.main.fragment_list_component.*
import kotlinx.android.synthetic.main.viewholder_title_subtitle.view.*
import javax.inject.Inject

/**
 * Created by Christian on 3/15/2018.
 * @author   Christian
 * @since    3/15/2018.
 */
class SenderGroupsComponentFragment : BaseFragment(), SenderGroupsComponentContract.View {

    @Inject
    lateinit var presenter: SenderGroupsComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_list_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)

        val sender = arguments.getSerializable(Sender::class.simpleName) as Sender?
        if (sender != null) {
            presenter.getSenderGroups(sender) // ask presenter to get the sendergroups for this
        }
    }

    override fun showSenderGroups(sender:Sender) {
        listComponentContentLl.removeAllViews()

        sender.groups?.forEach {
            val v = inflator.inflate(R.layout.viewholder_title_subtitle, listComponentContentLl, false)
            v.titleTv.text = it.name
            v.subTv.text = when (it.registered) {
                0 -> Translation.senderdetails.registeredTypeNo
                1 -> Translation.senderdetails.registeredTypeYes
                else -> ""
            }
            v.setOnClickListener { c ->
                val data = Bundle()
                data.putSerializable(Sender::class.simpleName, sender.id)
                data.putSerializable(SenderGroup::class.simpleName, it)
                getBaseActivity()?.openComponentDrawer(RegisterGroupComponentFragment::class.java, data)
            }
            listComponentContentLl.addView(v)
        }
        view?.requestLayout()
    }

    override fun showError(message: String) {
    }

}