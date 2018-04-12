package dk.eboks.app.presentation.ui.screens.Overlay

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.util.views
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.util.OnLanguageChangedListener
import kotlinx.android.synthetic.main.activity_overlay.*
import kotlinx.android.synthetic.main.viewholder_channel_cards.view.*
import kotlinx.android.synthetic.main.viewholder_overlay_row.view.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class OverlayActivity : BaseActivity(), OverlayContract.View, OnLanguageChangedListener {

    @Inject
    lateinit var presenter: OverlayContract.Presenter

    var buttons: ArrayList<OverlayButton> = ArrayList()
    var handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_overlay)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        getButtons()

        inflateButtons()

        NStack.addLanguageChangeListener(this)
        openAnimation()
        mainFab.setOnClickListener {
            closeAnimation()
        }
    }

    private fun getButtons() {
        buttons = intent.getSerializableExtra("buttons") as ArrayList<OverlayButton>
    }

    private fun inflateButtons() {
        var buttonContainerLl = findViewById<LinearLayout>(R.id.buttonContainerLl)
        for (item in buttons) {
            val v = inflator.inflate(R.layout.viewholder_overlay_row, buttonContainerLl, false)
            val button = v.findViewById<FloatingActionButton>(R.id.buttonFab)
            val text = v.findViewById<TextView>(R.id.textTv)

            text.text = item.text
            button.setImageDrawable(getDrawable(item.icon!!))
            v.tag = button
            buttonContainerLl.addView(v)
            buttonContainerLl.requestLayout()
        }
    }

    override fun onLanguageChanged(locale: Locale) {
        for (v in buttonContainerLl.views) {
            val buttonObj = v.tag as OverlayButton
            v.textTv.text = buttonObj.getText(buttonObj.type)
        }
    }

    private fun closeAnimation() {
//        openInFab.hide()
//                mailFab.hide()
//                printFab.hide()
//                deleteFab.hide()
//                moveFab.hide()
//                openInTv.visibility = View.GONE
//                mailTv.visibility = View.GONE
//                printTv.visibility = View.GONE
//                deleteTv.visibility = View.GONE
//                moveTv.visibility = View.GONE
//                handler?.postDelayed({
//                    fabContainerRl.visibility = View.GONE
//                    finish()
//                }, 100)
        finish()
    }

    private fun openAnimation() {

        var delay = 0L
        for (v in buttonContainerLl.views) {
            handler?.postDelayed({
                v.textTv.visibility = View.VISIBLE
                v.buttonFab.show()
            }, delay)
            delay = delay + 1500
        }

    }

//        openInFab.show()
//                handler?.postDelayed({
//                    openInTv.visibility = View.VISIBLE
//                    mailFab.show()
//                }, 50)
//                handler?.postDelayed({
//                    mailTv.visibility = View.VISIBLE
//                    printFab.show()
//                }, 90)
//                handler?.postDelayed({
//                    printTv.visibility = View.VISIBLE
//                    deleteFab.show()
//                }, 120)
//                handler?.postDelayed({
//                    deleteTv.visibility = View.VISIBLE
//                    moveFab.show()
//                    moveTv.visibility = View.VISIBLE
//                }, 140)
//    }


}
