package dk.nodes.template.presentation.ui.splash

import android.os.Bundle
import android.os.PersistableBundle
import dk.nodes.template.presentation.R
import dk.nodes.template.presentation.ui.base.BaseActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }
}