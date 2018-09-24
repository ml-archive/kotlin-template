package dk.eboks.app.presentation.ui.channels.components.content.ekey

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.App
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.ekey.*
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.additem.EkeyAddItemComponentFragment
import dk.eboks.app.presentation.ui.channels.components.content.ekey.open.EkeyOpenItemComponentFragment
import dk.eboks.app.presentation.ui.channels.components.settings.ChannelSettingsComponentFragment
import dk.eboks.app.util.dpToPx
import dk.eboks.app.util.putArg
import dk.nodes.locksmith.core.Locksmith
import dk.nodes.locksmith.core.encryption.handlers.EncryptionHandlerImpl
import dk.nodes.locksmith.core.encryption.providers.AesPasswordKeyProviderImpl
import dk.nodes.locksmith.core.preferences.EncryptedPreferences
import dk.nodes.locksmith.core.util.HashingUtils
import dk.nodes.locksmith.core.util.RandomUtils
import kotlinx.android.synthetic.main.fragment_channel_ekey.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


/**
 * Created by bison on 09-02-2018.
 */
class EkeyComponentFragment : BaseFragment(), EkeyComponentContract.View, BetterEkeyAdapter.Ekeyclicklistener {
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
        }
        getBaseActivity()?.addFragmentOnTop(R.id.content, frag, true)
    }

    @Inject
    lateinit var presenter: EkeyComponentContract.Presenter

    private val items = ArrayList<ListItem>()
    private var pin: String? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_channel_ekey, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        keysContentRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        keysContentRv.addItemDecoration(DividerDecoration())
        keysContentRv.adapter = BetterEkeyAdapter(items, this)

        setupTopBar()
        addItemBtn.setOnClickListener {
            getBaseActivity()?.addFragmentOnTop(R.id.content, EkeyAddItemComponentFragment(), true)
        }

        presenter.getKeys()
        keysContentRv.isFocusable = false
        headerTv.requestFocus()

        pin = arguments.getString("PIN_CODE", null)
        if(pin != null) {
            presenter.getMasterkey()
//            generateNewKeyAndSend()
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
            getBaseActivity()?.openComponentDrawer(ChannelSettingsComponentFragment::class.java)
            true
        }
    }

    private fun setEmptyState(empty: Boolean) {
        if (empty) {
            emptyStateTv.visibility = View.VISIBLE
        } else {
            emptyStateTv.visibility = View.GONE
        }
    }

    override fun onMasterkey(masterkey: String?) {
        if(masterkey != null) {
            val handler = EncryptionHandlerImpl(AesPasswordKeyProviderImpl(pin))
            handler.init()

            val decrypted = String(handler.decrypt(masterkey), charset("UTF-8"))
            Timber.d("Decrypted from backend: $decrypted")

            presenter.storeMasterkey(decrypted)

            val keyHash = HashingUtils.sha256AsByteArray(decrypted)

            val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
            val time = dateFormat.format(Date())
            val hash = HashingUtils.hmacSha256(keyHash, time)
            presenter.getKeys(time, hash)
        } else {
            generateNewKeyAndSend()
        }
    }

    override fun onGetMasterkeyError(viewError: ViewError) {
        Timber.d("error: ${viewError.message}")
    }

    private fun generateNewKeyAndSend() {
        Timber.d("pin: $pin")
        val key = RandomUtils.generateRandomString(32)
        Timber.d("gen key: $key")

        val hashed = HashingUtils.sha256AsBase64(key)
        Timber.d("Hash: $hashed")

        val handler = EncryptionHandlerImpl(AesPasswordKeyProviderImpl(pin))
        handler.init()

        val encrypted = handler.encrypt(key.toByteArray(charset("UTF-8")))
        Timber.d("Encrypted: $encrypted")

        //store key
        presenter.storeMasterkey(key)

        //send key to backend
        presenter.setMasterkey(hashed, encrypted)
    }

    override fun showKeys(keys: List<BaseEkey>) {
        items.clear()

        setEmptyState(keys.isEmpty())

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