package dk.dof.birdapp.presentation.ui.whichbird

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.dof.birdapp.R
import dk.dof.birdapp.presentation.base.BaseFragment
import dk.dof.birdapp.presentation.ui.whichbird.selectorDetailView.birdsize.BirdSizeSelectorFragment
import kotlinx.android.synthetic.main.fragment_whichbird.*
import javax.inject.Inject

class WhichBirdComponentFragment : BaseFragment(), WhichBirdComponentContract.View {

    @Inject
    lateinit var presenter : WhichBirdComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_whichbird, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupListeners()
    }

    private fun setupListeners() {
        cvDate.setOnClickListener {  }
        cvSize.setOnClickListener {
            getBaseActivity()?.addFragmentOnTop(R.id.contentFl,BirdSizeSelectorFragment(),true)
        }
        cvWhere.setOnClickListener {  }
        cvSilhouette.setOnClickListener {  }
        cvColor.setOnClickListener {  }
    }

}