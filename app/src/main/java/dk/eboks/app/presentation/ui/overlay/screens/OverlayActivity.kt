package dk.eboks.app.presentation.ui.overlay.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.util.views
import dk.eboks.app.util.visible
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.util.OnLanguageChangedListener
import kotlinx.android.synthetic.main.activity_overlay.*
import kotlinx.android.synthetic.main.viewholder_overlay_row.view.*
import java.util.Locale
import javax.inject.Inject

class OverlayActivity : BaseActivity(), OverlayContract.View, OnLanguageChangedListener {

    @Inject
    lateinit var presenter: OverlayContract.Presenter

    var buttons: ArrayList<OverlayButton> = ArrayList()
    var handler = Handler()
    var animationTime = 50L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_overlay)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        NStack.addLanguageChangeListener(this)

        getButtons()
        inflateButtons()
        mainFab.setOnClickListener {
            closeAnimation(null)
        }
    }

    private fun getButtons() {
        buttons = intent.getSerializableExtra(PARAM_BUTTONS) as ArrayList<OverlayButton>
    }

    private fun inflateButtons() {
        val buttonContainerLl = findViewById<LinearLayout>(R.id.buttonContainerLl)
        var delay = animationTime
        for (item in buttons) {
            handler.postDelayed({
                val v = inflator.inflate(
                    R.layout.viewholder_overlay_row,
                    buttonContainerLl,
                    false
                )
                val button = v.findViewById<ImageButton>(R.id.buttonFab)
                val text = v.findViewById<TextView>(R.id.textTv)

                text.text = item.text
                item.icon?.let {
                    button.setImageResource(it)
                }
                v.tag = item
                button.setOnClickListener {
                    closeAnimation(item.type)
                }
                buttonContainerLl.addView(v)
                buttonContainerLl.requestLayout()
                button.visibility = View.VISIBLE
                text.visibility = View.VISIBLE
            }, delay)
            delay += animationTime
        }
    }

    override fun onLanguageChanged(locale: Locale) {
        for (v in buttonContainerLl.views) {
            val buttonObj = v.tag as OverlayButton
            v.textTv.text = buttonObj.getText(buttonObj.type)
        }
    }

    private fun closeAnimation(item: ButtonType?) {
        var delay = animationTime
        for (v in buttonContainerLl.views) {
            handler.postDelayed({
                v.textTv.visibility = View.GONE
                v.buttonFab.visible = false
            }
                , delay)
            delay += animationTime
        }
        handler.postDelayed({
            if (item == null) {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
            val intent = Intent()
            intent.putExtra("res", item)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }, delay)
    }

    companion object {
        const val REQUEST_ID: Int = 1454
        private const val PARAM_BUTTONS = "buttons"
        fun createIntent(context: Context, buttons: ArrayList<OverlayButton>): Intent {
            return Intent(context, OverlayActivity::class.java).putExtra(PARAM_BUTTONS, buttons)
        }
    }
}
