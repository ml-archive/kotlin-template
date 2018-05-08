package dk.eboks.app.presentation.ui.components.channels.content.ekey.additem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_channel_ekey_additem.*
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class EkeyAddItemComponentFragment : BaseFragment(), EkeyAddItemComponentContract.View {

    @Inject
    lateinit var presenter : EkeyAddItemComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_channel_ekey_additem, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        setupTopBar()

        notesLl.setOnClickListener {  }
        cardsLl.setOnClickListener {  }
        loginLl.setOnClickListener {  }
    }

    private fun setupTopBar() {
        getBaseActivity()?.mainTb?.menu?.clear()

        getBaseActivity()?.mainTb?.title = Translation.ekey.addItemTopBarTitle

        getBaseActivity()?.mainTb?.setNavigationIcon(R.drawable.icon_48_close_red_navigationbar)
        getBaseActivity()?.mainTb?.setNavigationOnClickListener {
            getBaseActivity()?.onBackPressed()
        }
    }

}