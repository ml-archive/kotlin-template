package dk.eboks.app.presentation.ui.components.message.opening.promulgation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.fragment_mail_opening_promulgation_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class PromulgationComponentFragment : BaseFragment(), PromulgationComponentContract.View {

    val onLanguageChange : (Locale)->Unit = { locale ->
        Timber.e("Locale changed to locale")
        updateTranslation()
    }

    @Inject
    lateinit var presenter : PromulgationComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_mail_opening_promulgation_component, container, false)
        return rootView

    }

    override fun onResume() {
        super.onResume()
        NStack.addLanguageChangeListener(onLanguageChange)
        // TODO uncomment to make modal
        //getBaseActivity()?.backPressedCallback = { true }
    }

    override fun onPause() {
        NStack.removeLanguageChangeListener(onLanguageChange)
        // TODO uncomment to make modal
        //getBaseActivity()?.backPressedCallback = null
        super.onPause()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        okayBtn.setOnClickListener {
            presenter.setShouldProceed(true)
            activity.onBackPressed()
        }
        okayBtn.visibility = View.VISIBLE
        setupTopBar()
        updateTranslation()

    }

    private fun updateTranslation()
    {
        mainTb.title = Translation.message.promulgationTopBarTitle
        okayBtn.text = Translation.message.promulgationOkButton

    }

    private fun setupTopBar() {

    }

    override fun setPromulgationText(promulgationText: String) {
        termsContentTv.text = promulgationText
    }
}