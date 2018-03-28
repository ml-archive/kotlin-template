package dk.eboks.app.presentation.ui.components.profile.myinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import javax.inject.Inject

class MyInfoComponentFragment : BaseFragment(), MyInfoComponentContract.View {
    @Inject
    lateinit var presenter: MyInfoComponentContract.Presenter

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater?.inflate(R.layout.fragment_profile_myinformation_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

    }

    override fun onResume() {
        super.onResume()
    }


}