package dk.eboks.app.presentation.ui.message.components.detail.payment


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager

import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.Payment
import dk.eboks.app.domain.models.message.PaymentOption
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.base.SheetComponentActivity
import dk.eboks.app.presentation.ui.message.screens.payment.PaymentWebViewActivity
import dk.eboks.app.presentation.widgets.DividerItemDecoration
import dk.eboks.app.util.ActivityStarter
import dk.eboks.app.util.formatPayment
import kotlinx.android.synthetic.main.fragment_payment_component.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class PaymentComponentFragment : BaseFragment(), PaymentComponentContract.View, PaymentOptionsAdapter.PaymentOptionListener {

    @Inject
    lateinit var presenter: PaymentComponentContract.Presenter

    private val payment: Payment?
        get() = arguments?.getParcelable(ARG_PAYMENT)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_component, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paymentNotificationSwitch.text = Translation.paymentdrawer.notificationsHeader
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        payment?.let(this::showPaymentDetails)
    }

    private fun setupRecyclerView(options: List<PaymentOption>) {
        paymentOptionsRv.layoutManager = LinearLayoutManager(context)
        paymentOptionsRv.adapter = PaymentOptionsAdapter(options, this)
        paymentOptionsRv.addItemDecoration( DividerItemDecoration(
                drawable = ContextCompat.getDrawable(context!!, R.drawable.shape_divider)!!,
                indentationDp = 72,
                backgroundColor = ContextCompat.getColor(context!!, R.color.white)
        ))
    }

    override fun onPaymentOptionSelected(paymentOption: PaymentOption) {
        PaymentWebViewActivity.startForResult(this, paymentOption)
    }

    override fun showPaymentDetails(payment: Payment) {
        Timber.d("$payment")
        paymentDueTv.text = payment.status.date?.formatPayment()
        paymentValueTv.text = payment.amount?.toString() ?: ""
        paymentDisclaimer.text = payment.disclaimer
        paymentNotificationSwitch.isChecked = payment.notfication
        setupRecyclerView(payment.options ?: listOf())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PaymentWebViewActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            (getBaseActivity() as SheetComponentActivity).replaceFragment(PaymentStatusComponentFragment())
        }
    }


    companion object {
        private const val ARG_PAYMENT = "payment"
        fun createBundle(payment: Payment) = Bundle().apply { putParcelable(ARG_PAYMENT, payment) }
    }

}
