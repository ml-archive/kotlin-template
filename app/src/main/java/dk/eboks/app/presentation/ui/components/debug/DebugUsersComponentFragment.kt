package dk.eboks.app.presentation.ui.components.debug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import dk.eboks.app.R
import dk.eboks.app.domain.models.login.LoginState
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_debug_users_component.*
import javax.inject.Inject

/**
 * Created by Christian on 6/15/2018.
 * @author   Christian
 * @since    6/15/2018.
 */
class DebugUsersComponentFragment: BaseFragment(), DebugUsersComponentContract.View {

    @Inject
    lateinit var presenter : DebugUsersComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_debug_users_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        debugSpTitleTv.text = "DebugUsers"
        applyBtn.text = "Choose"
        applyBtn.setOnClickListener {
            presenter.updateLoginState(countrySpr.selectedItem as LoginState)
            activity.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.makeList()
    }

    override fun showUsers(users : List<LoginState>) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, users)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        countrySpr.adapter = adapter
        countrySpr.invalidate()
    }
}