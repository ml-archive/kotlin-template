package dk.eboks.app.presentation.ui.channels.components.content.ekey

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.channel.ekey.BaseEkey
import dk.eboks.app.domain.models.channel.ekey.Ekey
import dk.eboks.app.domain.models.channel.ekey.Login
import dk.eboks.app.domain.models.channel.ekey.Note
import dk.eboks.app.domain.models.channel.ekey.Pin
import dk.eboks.app.presentation.ui.channels.components.content.ekey.additem.EkeyAddItemComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.open.EkeyOpenItemComponentFragment
import dk.eboks.app.presentation.ui.channels.components.settings.ChannelSettingsComponentFragment
import dk.eboks.app.presentation.ui.channels.screens.content.ekey.EkeyContentActivity
import dk.eboks.app.util.dpToPx
import dk.eboks.app.util.guard
import dk.eboks.app.util.putArg
import kotlinx.android.synthetic.main.fragment_channel_ekey.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class EkeyComponentFragment : BaseEkeyFragment(), EkeyComponentContract.View,
    BetterEkeyAdapter.Ekeyclicklistener {
    override fun onEkeyClicked(ekey: BaseEkey) {
        val frag = EkeyOpenItemComponentFragment()
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
    private val actualItems = ArrayList<BaseEkey>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_channel_ekey, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        setupRecycler()

        setupTopBar()

        keysContentRv.isFocusable = false
        headerTv.requestFocus()

        val keys = arguments?.getParcelableArrayList<BaseEkey>("VAULT")
        keys?.let {
            showKeys(it)
        }.guard {
            Timber.d("GUARD")
        }

        addItemBtn.setOnClickListener {
            getBaseActivity()?.addFragmentOnTop(R.id.content, EkeyAddItemComponentFragment(), true)
        }
    }

    private fun setupRecycler() {
        // REFRESH
        refreshSrl.setOnRefreshListener {
            getEkeyBaseActivity()?.presenter?.getData()
        }

        // RECYCLER
        keysContentRv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        ViewCompat.setNestedScrollingEnabled(keysContentRv, false)
        keysContentRv.addItemDecoration(DividerDecoration())
        val adapter = BetterEkeyAdapter(items, this)

        adapter.onActionEvent = { baseEkey ->
            val list = arrayListOf<BaseEkey>()
            actualItems.forEach { item ->
                if (item != baseEkey)
                    list.add(item)
            }
            getEkeyBaseActivity()?.presenter?.putVault(list)
            showKeys(list)
        }

        keysContentRv.adapter = adapter
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
                arguments.putParcelable(Channel::class.java.simpleName, channel)
                getBaseActivity()?.openComponentDrawer(
                    ChannelSettingsComponentFragment::class.java,
                    arguments
                )
            }
            true
        }
    }

    private fun setEmptyState(empty: Boolean) {
        if (empty) {
            emptyStateTv?.visibility = View.VISIBLE
        } else {
            emptyStateTv?.visibility = View.GONE
        }
    }

    fun showKeys(keys: List<BaseEkey>) {
        items.clear()
        actualItems.clear()
        actualItems.addAll(keys)

        setEmptyState(keys.isEmpty())

        // group by type
        keys.filter { it is Login }.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })
            .forEach { items.add(EkeyItem(it)) }
        keys.filter { it is Pin }.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })
            .forEach { items.add(EkeyItem(it)) }
        keys.filter { it is Note }.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })
            .forEach { items.add(EkeyItem(it)) }
        keys.filter { it is Ekey }.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })
            .forEach { items.add(EkeyItem(it)) }

        // add headers
        var index = items.indexOfFirst { it is EkeyItem && it.data is Login }
        if (index >= 0) {
            items.add(index, Header(Translation.ekey.overviewLogins))
        }
        index = items.indexOfFirst { it is EkeyItem && it.data is Pin }
        if (index >= 0) {
            items.add(index, Header(Translation.ekey.overviewPinCodes))
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
        keysContentRv.adapter?.notifyDataSetChanged()
        keysContentRv.invalidate()

        showLoading(false)
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            content.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        } else {
            content.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    inner class DividerDecoration : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
        private val d = resources.getDrawable(R.drawable.shape_divider)

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: androidx.recyclerview.widget.RecyclerView,
            state: androidx.recyclerview.widget.RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)

            outRect.bottom += d.intrinsicHeight
        }

        override fun onDraw(c: Canvas, parent: androidx.recyclerview.widget.RecyclerView) {

            for (i in 0 until parent.childCount - 1) { // not after the last
                val child = parent.getChildAt(i)

                var marginLeft = dpToPx(72)

                val aPos = parent.getChildAdapterPosition(child)
                if (parent.adapter?.getItemViewType(aPos) == 42762461) {
                    marginLeft = 0
                }
                if (parent.adapter?.getItemViewType(aPos + 1) == 42762461) {
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
        fun newInstance(vault: ArrayList<BaseEkey>) =
            EkeyComponentFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("VAULT", vault)
                }
            }
    }
}