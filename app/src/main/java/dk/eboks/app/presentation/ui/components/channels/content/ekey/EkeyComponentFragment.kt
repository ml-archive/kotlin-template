package dk.eboks.app.presentation.ui.components.channels.content.ekey

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.ekey.Ekey
import dk.eboks.app.domain.models.channel.ekey.Login
import dk.eboks.app.domain.models.channel.ekey.Note
import dk.eboks.app.domain.models.channel.ekey.Pin
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.channels.content.ekey.additem.EkeyAddItemComponentFragment
import dk.eboks.app.presentation.ui.components.channels.content.ekey.open.EkeyOpenItemComponentFragment
import dk.eboks.app.presentation.ui.components.channels.settings.ChannelSettingsComponentFragment
import dk.eboks.app.util.dpToPx
import dk.eboks.app.util.putArg
import kotlinx.android.synthetic.main.fragment_channel_ekey.*
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class EkeyComponentFragment : BaseFragment(), EkeyComponentContract.View, BetterEkeyAdapter.Ekeyclicklistener {
    override fun onEkeyClicked(ekey: Ekey) {
        val frag = EkeyOpenItemComponentFragment()
        when (ekey){
            is Login ->{
                frag.putArg("login", ekey)
            }
            is Pin ->{
                frag.putArg("pin", ekey)
            }
            is Note ->{
                frag.putArg("note", ekey)
            }
        }
        getBaseActivity()?.addFragmentOnTop(R.id.content, frag, true)
    }

    @Inject
    lateinit var presenter: EkeyComponentContract.Presenter

    private val items = ArrayList<ListItem>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_channel_ekey, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        keysContentRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        keysContentRv.addItemDecoration(DividerDecoration())
        keysContentRv.adapter = BetterEkeyAdapter(items,  this)

        setupTopBar()
        addItemBtn.setOnClickListener {
            getBaseActivity()?.addFragmentOnTop(R.id.content, EkeyAddItemComponentFragment(), true)
        }

        presenter.getKeys()
        keysContentRv.isFocusable = false
        headerTv.requestFocus()
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
            getBaseActivity()?.openComponentDrawer( ChannelSettingsComponentFragment::class.java)
            true
        }
    }

    override fun showKeys(keys: List<Ekey>) {
        items.clear()

        // group by type
        keys.filter { it is Login }.forEach { items.add(EkeyItem(it)) }
        keys.filter { it is Pin }.forEach { items.add(EkeyItem(it)) }
        keys.filter { it is Note }.forEach { items.add(EkeyItem(it)) }

        // add headers
        var index = items.indexOfFirst { it is EkeyItem && it.data is Login }
        if (index >= 0) {
            items.add(index, Header(Translation.ekey.overviewLogins))
        }
        index = items.indexOfFirst { it is EkeyItem && it.data is Pin }
        if (index >= 0) {
            items.add(index, Header(Translation.ekey.overviewPins))
        }
        index = items.indexOfFirst { it is EkeyItem && it.data is Note }
        if (index >= 0) {
            items.add(index, Header(Translation.ekey.overviewNotes))
        }

        // all done
        keysContentRv.adapter.notifyDataSetChanged()
    }

    inner class DividerDecoration : RecyclerView.ItemDecoration() {
        private val d = resources.getDrawable(R.drawable.shape_divider)

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)

            outRect.bottom += d.intrinsicHeight
        }

        override fun onDraw(c: Canvas, parent: RecyclerView) {

            for (i in 0 until parent.childCount - 1) { // not after the last
                val child = parent.getChildAt(i)

                var marginLeft = dpToPx(72)

                val aPos = parent.getChildAdapterPosition(child)
                if(parent.adapter.getItemViewType(aPos) == R.layout.item_header) {
                    marginLeft = 0
                }
                if(parent.adapter.getItemViewType(aPos+1) == R.layout.item_header) {
                    marginLeft = 0
                }

                val left = parent.paddingLeft + marginLeft
                val top = child.bottom
                val right = parent.width - parent.paddingRight
                val bottom = child.bottom + d.intrinsicHeight

                d.setBounds(left, top, right, bottom)
                d.draw(c)
            }
        }
    }
}