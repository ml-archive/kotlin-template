package dk.eboks.app.presentation.ui.channels.components.content.ekey

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.channel.ekey.*
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.additem.EkeyAddItemComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.open.EkeyOpenItemComponentFragment
import dk.eboks.app.presentation.ui.channels.components.settings.ChannelSettingsComponentFragment
import dk.eboks.app.presentation.ui.channels.screens.content.ekey.EkeyContentActivity
import dk.eboks.app.util.dpToPx
import dk.eboks.app.util.putArg
import kotlinx.android.synthetic.main.fragment_channel_ekey.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject


/**
 * Created by bison on 09-02-2018.
 */
class EkeyComponentFragment : BaseFragment(), EkeyComponentContract.View, BetterEkeyAdapter.Ekeyclicklistener {
    override fun onEkeyClicked(ekey: BaseEkey) {
        val frag = EkeyOpenItemComponentFragment()
        (activity as EkeyContentActivity).setVault(presenter.getKeyList())
        when (ekey) {
            is Login -> {
                frag.putArg("login", ekey)
            }
            is Pin -> {
                frag.putArg("pin", ekey)
            }
            is Note -> {
                frag.putArg("note", ekey)
            }
            is Ekey -> {
                frag.putArg("ekey", ekey)
            }
        }
        getBaseActivity()?.addFragmentOnTop(R.id.content, frag, true)
    }

    @Inject
    lateinit var presenter: EkeyComponentContract.Presenter

    private val items = ArrayList<ListItem>()
    private var pin: String? = null
    private var masterkey: String? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_channel_ekey, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        keysContentRv.layoutManager = LinearLayoutManager(context)
        ViewCompat.setNestedScrollingEnabled(keysContentRv, false)
        keysContentRv.addItemDecoration(DividerDecoration())
        keysContentRv.adapter = BetterEkeyAdapter(items, this)

        setupTopBar()

        keysContentRv.isFocusable = false
        headerTv.requestFocus()

        pin = arguments.getString("PIN_CODE")
        pin?.let {
            (activity as EkeyContentActivity).pin = pin
            presenter.getMasterkey(it)
        }

        addItemBtn.setOnClickListener {
            (activity as EkeyContentActivity).setVault(presenter.getKeyList())
            getBaseActivity()?.addFragmentOnTop(R.id.content, EkeyAddItemComponentFragment(), true)
        }
    }

    private fun setupTopBar() {
        getBaseActivity()?.mainTb?.menu?.clear()

        getBaseActivity()?.mainTb?.title = Translation.ekey.topBarTitle

        getBaseActivity()?.mainTb?.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        getBaseActivity()?.mainTb?.setNavigationOnClickListener {
            getBaseActivity()?.finish()
        }

        val menuSearch = getBaseActivity()?.mainTb?.menu?.add("_settings")
        menuSearch?.setIcon(R.drawable.ic_settings_red)
        menuSearch?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuSearch?.setOnMenuItemClickListener { item: MenuItem ->
            (activity as EkeyContentActivity).channel?.let { channel ->
                val arguments = Bundle()
                arguments.putCharSequence("arguments", "ekey")
                arguments.putSerializable(Channel::class.java.simpleName, channel)
                getBaseActivity()?.openComponentDrawer(
                        ChannelSettingsComponentFragment::class.java,
                        arguments
                )
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        if((activity as EkeyContentActivity).shouldRefresh) {
            (activity as EkeyContentActivity).shouldRefresh = false
            pin?.let {
                presenter.getMasterkey(it)
            }
        }
    }

    private fun setEmptyState(empty: Boolean) {
        if (empty) {
            emptyStateTv.visibility = View.VISIBLE
        } else {
            emptyStateTv.visibility = View.GONE
        }
    }

    override fun onGetMasterkeyError(viewError: ViewError) {
        Timber.d("error: ${viewError.message}")
    }

    override fun showKeys(keys: List<BaseEkey>) {
        items.clear()

        setEmptyState(keys.isEmpty())

        // group by type
        keys.filter { it is Login }.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }).forEach { items.add(EkeyItem(it)) }
        keys.filter { it is Pin }.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }).forEach { items.add(EkeyItem(it)) }
        keys.filter { it is Note }.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }).forEach { items.add(EkeyItem(it)) }
        keys.filter { it is Ekey }.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }).forEach { items.add(EkeyItem(it)) }

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
        index = items.indexOfFirst { it is EkeyItem && it.data is Ekey }
        if (index >= 0) {
            items.add(index, Header(Translation.ekey.overviewEkey))
        }

        // all done
        Timber.d("ADDED ${items.size} items")
        keysContentRv.adapter.notifyDataSetChanged()
        keysContentRv.invalidate()
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
                if (parent.adapter.getItemViewType(aPos) == 42762461) {
                    marginLeft = 0
                }
                if (parent.adapter.getItemViewType(aPos + 1) == 42762461) {
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

    companion object {
        @JvmStatic
        fun newInstance(pin: String) =
                EkeyComponentFragment().apply {
                    arguments = Bundle().apply {
                        putString("PIN_CODE", pin)
                    }
                }
    }
}