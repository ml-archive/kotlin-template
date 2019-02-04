package dk.eboks.app.presentation.ui.debug.screens

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_debug.*
import kotlinx.android.synthetic.main.include_toolbar.*

class DebugActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_debug)

        // component.inject(this)
        mainTb.title = "Debug"

        componentTestTv.setOnClickListener {
            startActivity(Intent(this, ComponentTestActivity::class.java))
        }
    }
}
