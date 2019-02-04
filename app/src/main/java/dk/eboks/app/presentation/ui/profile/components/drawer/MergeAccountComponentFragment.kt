package dk.eboks.app.presentation.ui.profile.components.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.profile.components.main.ProfileInfoComponentFragment
import kotlinx.android.synthetic.main.fragment_profile_merge_account_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class MergeAccountComponentFragment : BaseFragment(), MergeAccountComponentContract.View {

    @Inject
    lateinit var presenter: MergeAccountComponentContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_merge_account_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        mergeAccountsBtn.setOnClickListener { presenter.setMergeStatus(true) }
        keepSeperatedBtn.setOnClickListener { presenter.setMergeStatus(false) }
        ProfileInfoComponentFragment.refreshOnResume = true
    }

    override fun close() {
        activity?.finish()
    }
}