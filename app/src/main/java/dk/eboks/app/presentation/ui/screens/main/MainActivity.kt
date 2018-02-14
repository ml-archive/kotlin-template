package dk.eboks.app.presentation.ui.screens.main

import android.os.Bundle
import android.util.Log
import dk.eboks.app.presentation.base.MainNavigationBaseActivity
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : MainNavigationBaseActivity(), MainContract.View {
    @Inject lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_main)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        circleIv.setOnClickListener {
            circleIv.isSelected = !circleIv.isSelected
        }
    }

    override fun setupTranslations() {

    }

    override fun onResume() {
        super.onResume()
        NStack.translate(this@MainActivity)
    }

    override fun showError(msg: String) {
        Log.e("debug", msg)
    }
}
