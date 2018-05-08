package dk.eboks.app.presentation.ui.components.channels.content.ekey

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.ekey.Login
import dk.eboks.app.domain.models.channel.ekey.Note
import dk.eboks.app.domain.models.channel.ekey.Pin
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.channels.content.ekey.additem.EkeyAddItemComponentFragment
import dk.eboks.app.presentation.ui.components.channels.settings.ChannelSettingsComponentFragment
import kotlinx.android.synthetic.main.fragment_channel_ekey.*
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject


/**
 * Created by bison on 09-02-2018.
 */

class EkeyComponentFragment : BaseFragment(), EkeyComponentContract.View {

    @Inject
    lateinit var presenter: EkeyComponentContract.Presenter

    private var keyList: MutableList<EkeyListItem> = ArrayList()
    val headerKeyList = mutableListOf<EkeyListItem>()
    private val adapter = EkeyAdapter()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_channel_ekey, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        createMocks()
        setupRecyclerView()
        setupTopBar()
        addItemBtn.setOnClickListener {
            getBaseActivity()?.addFragmentOnTop(R.id.content, EkeyAddItemComponentFragment(), true)
        }
    }


    private fun setupTopBar() {
        getBaseActivity()?.mainTb?.menu?.clear()

        getBaseActivity()?.mainTb?.title = Translation.ekey.topBarTitle

        getBaseActivity()?.mainTb?.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        getBaseActivity()?.mainTb?.setNavigationOnClickListener {
            getBaseActivity()?.onBackPressed()
        }

        val menuSearch = getBaseActivity()?.mainTb?.menu?.add("_settings")
        menuSearch?.setIcon(R.drawable.ic_settings_red)
        menuSearch?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuSearch?.setOnMenuItemClickListener { item: MenuItem ->
            getBaseActivity()?.openComponentDrawer(
                    ChannelSettingsComponentFragment::class.java)
            true
        }
    }

    private fun setupRecyclerView() {
        setupKeys()

        keysContentRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        keysContentRv.adapter = adapter

        //test stuff
        showMessages(headerKeyList)
    }

    private fun setupKeys() {
        var sortedKeyList: MutableList<EkeyListItem> = ArrayList()

        // sorting the list
        keyList.forEach {
            if (it is EkeyListItem.Data) {
                if (it.data is Login) {
                    sortedKeyList.add(it)
                }
            }
        }

        keyList.forEach {
            if (it is EkeyListItem.Data) {
                if (it.data is Pin) {
                    sortedKeyList.add(it)
                }
            }
        }
        keyList.forEach {
            if (it is EkeyListItem.Data) {
                if (it.data is Note) {
                    sortedKeyList.add(it)
                }
            }
        }


        sortedKeyList.forEach {
            when (it) {
                is EkeyListItem.Data -> {
                    when (it.data) {
                        is Login -> {
                            if (headerKeyList.contains(EkeyListItem.Header(type = "Login")) == false) {
                                headerKeyList.add(EkeyListItem.Header(type = "Login"))
                            }

                            headerKeyList.add(it)
                        }

                        is Pin -> {
                            if (headerKeyList.contains(EkeyListItem.Header(type = "Pin")) == false) {
                                headerKeyList.add(EkeyListItem.Header(type = "Pin"))
                            }

                            headerKeyList.add(it)
                        }

                        is Note -> {
                            if (headerKeyList.contains(EkeyListItem.Header(type = "Note")) == false) {
                                headerKeyList.add(EkeyListItem.Header(type = "Note"))
                            }

                            headerKeyList.add(it)
                        }
                    }
                }
            }
        }

    }


    private fun createMocks() {

        keyList.add(EkeyListItem.Data(Login("test1@gmail.com", "gmailPW1", "Gmail", null)))
        keyList.add(EkeyListItem.Data(Pin("1234", "Visa", null)))
        keyList.add(EkeyListItem.Data(Login("test1@hotmail.com", "hotmailPW1", "Hotmail", null)))
        keyList.add(EkeyListItem.Data(Note("Summerhouse", "Lorem ipsum dolor sit amet")))
        keyList.add(EkeyListItem.Data(Pin("4321", "MasterCard", null)))
    }


    private fun showMessages(keys: List<EkeyListItem>) {
        adapter.keyList.clear()
        adapter.keyList.addAll(keys)
        keysContentRv.adapter.notifyDataSetChanged()
    }
}