package dk.eboks.app.presentation.ui.message.components.opening.receipt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.mail.presentation.ui.message.components.opening.receipt.OpeningReceiptComponentContract
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.util.visible
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.fragment_mail_opening_error_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class OpeningReceiptComponentFragment : BaseFragment(), OpeningReceiptComponentContract.View {

    @Inject
    lateinit var presenter: OpeningReceiptComponentContract.Presenter

    private var voluntaryReceipt: Boolean = false

    private val onLanguageChange: (Locale) -> Unit = { locale ->
        updateTranslation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mail_opening_error_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        voluntaryReceipt = arguments?.getBoolean("voluntaryReceipt") ?: false

        buttonsLl.visible = true
        openMessageButtons.visible = true

        openBtn.setOnClickListener {
            presenter.setShouldProceed(proceed = true, receipt = true)
        }
        openBtn.visibility = View.VISIBLE

        if (voluntaryReceipt) {
            Timber.e("This is a voluntary receipt")
            secondaryOptionBtn.visible = (true)
            secondaryOptionBtn.setOnClickListener {
                presenter.setShouldProceed(proceed = true, receipt = false)
            }
        }

        setupTopBar()
        updateTranslation()
        iconIv.setImageDrawable(resources.getDrawable(R.drawable.icon_48_read_receipt_white))
    }

    override fun onResume() {
        super.onResume()
        NStack.addLanguageChangeListener(onLanguageChange)
    }

    override fun onPause() {
        NStack.removeLanguageChangeListener(onLanguageChange)
        super.onPause()
    }

    private fun updateTranslation() {
        mainTb.title = Translation.message.receiptTitle
        headerTv.text = Translation.message.receiptTitle
        mainTv.text = Translation.message.receiptMessage
        openBtn.text = Translation.message.openMessageWithReceiptButton
        if (voluntaryReceipt)
            secondaryOptionBtn.text = Translation.message.openMessageWithoutReceiptButton
    }

    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            presenter.setShouldProceed(proceed = false, receipt = false)
            activity?.onBackPressed()
        }
    }

    override fun showOpeningProgress(show: Boolean) {
        progressPb.visible = show
        buttonsLl.visible = !show
    }
}