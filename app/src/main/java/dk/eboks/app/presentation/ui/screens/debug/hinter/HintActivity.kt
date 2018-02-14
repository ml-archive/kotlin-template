package dk.eboks.app.presentation.ui.screens.debug.hinter

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.managers.PrefManager
import dk.eboks.app.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_hint.*
import javax.inject.Inject

class HintActivity : BaseActivity() {
    @Inject lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_hint)
        hintTv.text = hint
        component.inject(this)

        closeBtn.setOnClickListener {
            if(disableCb.isChecked)
            {
                prefManager.setBoolean("disable_hints", true)
            }
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    override fun onBackPressed() {
        if(disableCb.isChecked)
        {
            prefManager.setBoolean("disable_hints", true)
        }
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun setupTranslations() {

    }

    companion object {
        var hint : String = ""
        var hintKey : String = ""
        fun showHint(activity : Activity, newhint : String)
        {
            if(BuildConfig.DEBUG) {
                Handler(Looper.getMainLooper()).postDelayed({
                    val sharedPrefs : SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(activity)
                    if(sharedPrefs != null) {

                        if (!sharedPrefs.getBoolean("disable_hints", false)) {
                            hint = newhint
                            hintKey = "${activity.javaClass.name}_hint_key"

                            // only show each hint once
                            if (!sharedPrefs.getBoolean(hintKey, false)) {
                                activity.startActivity(Intent(activity, HintActivity::class.java))
                                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                sharedPrefs.edit().putBoolean(hintKey, true).apply()
                            }
                        }
                    }
                }, 1000)
            }
        }
    }
}
