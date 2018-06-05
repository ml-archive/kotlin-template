package dk.dof.birdapp.presentation.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.dof.birdapp.R
import dk.dof.birdapp.presentation.base.BaseFragment
import javax.inject.Inject

class MapComponentFragment : BaseFragment(), MapComponentContract.View {

    @Inject
    lateinit var presenter : MapComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_map, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

}