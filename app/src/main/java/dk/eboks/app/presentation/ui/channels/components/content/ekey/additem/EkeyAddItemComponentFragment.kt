package dk.eboks.app.presentation.ui.channels.components.content.ekey.additem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.ui.channels.components.content.ekey.BaseEkeyFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.detail.EkeyDetailComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.detail.EkeyDetailMode
import kotlinx.android.synthetic.main.fragment_channel_ekey_additem.*
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class EkeyAddItemComponentFragment : BaseEkeyFragment(), EkeyAddItemComponentContract.View {

    @Inject
    lateinit var presenter: EkeyAddItemComponentContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_channel_ekey_additem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        setupTopBar()

        notesLl.setOnClickListener { showNextView(EkeyDetailMode.NOTE) }
        cardsLl.setOnClickListener { showNextView(EkeyDetailMode.PIN) }
        loginLl.setOnClickListener { showNextView(EkeyDetailMode.LOGIN) }
    }

    private fun setupTopBar() {
        getBaseActivity()?.mainTb?.menu?.clear()

        getBaseActivity()?.mainTb?.title = Translation.ekey.addItemTopBarTitle

        getBaseActivity()?.mainTb?.setNavigationIcon(R.drawable.icon_48_close_red_navigationbar)
        getBaseActivity()?.mainTb?.setNavigationOnClickListener {
            getBaseActivity()?.onBackPressed()
        }
    }

    private fun showNextView(category: EkeyDetailMode) {
        val fragment = EkeyDetailComponentFragment()
        val args = Bundle()
        args.putSerializable("category", category)
        fragment.arguments = args
        getBaseActivity()?.addFragmentOnTop(R.id.content, fragment, true)
    }
}