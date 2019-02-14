package dk.eboks.app.presentation.ui.message.components.detail.payment


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager

import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.message.PaymentOption
import dk.eboks.app.domain.models.shared.Description
import dk.eboks.app.domain.models.shared.Link
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.widgets.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_payment_component.*


class PaymentComponentFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_component, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paymentNotificationSwitch.text = Translation.paymentdrawer.notificationsHeader
        setupListeners()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        paymentOptionsRv.layoutManager = LinearLayoutManager(context)
        paymentOptionsRv.adapter = PaymentOptionsAdapter(listOf(PaymentOption("card", description = Description("", "", link = Link()), status = 1, type = ""), PaymentOption("card", description = Description("", "", link = Link()), status = 1, type = "")))
        paymentOptionsRv.addItemDecoration( DividerItemDecoration(
                drawable = ContextCompat.getDrawable(context!!, R.drawable.shape_divider)!!,
                indentationDp = 72,
                backgroundColor = ContextCompat.getColor(context!!, R.color.white)
        ))
    }

    private fun setupListeners() {

    }

}
