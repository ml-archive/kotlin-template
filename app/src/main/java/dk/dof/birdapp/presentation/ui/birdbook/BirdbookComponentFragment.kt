package dk.dof.birdapp.presentation.ui.birdbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.dof.birdapp.R
import dk.dof.birdapp.presentation.base.BaseFragment
import javax.inject.Inject

class BirdbookComponentFragment : BaseFragment(), BirdbookComponentContract.View {

    @Inject
    lateinit var presenter : BirdbookComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_birdbook, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

}