package dk.nodes.template.presentation.ui.main

import android.os.Bundle
import dk.nodes.template.presentation.R
import dk.nodes.template.presentation.ui.base.BaseActivity
import net.hockeyapp.android.UpdateManager

class MainActivity : BaseActivity() {

    private val viewModel by viewModel<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNStack()
        // setupHockey()
    }

    override fun onDestroy() {
        super.onDestroy()
        // If we checked for hockey updates, unregister
        UpdateManager.unregister()
    }
}
