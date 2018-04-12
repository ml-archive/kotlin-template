package dk.eboks.app.presentation.ui.screens.Overlay

import android.os.Bundle
import android.os.Handler
import android.view.View
import dk.eboks.app.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_overlay.*
import javax.inject.Inject

class OverlayActivity : BaseActivity(), OverlayContract.View {

    @Inject lateinit var presenter: OverlayContract.Presenter

    var handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_overlay)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        openAnimation()
        mainFab.setOnClickListener {
            closeAnimation()
        }
    }

    private fun closeAnimation() {
        openInFab.hide()
                mailFab.hide()
                printFab.hide()
                deleteFab.hide()
                moveFab.hide()
                openInTv.visibility = View.GONE
                mailTv.visibility = View.GONE
                printTv.visibility = View.GONE
                deleteTv.visibility = View.GONE
                moveTv.visibility = View.GONE
                handler?.postDelayed({
                    fabContainerRl.visibility = View.GONE
                    finish()
                }, 100)
    }

    private fun openAnimation() {
        openInFab.show()
                handler?.postDelayed({
                    openInTv.visibility = View.VISIBLE
                    mailFab.show()
                }, 50)
                handler?.postDelayed({
                    mailTv.visibility = View.VISIBLE
                    printFab.show()
                }, 100)
                handler?.postDelayed({
                    printTv.visibility = View.VISIBLE
                    deleteFab.show()
                }, 150)
                handler?.postDelayed({
                    deleteTv.visibility = View.VISIBLE
                    moveFab.show()
                    moveTv.visibility = View.VISIBLE
                }, 200)
    }


}
