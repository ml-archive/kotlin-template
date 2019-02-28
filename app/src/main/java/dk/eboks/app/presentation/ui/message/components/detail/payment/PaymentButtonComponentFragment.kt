package dk.eboks.app.presentation.ui.message.components.detail.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.message.Payment
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.util.formatPayment
import kotlinx.android.synthetic.main.fragment_payment_button.*
import javax.inject.Inject

class PaymentButtonComponentFragment : BaseFragment(), PaymentButtonComponentContract.View {

    private val payment: Payment?
        get() = arguments?.getParcelable(ARG_PAYMENT)

    @Inject
    lateinit var presenter: PaymentButtonComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_payment_button, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        payment?.let(this::setPaymentDetails)
    }

    private fun setPaymentDetails(payment: Payment) {
        paymentButton.text = "Pay ${payment.amount} ${payment.currency}"
        paymentDueTv.text = payment.status.date?.formatPayment()
        paymentButton.setOnClickListener {
            getBaseActivity()?.openComponentDrawer(PaymentComponentFragment::class.java)
        }
    }

    override fun showPaymentDetails(payment: Payment) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {

        private const val ARG_PAYMENT = "Payment"

        fun newInstance(payment: Payment) : PaymentButtonComponentFragment {
            val args = Bundle().apply { putParcelable(ARG_PAYMENT, payment) }
            return PaymentButtonComponentFragment().apply { arguments = args }
        }

    }

}