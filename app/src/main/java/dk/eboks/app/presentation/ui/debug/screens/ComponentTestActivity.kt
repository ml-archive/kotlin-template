package dk.eboks.app.presentation.ui.debug.screens

import android.os.Bundle
import dk.eboks.app.presentation.base.BaseActivity

class ComponentTestActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_component_test)

        // we dont need stinkin injection here
        //component.inject(this)

    }

}
