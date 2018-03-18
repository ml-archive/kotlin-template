package dk.eboks.app.presentation.ui.components.debug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import javax.inject.Inject
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_debug_options_component.*
import timber.log.Timber


/**
 * Created by bison on 09-02-2018.
 */
class DebugOptionsComponentFragment : BaseFragment(), DebugOptionsComponentContract.View {

    @Inject
    lateinit var presenter : DebugOptionsComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_debug_options_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        applyBtn.setOnClickListener {
            presenter.setConfig(countrySpr.selectedItem as String)
            activity.onBackPressed()
        }

        presenter.setup()
    }

    override fun setupTranslations() {

    }

    override fun showCountrySpinner(configIndex : Int)
    {
        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter.createFromResource(context, R.array.countries, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        countrySpr.adapter = adapter
        countrySpr.setSelection(configIndex)
    }
}