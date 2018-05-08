package dk.eboks.app.presentation.ui.components.channels.content.ekey.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.include_toolbar.*
import dk.eboks.app.presentation.ui.components.channels.settings.ChannelSettingsComponentFragment
import kotlinx.android.synthetic.main.fragment_channel_ekey_detail.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class EkeyDetailComponentFragment : BaseFragment(), EkeyDetailComponentContract.View {

    var category: EkeyDetailMode? = null

    @Inject
    lateinit var presenter : EkeyDetailComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_channel_ekey_detail, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        category = arguments.getSerializable("category") as EkeyDetailMode
        setupTopBar()
        setupInputfields()
    }

    private fun setupInputfields() {
        when (category){
            EkeyDetailMode.LOGIN->{
                pinTil.visibility = View.GONE
                usernameTil.visibility = View.VISIBLE
                passwordTil.visibility = View.VISIBLE
            }
            EkeyDetailMode.PIN->{
                pinTil.visibility = View.VISIBLE
                usernameTil.visibility = View.GONE
                passwordTil.visibility = View.GONE
            }
            EkeyDetailMode.NOTE->{
                pinTil.visibility = View.GONE
                usernameTil.visibility = View.GONE
                passwordTil.visibility = View.GONE
            }
        }
    }

    private fun setupTopBar() {
        getBaseActivity()?.mainTb?.menu?.clear()
        var item = ""
        when (category){
            EkeyDetailMode.LOGIN->{item = Translation.ekey.addItemLogin}
            EkeyDetailMode.PIN->{item = Translation.ekey.addItemCards}
            EkeyDetailMode.NOTE->{item = Translation.ekey.addItemNote}
        }

        getBaseActivity()?.mainTb?.title = Translation.ekey.saveTopbar.replace("[item]",item)


        getBaseActivity()?.mainTb?.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        getBaseActivity()?.mainTb?.setNavigationOnClickListener {
            getBaseActivity()?.onBackPressed()
        }

        val menuSearch = getBaseActivity()?.mainTb?.menu?.add(Translation.defaultSection.save.toUpperCase())
        menuSearch?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuSearch?.setOnMenuItemClickListener { item: MenuItem ->
            //todo save clicked
            var temp = "_Save clicked"
            println(temp)
            true
        }
    }

}