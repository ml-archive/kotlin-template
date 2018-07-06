package dk.eboks.app.presentation.ui.message.components.opening.recalled

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
class RecalledComponentFragment : BaseFragment(), RecalledComponentContract.View {

    @Inject
    lateinit var presenter : RecalledComponentContract.Presenter

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
        iconIv.setImageDrawable(resources.getDrawable(R.drawable.ic_reply_white))
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
        mainTb.title = Translation.message.recalledTitle
        headerTv.text = Translation.message.recalledTitle
        mainTv.text = Translation.message.recalledMessage
    }

    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            presenter.setShouldProceed(false)
            activity.onBackPressed()
        }
    }
}