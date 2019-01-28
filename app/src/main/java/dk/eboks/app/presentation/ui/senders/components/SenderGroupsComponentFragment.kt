package dk.eboks.app.presentation.ui.senders.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import dk.eboks.app.R
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.models.sender.SenderGroup
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.senders.components.register.RegisterGroupComponentFragment
import kotlinx.android.synthetic.main.fragment_list_component.*
import javax.inject.Inject

/**
 * Created by Christian on 3/15/2018.
 * @author   Christian
 * @since    3/15/2018.
 */
class SenderGroupsComponentFragment : BaseFragment(), SenderGroupsComponentContract.View,
    SenderGroupAdapter.Callback {
    @Inject lateinit var presenter: SenderGroupsComponentContract.Presenter
    private lateinit var adapter: SenderGroupAdapter
    private lateinit var sender: Sender

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        adapter = SenderGroupAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        presenter.onViewCreated(this, lifecycle)
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        // ask presenter to get the sendergroups for this
        arguments?.getParcelable<Sender>(Sender::class.simpleName)?.let(presenter::getSenderGroups)
    }

    override fun showSenderGroups(sender: Sender) {
        this.sender = sender
        adapter.setData(sender.groups ?: listOf())
    }

    override fun showError(message: String) {
    }

    override fun onGroupClick(group: SenderGroup) {
        val data = Bundle()
        data.putLong(Sender::class.simpleName, sender.id)
        data.putParcelable(SenderGroup::class.simpleName, group)
        getBaseActivity()?.openComponentDrawer(RegisterGroupComponentFragment::class.java, data)
    }
}