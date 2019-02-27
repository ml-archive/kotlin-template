package dk.eboks.app.presentation.ui.message.components.opening.privatesender

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.mail.presentation.ui.message.components.opening.privatesender.PrivateSenderWarningComponentContract
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.base.ViewController
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.fragment_mail_opening_error_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class PrivateSenderWarningComponentFragment : BaseFragment(),
    PrivateSenderWarningComponentContract.View {

    private val onLanguageChange: (Locale) -> Unit = { locale ->
        Timber.e("Locale changed to locale")
        updateTranslation()
    }

    @Inject lateinit var presenter: PrivateSenderWarningComponentContract.Presenter
    @Inject lateinit var viewController: ViewController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mail_opening_error_component, container, false)
    }

    override fun onResume() {
        super.onResume()
        NStack.addLanguageChangeListener(onLanguageChange)
    }

    override fun onPause() {
        NStack.removeLanguageChangeListener(onLanguageChange)
        super.onPause()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        openBtn.setOnClickListener {
            viewController.refreshAllOnResume()
            presenter.setShouldProceed(true)
        }
        openBtn.visibility = View.VISIBLE
        buttonsLl.visibility = View.VISIBLE
        openMessageButtons.visibility = View.VISIBLE
        setupTopBar()
        updateTranslation()
        iconIv.setImageDrawable(resources.getDrawable(R.drawable.icon_48_mail_white))
    }

    private fun updateTranslation() {
        mainTb.title = Translation.message.privateSenderTitle
        headerTv.text = Translation.message.privateSenderTitle
        mainTv.text = Translation.message.privateSenderMessage
        openBtn.text = Translation.message.openMessageButton
    }

    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.title = Translation.message.privateSenderTitle
        mainTb.setNavigationOnClickListener {
            presenter.setShouldProceed(false)
            activity?.onBackPressed()
        }
    }

    override fun showOpeningProgress(show: Boolean) {
        progressPb.visibility = if (show) View.VISIBLE else View.GONE
        openBtn.visibility = if (!show) View.VISIBLE else View.GONE
    }
}