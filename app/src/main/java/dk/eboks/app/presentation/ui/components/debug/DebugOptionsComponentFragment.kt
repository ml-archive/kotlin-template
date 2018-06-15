package dk.eboks.app.presentation.ui.components.debug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import javax.inject.Inject
import android.widget.ArrayAdapter
import dk.eboks.app.domain.config.Environments
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
            activity.onBackPressed()
        }

        presenter.setup()
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
        countrySpr.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                presenter.setConfig(countrySpr.selectedItem as String)
            }
        }
    }

    override fun showEnvironmentSpinner(environments: Map<String, Environments>, curEnv : Environments?) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        val keys = ArrayList(environments.keys)
        val adapter: ArrayAdapter<String> = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_item,
                keys
        )

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        environmentSpr.adapter = adapter
        try {
            environmentSpr.setSelection(environments.values.indexOf(curEnv))
        }
        catch(t : Throwable)
        {
            Timber.e(t)
        }

        environmentSpr.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                presenter.setEnvironment(environmentSpr.selectedItem as String)
            }
        }
    }
}