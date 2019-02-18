package dk.eboks.app.presentation.ui.message.components.detail.payment


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
import dk.eboks.app.presentation.widgets.DividerItemDecoration
import dk.eboks.app.util.formatPayment
import kotlinx.android.synthetic.main.fragment_payment_component.*
import java.util.*
import javax.inject.Inject


class PaymentComponentFragment : BaseFragment(), PaymentComponentContract.View {

    @Inject
    lateinit var presenter: PaymentComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_component, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paymentNotificationSwitch.text = Translation.paymentdrawer.notificationsHeader
        setupListeners()
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    private fun setupRecyclerView(options: List<PaymentOption>) {
        paymentOptionsRv.layoutManager = LinearLayoutManager(context)
        paymentOptionsRv.adapter = PaymentOptionsAdapter(options)
        paymentOptionsRv.addItemDecoration( DividerItemDecoration(
                drawable = ContextCompat.getDrawable(context!!, R.drawable.shape_divider)!!,
                indentationDp = 72,
                backgroundColor = ContextCompat.getColor(context!!, R.color.white)
        ))
    }

    private fun setupListeners() {

    }

    override fun showPaymentDetails(payment: Payment) {
        paymentDueTv.text = payment.status.date?.formatPayment()
        paymentValueTv.text = "${payment.amount} ${payment.currency}"
        paymentDisclaimer.text = payment.disclaimer
        paymentNotificationSwitch.isChecked = payment.notfication
        setupRecyclerView(payment.options ?: listOf())
    }

}
