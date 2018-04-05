package dk.eboks.app.presentation.ui.components.verification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.base.SheetComponentActivity
import kotlinx.android.synthetic.main.fragment_verification_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class VerificationComponentFragment : BaseFragment(), VerificationComponentContract.View {

    @Inject
    lateinit var presenter : VerificationComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_verification_component, container, false)
        // This will to change translations based on if its signup with nemID, BankID etc
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        cancelTv.setOnClickListener {
            (activity as SheetComponentActivity).onBackPressed()
        }
    }
}