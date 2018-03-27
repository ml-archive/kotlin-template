package dk.eboks.app.presentation.ui.components.message.opening.quarantine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.fragment_mail_opening_error_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class QuarantineComponentFragment : BaseFragment(), QuarantineComponentContract.View {

    @Inject
    lateinit var presenter : QuarantineComponentContract.Presenter

    val onLanguageChange : (Locale)->Unit = { locale ->
        Timber.e("Locale changed to locale")
        updateTranslation()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_mail_opening_error_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupTopBar()
        updateTranslation()
    }

    override fun onResume() {
        super.onResume()
        NStack.addLanguageChangeListener(onLanguageChange)
    }

    override fun onPause() {
        NStack.removeLanguageChangeListener(onLanguageChange)
        super.onPause()
    }

    private fun updateTranslation()
    {
        mainTb.title = "_Quarantined"
        headerTv.text = "_Quarantined"
        mainTv.text = "_Message is quarantined, spilling to the brim with viruses and such most like, I fear yes I do"
    }

    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            presenter.setShouldProceed(false)
            activity.onBackPressed()
        }
    }
}